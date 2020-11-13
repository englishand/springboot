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
 * 应用：通过程序启动bat文件
 */
@Slf4j
public class ProcessController implements Runnable{

    /**
     * 新建进程
     * @param pid
     */
    public void process(String pid) {
        ProcessCommandArg processCommandArg = resolveCommands(pid);
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            //启动子进程
            process = runtime.exec(processCommandArg.getCommands());
//            process = runtime.exec("D:/batch.bat");
            log.info("process begin:"+processCommandArg.getPid()+":"+processCommandArg.getProcessName());
            //子进程输出
            this.resolveStream(process);
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

    private void resolveStream(Process process) throws Exception {
//        String line ;
        InputStream inputStream = process.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        byte[] by = new byte[1024];
        try {
//            while ((line=br.readLine())!=null){       //出现乱码问题，用read方法解决
//                System.out.println(line);
//            }
            while (inputStream.read(by) != -1){
                System.out.println(new String(by,"gbk"));
            }
            inputStream.close();
            process.waitFor();
            log.info("执行成功");
        }catch (Exception e){
            log.error("执行失败");
            throw e;
        }
    }


    @Override
    public void run() {
        this.process("1");
    }

    public static void main(String[] args){
        PoolConfig poolConfig = new PoolConfig();
        ThreadPoolTaskExecutor executor = poolConfig.executor();
        executor.execute(new ProcessController());
    }
}
