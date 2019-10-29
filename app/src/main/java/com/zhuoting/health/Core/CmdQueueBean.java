package com.zhuoting.health.Core;

/**
 * @author StevenLiu
 * @date 2019/9/28
 * @desc one word for this class
 */
public class CmdQueueBean{

    public static final int SendState_idle = 0; //空闲
    public static final int SendState_working = 5;//命令执行中

    public static final int PriorityState_low = 0;
    public static final int PriorityState_normal = 1;
    public static final int PriorityState_high = 2;

    private int cmdType;
    private int keyType;
    private int cmdPriority; // 0,正常，5，最高 (暂定两个等级)
    private int sendState;  // 0，空闲（未发送，在排队）；5 ，正在发送
    private int groupType; //
    private byte[] willSendData;    // cmd data
    private IAckCallback ackCallback;
    private IResultHeart resultHeart;
    private IResultBlood resultBlood;
    private IResultSleep resultSleep;
    private IResultSport resultSport;
    private IQueryAlarm queryAlarm;

    public CmdQueueBean(int cmd_type, int key_type){
        cmdType = cmd_type;
        keyType = key_type;
        cmdPriority = PriorityState_normal;
        sendState = SendState_idle;
    }

    public IAckCallback getAckCallback() {
        return ackCallback;
    }

    public void setAckCallback(IAckCallback ackCallback) {
        this.ackCallback = ackCallback;
    }

    public IResultBlood getResultBlood(){
        return resultBlood;
    }

    public void setResultBlood(IResultBlood resultBlood){
        this.resultBlood = resultBlood;
    }

    public IResultHeart getResultHeart() {
        return resultHeart;
    }

    public void setResultHeart(IResultHeart resultHeart) {
        this.resultHeart = resultHeart;
    }

    public IResultSleep getResultSleep() {
        return resultSleep;
    }

    public void setResultSleep(IResultSleep resultSleep) {
        this.resultSleep = resultSleep;
    }

    public IResultSport getResultSport() {
        return resultSport;
    }

    public void setResultSport(IResultSport resultSport) {
        this.resultSport = resultSport;
    }

    public IQueryAlarm getQueryAlarm() {
        return queryAlarm;
    }

    public void setQueryAlarm(IQueryAlarm queryAlarm) {
        this.queryAlarm = queryAlarm;
    }

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public int getCmdPriority() {
        return cmdPriority;
    }

    public void setCmdPriority(int cmdPriority) {
        this.cmdPriority = cmdPriority;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public byte[] getWillSendData() {
        return willSendData;
    }

    public void setWillSendData(byte[] willSendData) {
        this.willSendData = willSendData;
    }

    @Override
    public String toString() {
        return "cmdType = " + cmdType +
                " keyType = " + keyType +
                " cmdPriority = " + cmdPriority +
                " sendState = " + sendState;
    }
}
