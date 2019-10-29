package com.zhuoting.health.notify;

/**
 * Created by Hqs on 2018/1/12
 * 该接口用于查看发送请求后，收到设备的响应，以此来判断是否设置成功和设置失败的原因
 */
public interface IRequestResponse {

    /**
     * 固件升级
     *
     * @param result 0 成功进入固件升级模式        1 电量过低          2 设备不支持
     */
    void onFirmWareUpdateResponse(byte result);


    /**
     * 固件删除
     *
     * @param result 0 删除成功              1 删除失败
     */
    void onDeleteDownloadedFirmWare(byte result);


    /**
     * 升级固件的状态
     *
     * @param result 0     成功进入升级模式
     *               1    待下载固件版本号低于当前运行固件版本号
     *               2    待下载固件版本号不等于已下载固件版本号
     *               3    设备存储空间不足
     */
    void onUpdateFWStatusResponse(byte result);


    /**
     * 1 Block 包固件数据的校验信息
     *
     * @param result 0    接收成功,准备接收下一 BLOCK数据
     *               1   所有固件数据成功接收完毕,设备进入升级模式
     *               2   包数不符
     *               3   字节数不符
     *               4  CRC16错误
     */
    void onFirmWareBlockResponse(byte result);


    /**
     * 时间设置
     *
     * @param result 0 成功            1 失败
     */
    void onTimeSettingResponse(byte result);

    /**
     * @param result 0x00:设置成功
     *               0x01:设置失败-超过设备闹钟
     *               数最大值(N-1)
     *               0x02:此闹钟已存在
     *               0x03:类型不支持
     *               0x04:参数错误
     */
    void onAlarmSettingResponse(byte result);


    /**
     * 删除闹钟响应
     *
     * @param result 0 删除成功
     *               1 此闹钟不存在
     *               2 参数错误
     */
    void onDeleteAlarmSetting(byte result);

    /**
     * 修改闹钟
     *
     * @param result 0x00:修改成功
     *               0x01:此闹钟不存在
     *               0x02:修改的闹钟已存在
     */
    void onModifyAlarmResponse(byte result);


    /**
     * 目标设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-类型不支持
     *               0x02:设置失败-参数错误
     */
    void onTargetSettingResponse(byte result);


    /**
     * 用户信息设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onUserInfoSettingResponse(byte result);


    /**
     * 单位设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onUnitSettingResponse(byte result);


    /**
     * 久坐提醒
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onLongsitSettingResponse(byte result);


    /**
     * 防丢失
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onPreventLostOnOffResponse(byte result);


    /**
     * 防丢提醒参数设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onPreventLostParamSettingResponse(byte result);


    /**
     * 左右手佩戴设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onLeftOrRightHandSettingResponse(byte result);


    /**
     * 手机操作系统设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onMobileOSSettingResponse(byte result);


    /**
     * 通知提醒开关
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onNotificationSettingResponse(byte result);


    /**
     * 心率报警设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onHeartRateAlarmSettingResponse(byte result);


    /**
     * 心率监测开关
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onHeartRateMonitorResponse(byte result);


    /**
     * 找手机开关设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onFindMobileOnOffResponse(byte result);


    /**
     * 恢复出厂设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onRecoverToDefaultSettingResponse(byte result);


    /**
     * 勿打扰设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onDisturbeSettingResponse(byte result);


    /**
     * 有氧运动开关
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onAerobicExerciseResponse(byte result);


    /**
     * 语言设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onLanguageSettingResponse(byte result);


    /**
     * 抬腕亮屏设置
     *
     * @param result 0x00:设置成功
     *               0x01:设置失败-参数错误
     */
    void onLeftTheWristToBrightResponse(byte result);


    /**
     * 亮度设置
     *
     * @param result
     */
    void onBrightnessSettingResponse(byte result);


    /**
     * 寻找手环
     *
     * @param result 0 成功
     */
    void onFindBandResponse(byte result);


    /**
     * 心率测试开关控制
     *
     * @param result 0 成功   1 失败
     */
    void onHRMeasurementOnoffControl(byte result);

    /**
     * 血压测试开关控制
     *
     * @param result 0 成功  1 失败
     */
    void onBPMeasurementOnoffControl(byte result);


    /**
     * 血压校准
     *
     * @param result 0 校准成功  1 参数错误   2 设备未正在测血压或者血压还没出值
     */
    void onBloodPressureCalibration(byte result);

    /**
     * App 退出
     *
     * @param result 0 成功  1 失败
     */
    void onAppExitResponse(byte result);


    /**
     * 有氧教练模式开关控制
     *
     * @param result 0 成功   1 失败
     */
    void onAerobicExerciseOnOffResponse(byte result);


    /**
     * 绑定设备
     *
     * @param result 0成功   1 失败  2 设备已绑定
     */
    void onBindDeviceResponse(byte result);


    /**
     * 解除绑定
     *
     * @param result 0 成功        1 失败            2 设备未绑定
     */
    void onUnBindDeviceResponse(byte result);


    /**
     * 信息提醒
     *
     * @param result 0 成功        1 类型不支持
     */
    void onMessageNotificationResponse(byte result);


    /**
     * 实时数据上传
     *
     * @param result 0 设置成功      1 类型不支持
     */
    void onRealTimeDataResponse(byte result);


    /**
     * 波形上传控制
     *
     * @param result 0 成功        1 类型不支持
     */
    void onWaveFormPostResponse(byte result);


    /**
     * 寻找手机
     *
     * @param result 0 成功        1 失败-不支持
     */
    void onFindPhoneResponse(byte result);

    /**
     * 防丢失操作
     *
     * @param result 0 成功        1 失败
     */
    void onPreventLostResponse(byte result);


    /**
     * 接听（拒绝）电话
     *
     * @param result 0 接听        1 拒接
     */
    void onAnswerOrRejectPhoneResponse(byte result);


    /**
     * 0-退出拍照模式  1-进入拍照模式  2-拍照
     * 控制相机
     */
    void onControlTheCamera(byte result);


    /**
     * 控制音乐播放
     * 0-停止 1-播放  2-暂停 3-上一首 4-下一首
     */

    void onControlTheMusic(byte result);

    /**
     * 同步所有数据开关
     *
     * @param result 0 成功         1 失败
     */
    void onSynchronizdAllSwitchResponse(byte result);

    /**
     * 设备发送 BLOCK
     *
     * @param result result
     */
    void onBlockConfirmResponse(byte result);

    /**
     * 删除运动数据
     *
     * @param result 0 成功       1 失败         2 无运动记录
     */
    void onDeleteSportData(byte result);

    /**
     * 删除睡眠数据
     *
     * @param result 0 成功       1 失败         2 相关数据不存在
     */
    void onDeleteSleepData(byte result);

    /**
     * 删除心率数据
     *
     * @param result 0 成功       1 失败         2 相关数据不存在
     */
    void onDeleteHeartRateData(byte result);

    /**
     * 删除血压数据
     *
     * @param result 0 成功       1 失败         2 相关数据不存在
     */
    void onDeleteBloodPressureData(byte result);

    /**
     * 测试血压
     *
     * @param result 00 关闭       02 监测
     */
    void onTestBlood(byte result);

    /**
     * 上传波形控制
     *
     * @param result 0 成功       1 失败
     */
    void onUpdateWaveData(byte result);

    /**
     * 查询采样率
     * 采样率16-1000
     */
    void onSamplingRate(byte result1, byte result2);

    /**
     * 上传光电波形
     *
     * @param result result
     */
    void onUpdateOptoelectronicWaveform(byte[] result);

    /**
     * 运动模式
     *
     * @param result 0 成功       1 失败
     */
    void onsetSportTypeResponse(byte result);

    /**
     * ppg
     *
     * @param result 0 成功       1 失败
     */
    void onsetPPGHZResponse(byte result);

    /**
     * 肤色设置
     */

    void onSetSkinColor(byte result);

    /**
     * 血氧开关控制
     */

    void onBloodOxygenMeasure(byte result);

    /**
     * 呼吸率开关控制
     */

    void onRespiratoryRateMeasure(byte result);

    /**
     * 发送天气预报
     */

    void onWeatherResponse(byte result);

    /**
     * 血压范围设置
     */

    void onBloodPressureResponse(byte result);

    /**
     * 设置睡眠提醒时间
     */
    void onSleepToRemindResponse(byte result);

    /**
     * 设置成功或失败的回调
     * 0,设置成功，1，设置失败
     */
    void onPPGDataCollectSettingResult(int responseCode);

    /**
     * 设置健康参数、预警信息
     */
    void onHealthParamAndWarningInformation(byte result);
}
