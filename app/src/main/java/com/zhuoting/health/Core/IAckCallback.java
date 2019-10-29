package com.zhuoting.health.Core;

import java.util.HashMap;

/**
 * @author StevenLiu
 * @date 2019/9/28
 * @desc one word for this interface IAckCallback
 */
public interface IAckCallback {

    /**
     * 返回结果
     *
     * @param result 0，成功，1，失败，2，断连
     * @param map    数据
     */
    void onResult(int result, HashMap map);
}