package com.zhuoting.health.notify;

/**
 * Created by Hqs on 2018/1/12
 * 命令异常回复
 * COMMAND ID 和 KEY 一致，长度为 7 位，Data包含错误类型
 */
public interface IErrorCommand {

    /**
     * 错误回调
     * @param commandIdAndKey               发错的 CommandId 和 Key
     * @param errorType                                 错误类型 见 error 包的 ErrorType 类
     */
    void onErrorCommand(String commandIdAndKey, int errorType);
}
