package com.zhuoting.health.Core;

import com.zhuoting.health.bean.ClockInfo;

import java.util.List;

/**
 * @author StevenLiu
 * @date 2019/9/29
 * @desc one word for this interface IQueryAlarm
 */
public interface IQueryAlarm {

    /**
     * 查询闹钟返回结果
     *
     * @param result          0，成功，1，失败，2，断连
     * @param supportTotalNum 支持的最大闹钟数
     * @param totalNum        已经设置的闹钟数
     * @param alarmSet        闹钟集合
     */
    void onResult(int result, int supportTotalNum, int totalNum, List<ClockInfo> alarmSet);
}