package io.github.tuhe32.bin.pay.common;

import io.github.tuhe32.bin.pay.common.exception.PayException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/7 15:01
 */
@Slf4j
public abstract class BasePayServiceImpl {

    protected Map<String, BasePayConfig> configMap = new HashMap<>();

    private BasePayConfigHolderI configHolder;

    public void setConfigHolder(BasePayConfigHolderI configHolder) {
        this.configHolder = configHolder;
        this.configMap = new HashMap<>(6);
    }

    public BasePayConfig getConfig() {
        if (this.configMap.size() == 1) {
            // 只有一个商户号，直接返回其配置即可
            return this.configMap.values().iterator().next();
        }
        return this.configMap.get(this.configHolder.get());
    }

    public void setConfig(BasePayConfig config) {
        final String defaultCusId = config.getCusId();
        this.setMultiConfig(Map.of(defaultCusId, config), defaultCusId);
    }

    public void addConfig(String cusId, BasePayConfig basePayConfig) {
        synchronized (this) {
            if (this.configMap == null) {
                this.setConfig(basePayConfig);
            } else {
                this.configHolder.set(cusId);
                this.configMap.put(cusId, basePayConfig);
            }
        }
    }

    public void removeConfig(String cusId) {
        synchronized (this) {
            if (this.configMap.size() == 1) {
                this.configMap.remove(cusId);
                log.warn("已删除最后一个商户号配置：{}，须立即使用setConfig或setMultiConfig添加配置", cusId);
                return;
            }
            if (this.configHolder.get().equals(cusId)) {
                this.configMap.remove(cusId);
                final String defaultCusId = this.configMap.keySet().iterator().next();
                this.configHolder.set(defaultCusId);
                log.warn("已删除默认商户号配置，商户号【{}】被设为默认配置", defaultCusId);
                return;
            }
            this.configMap.remove(cusId);
        }
    }

    public void setMultiConfig(Map<String, BasePayConfig> basePayConfig) {
        this.setMultiConfig(basePayConfig, basePayConfig.keySet().iterator().next());
    }

    public void setMultiConfig(Map<String, BasePayConfig> basePayConfig, String defaultCusId) {
        this.configMap = new HashMap<>(basePayConfig);
        this.configHolder.set(defaultCusId);
    }

    public boolean switchover(String cusId) {
        if (this.configMap.containsKey(cusId)) {
            this.configHolder.set(cusId);
            return true;
        }
        log.error("无法找到对应【{}】的商户号配置信息，请核实！", cusId);
        return false;
    }

    public boolean switchoverTo(String cusId) throws PayException {
        if (this.configMap.containsKey(cusId)) {
            this.configHolder.set(cusId);
            return true;
        }
        throw new PayException(String.format("无法找到对应【%s】的商户号配置信息，请核实！", cusId));
    }


}
