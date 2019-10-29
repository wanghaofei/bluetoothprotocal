package com.zhuoting.health.Core;

import java.util.ArrayList;

/**
 * @author StevenLiu
 * @date 2019/9/28
 * @desc one word for this class CmdHelper
 */
public class CmdHelper {

    private static CmdHelper instance;
    private ArrayList<CmdQueueBean> cmdQueueBeans;

    public static CmdHelper getInstance(){
        if (instance == null){
            instance = new CmdHelper();
        }
        return instance;
    }

    public CmdHelper(){
        cmdQueueBeans = new ArrayList<>();
    }

    private void push(CmdQueueBean cmdQueueBean){
        cmdQueueBeans.add(cmdQueueBean);
        CmdQueueBean topBean = top();
        if (topBean != null && topBean.getSendState() == 0){
            //发送数据

        }
    }

    private void pop(){
        if (cmdQueueBeans.size() > 0){
            cmdQueueBeans.remove(0);
        }
    }

    private CmdQueueBean top(){
        if (cmdQueueBeans.size() > 0){
            return cmdQueueBeans.get(0);
        }
        return null;
    }


}