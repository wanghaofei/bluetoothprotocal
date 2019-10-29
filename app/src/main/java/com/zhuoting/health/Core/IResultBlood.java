package com.zhuoting.health.Core;

import com.zhuoting.health.bean.BloodInfo;

import java.util.List;

/**
 * @author StevenLiu
 * @date 2019/9/29
 * @desc one word for this interface IResultBlood
 */
public interface IResultBlood {

    /**
     * 返回结果
     *
     * @param result 0，成功，1，失败，2，断连
     * @param bloods 血压数据
     */
    void onResult(int result, List<BloodInfo> bloods);
}