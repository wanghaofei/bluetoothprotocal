package com.zhuoting.health.error;

/**
 * Created by Hqs on 2018/1/8
 * 命令异常回复
 * 命令异常的指令共有 7 位，Command ID 和 Key 与 发送时一致，只适用于 Characteristic 1
 */
public class ErrorType {

    public static final int NOT_SUPPORT_COMMAND_ID = -5 ;           // 不支持的 Command ID  FB
    public static final int NOT_SUPPORT_KEY = -4 ;                                // 不支持的 Key                   FC
    public static final int ERROR_LENGTH = -3 ;                                       // 错误的长度                      FD
    public static final int ERROR_DATA = -2 ;                                             // Data错误                         FE
    public static final int ERROR_CRC = -1;                                                // 错误的CRC16校验码   FF

}
