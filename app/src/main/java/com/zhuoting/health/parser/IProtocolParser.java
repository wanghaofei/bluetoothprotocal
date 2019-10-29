package com.zhuoting.health.parser;

/**
 * Created by Hqs on 2018/1/4
 */
public interface IProtocolParser {

    /**
     * 解析出数据的功能类型。
     * 功能类型有 步数、心率、心电图数据等。
     * @param data
     * @return
     */
    int findDataType(byte[] data);

    /**
     *  解析数据
     * @param data
     * @return
     */
    void parseData(byte[] data);
}
