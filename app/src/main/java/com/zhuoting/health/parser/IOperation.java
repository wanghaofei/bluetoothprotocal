package com.zhuoting.health.parser;

/**
 * Created by Hqs on 2018/1/19
 * 该接口用于在某个时机进行某种操作
 */
public interface IOperation {

    /**
     * 执行发送同步历史运动数据的操作
     */
    void onDoSynchronizedHistorySport();

    /**
     * 执行同步历史睡眠
     */
    void onDoSynchronizedHistorySleep();

    /**
     * 执行同步历史心率
     */
    void onDoSynchronizedHistoryHeartRate();

    /**
     * 执行同步历史血压
     */
    void onDoSynchronizedHistoryBloodPressure();

    /**
     * 执行删除所有运动数据操作
     */
    void onDeleteSport();

    /**
     * 执行删除所有睡眠数据操作
     */
    void onDeleteSleep();

    /**
     * 执行删除所有心率数据操作
     */
    void onDeleteHeartRate();

    /**
     * 执行删除所有血压数据操作
     */
    void onDeleteBloodPressure();

    /**
     * 执行接收所有数据完毕操作
     */
    void onDoReceiveAllComplete();

    

}
