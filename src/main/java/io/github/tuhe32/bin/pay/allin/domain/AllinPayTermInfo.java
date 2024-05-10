package io.github.tuhe32.bin.pay.allin.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 刘斌
 * @date 2024/5/9 11:49
 */
@Getter
@Setter
public class AllinPayTermInfo {

    /**
     * 终端号
     */
    private String termno;
    /**
     * 设备类型
     */
    private String devicetype = "11";
    /**
     * 终端序列号
     */
    private String termsn;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 终端IP
     */
    private String deviceip;
}
