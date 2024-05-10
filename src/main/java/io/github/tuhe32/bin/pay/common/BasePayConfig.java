package io.github.tuhe32.bin.pay.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 刘斌
 * @date 2024/5/7 15:03
 */
@Getter
@Setter
public class BasePayConfig {

    private String cusId;

    public BasePayConfig() {
    }

    public BasePayConfig(String cusId) {
        this.cusId = cusId;
    }

}
