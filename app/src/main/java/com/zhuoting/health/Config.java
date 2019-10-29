package com.zhuoting.health;

/**
 * Created by Hqs on 2018/1/8
 */
public class Config {

    // 可使用128 bit的私有UUID
    public static final String char0 = "BE940000-7333-BE46-B7AE-689E71722BD5";

    // 用于APP或设备发送命令和接收回复， Properties 有：Write,Indicate
    public static final String char1 = "BE940001-7333-BE46-B7AE-689E71722BD5";

    // 用于发送批量数据，比如固件升级时的数据传输，Properties有：Write Without Response
    public static final String char2 = "BE940002-7333-BE46-B7AE-689E71722BD5";

    // 用于设备发送批量数据，比如运动，睡眠，心率记录等，Properties有：Indicate
    public static final String char3 = "BE940003-7333-BE46-B7AE-689E71722BD5";

}
