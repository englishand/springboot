package com.zhy.test.process;

/**
 * 进程对象
 */
public class ProcessCommandArg {

    private String pid;//进程id
    private String processName;//进程名称
    private String osName;//操作系统名称
    private String chasetName;//操作系统字符集
    private String[] commands;//命令

    public ProcessCommandArg(){
        super();
    }

    public ProcessCommandArg(ProcessCommandArg processCommandArg){
        super();
        this.pid = processCommandArg.pid;
        this.osName = processCommandArg.osName;
        this.processName = processCommandArg.processName;
        this.chasetName = processCommandArg.chasetName;
        if (processCommandArg.commands!=null){
            this.commands = new String[processCommandArg.commands.length];
            for (int i = 0; i< processCommandArg.commands.length; i++){
                this.commands[i]= processCommandArg.commands[i];
            }
        }
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getChasetName() {
        return chasetName;
    }

    public void setChasetName(String chasetName) {
        this.chasetName = chasetName;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }
}
