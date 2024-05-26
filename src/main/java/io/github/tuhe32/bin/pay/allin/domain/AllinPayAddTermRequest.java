package io.github.tuhe32.bin.pay.allin.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 刘斌
 * @date 2024/5/10 11:11
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AllinPayAddTermRequest extends BaseAllinPayRequest implements Serializable {

    private static final long serialVersionUID = -8409011710869543485L;

    /**
     * 终端号
     * 是否必填：是
     * 8位数字
     */
    private String termNo;

    /**
     * 设备类型
     * 是否必填：是
     * 长度：2
     * 默认：11
     */
    private String deviceType;

    /**
     * 操作类型
     * 是否必填：是
     * 长度：2
     */
    private String operation;

    /**
     * 终端状态
     * 是否必填：是
     * 00：启用；01：注销
     */
    private String termState;

    /**
     * 终端地址
     * 是否必填：是
     * 省-市-区-详细地址，如：上海市-上海市-浦东新区-五星路101号5楼
     * 详细地址长度控制在 30 个汉字以内
     * 错误示例：上海市-上海市-浦东新区-上海市浦东新区五星路101号5楼
     */
    private String termAddress;

    /**
     * 终端序列号
     * 是否必填：否
     */
    private String termSn;


}
