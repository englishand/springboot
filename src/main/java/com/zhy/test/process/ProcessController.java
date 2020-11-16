package com.zhy.test.process;

import com.zhy.test.enableAsync.PoolConfig;
import com.zhy.test.utils.SystemPropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 进程控制器
 * 应用：通过程序启动bat文件、exe程序等。
 */
@Slf4j
public class ProcessController{

    /**
     * 新建进程
     * @param pid
     */
    public void process(String pid) {
        PoolConfig poolConfig = new PoolConfig();
        ThreadPoolTaskExecutor executor = poolConfig.executor();

        ProcessCommandArg processCommandArg = resolveCommands(pid);
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            //启动子进程
            process = runtime.exec(processCommandArg.getCommands());
//            process = runtime.exec("D:/batch.bat");
            log.info("process begin:"+processCommandArg.getPid()+":"+processCommandArg.getProcessName());
            //子进程输出
            this.resolveOutput(process,executor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            //销毁子进程
            if (process!=null){
                process.destroy();
            }
            log.info("进程销毁");
        }
    }

    /**
     *  销毁指定线程
     * @param pid
     */
    public void destroy(String pid){
        ProcessCommandArg commandArg = resolveCommands(pid);
    }

    /**
     * 解析命令
     * @param pid
     * @return
     */
    public ProcessCommandArg resolveCommands(String pid){
        String processName = "";
        ProcessCommandArg commandArg = switchCommandArg();
        commandArg.setPid(pid);
        if (pid.equals("1")){
            processName = "新核心";
        }else if (pid.equals("2")){
            processName = "信用卡核心";
        }else if (pid.equals("4")){
            processName = "银联无卡";
        }else if (pid.equals("5")){
            processName = "电子账户";
        }else {
            processName = "村行核心";
        }
        commandArg.setProcessName(processName);
        String[] commands = commandArg.getCommands();
        //设置命令行最后一个参数为PID,替换原有PID-PLACEHOLDER
        commands[commands.length-1] = pid;
        return commandArg;
    }

    /**
     * 选择对应操作系统的命令参数
     * @return
     */
    private ProcessCommandArg switchCommandArg(){
        String osName = System.getProperty(SystemPropertiesUtil.OS_NAME);
        ProcessCommandArg command = new ProcessCommandArg();
        if (osName.toLowerCase().contains(SystemPropertiesUtil.WINDOWS_OS)){
            command.setOsName("windows");
            command.setChasetName("GBK");
            command.setCommands(new String[]{"cmd","/c","start /b","D:/batch.bat","PID-PlaceHolder"});
        }
        return command;
    }

    /**
     * 处理子进程输出流
     * @param process
     * @throws Exception
     */
    private void resolveInputStream(Process process) throws Exception {
//        String line ;
        try {
            //获取进程的标准输入流
            InputStream inputStream = process.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            byte[] by = new byte[1024];

//            while ((line=br.readLine())!=null){       //出现乱码问题，用read方法解决
//                System.out.println(line);
//            }
            while (inputStream.read(by) != -1) {
                System.out.println(new String(by, "gbk"));
            }
            inputStream.close();
            log.info("执行成功");
        } catch (Exception e) {
            log.error("执行失败");
            throw e;
        }
    }

    /**
     * 处理子进程错误流
     * @param process
     * @throws Exception
     */
    private void resolveErrorStream(Process process) throws Exception{
        try {
            //获取进程的标准错误流
            InputStream errorStream = process.getErrorStream();
            byte[] bytes = new byte[1024];
            while (errorStream.read(bytes)!=-1){
                System.out.println(new String(bytes,"gbk"));
            }
            errorStream.close();
            log.info("执行成功");
        }catch (Exception e){
            log.error("执行失败");
            throw e;
        }

    }

    /**
     * 子进程输出
     * @param process
     * @param executor
     */
    public void resolveOutput(Process process,ThreadPoolTaskExecutor executor){
        try {
            //处理子进程错误流
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        resolveErrorStream(process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //处理子进程输出流
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        resolveInputStream(process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //主进程调用proces.waitfor()等待子进程完成。这里我们使用了ThreadPoolTaskExecutor执行的子进程故不会在主进程中。
            int ret = process.waitFor();
            System.out.println("return value:"+ret);
            System.out.println(process.exitValue());
            log.info("event:{}","RunExeForWindows",process.exitValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ProcessController pc = new ProcessController();
        pc.process("1");
    }

    /**
     *  waitfor 问题描述分析
     * 1.主进程中调用pb.start/pb.exec会创建一个子进程，用于执行shell /exe 脚本。子进程创建后会和主进程分别独立运行。
     * 2. 因为主进程需要等待脚本执行完成，然后对脚本返回值或输出进行处理，所以这里主进程调用Process.waitfor等待子进程完成。
     * 3. 子进程执行过程就是不断的打印信息。主进程中可以通过Process.getInputStream和Process.getErrorStream获取并处理。
     * 4. 这时候子进程不断向主进程发生数据，而主进程调用Process.waitfor后已挂起。当前子进程和主进程之间的缓冲区塞满后，子进程不能继续写数据，然后也会挂起。
     * 5. 这样子进程等待主进程读取数据，主进程等待子进程结束，两个进程相互等待，最终导致死锁。
     *
     * 解决方法：在waitfor之前，单独启动两个额外的线程，分别用于处理InputStream和ErrorStream
     */
}
