package com.zhuoting.health.notify;

import java.util.List;

public interface IDataReceiveComplete {

    /**
     * 获取记录条数回调
     *
     */
    void onQueryDataCount(int num);

    /**
     * 记录概要回调
     */
    void onQueryDataProfile(String time, int collectType, int curFileIndex, int fileTotal);

    /**
     * 记录时间集回调
     */
    void onQueryDataTimes(List<Long> times);

    /**
     * 记录数据量回调
     */
    void onDataCountCompare(int ecgData,int ecgDataLength,int c, int collectType);

    /**
     * 停止上传数据
     * @param dataType 数据类型
     */
    void onStopUploadData(int dataType);

    /**
     * 停止上传数据成功
     * @param responseCode 数据类型
     */
    void onStopUploadDataSuccess(int responseCode);

    /**
     * 删除成功的回调
     */
    void onDataDeleteSuccess(int result);

    /**
     * 历史ECG的数据
     * @param listECG     ECG数据
     */
    void onHistoryECGData(List<Byte> listECG);

    /**
     * 历史PPG的数据
     * @param listPPG     PPG数据
     */
    void onHistoryPPGData(List<Byte> listPPG);

    void onHistoryCollectData0702Finish();


    /**
     * 实时测试中设备的状态
     * 心电电极状态 0x00 接触良好，0x01 心电电极脱落，0x02 心电传感故障；
     * 光电传感器状态 0x00 已佩戴，0x01 未佩戴，0x02 光电传感器故障；
     */
    void onRealTimeUploadDataDeviceState(int ecgStatus, int ppgStatus);

}
