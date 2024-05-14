# 工程简介

大幅简化支付功能接口，大大便利化支付功能开发。封装了支付宝，微信，通联等支付接口，支持H5，APP，小程序，PC网页，微信小程序等各种支付方式。

# 使用说明

- 引用：Maven

  ```xml
  <dependency>
    <groupId>io.github.tuhe32</groupId>
    <artifactId>bin-pay</artifactId>
    <version>${最新版}</version>
  </dependency>
  ```
  
# 特点
- 支持多账户，多租户
- 简化接口使用，最多三行代码
- 支持多种支付，极简化开发
- 封装支付接口，支持H5，APP，小程序，PC网页，扫码枪等

# 基本使用
- 通联使用示例

```java
@SpringBootTest
public class PayTest {

    @Resource
    private AllinPayService allinPayService;

    @Bean
    public AllinPayService allinPayService () {
        AllinPayService allinPayService = new AllinPayServiceImpl();
        AllinPayConfig allinPayConfig = new AllinPayConfig("appId", "privateAppKey", "cusId");
        allinPayService.setConfig(allinPayConfig);
        return allinPayService;
    }

    @Test
    public void test() {
        try {
            AllinPayUnitOrderRequest request = new AllinPayUnitOrderRequest()
                    .setReqSn("sn").setTrxAmt(BigDecimal.valueOf(0.01)).setBody("测试支付");
            request.setCusId("newCusId");
            String payInfo = allinPayService.unitOrderPay(request);
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }
}
```

- 微信使用示例

```java
@SpringBootTest
public class PayTest {

    @Resource
    private WxPayI wxPay;

    @Bean
    public WxPayI wxPay() {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(createNewConfig());
        return new WxPay(payService);
    }

    @Test
    public void test() {
        try {
            // 如果只有一个mchId，可以不用切换
            wxPay.switchoverTo("mchId");
            String codeUrl = wxPay.nativePay("sn", BigDecimal.valueOf(0.01), "测试支付");
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

    private static WxPayConfig createNewConfig() {
        WxPayConfig config = new WxPayConfig();
        // 以下参数必填
        config.setAppId("appId");
        config.setMchId("mchId");
        config.setApiV3Key("apiV3Key");
        config.setPrivateKeyPath("privateKeyPath");
        config.setPrivateCertPath("privateCertPath");
        config.setNotifyUrl("notifyUrl");
        return config;
    }
}
```

- 支付宝使用示例
```java
@SpringBootTest
public class PayTest {

    @Resource
    private AliPayService aliPayService;

    @Bean
    public AliPayService aliPayService() {
        AliPayService aliPayService = new AliPayServiceImpl();
        AliPayConfig aliPayConfig = new AliPayConfig("app_id", "your private_key", "alipay_public_key");
        aliPayConfig.setNotifyUrl("notifyUrl");
        aliPayService.setConfig(aliPayConfig);
        return aliPayService;
    }

    @Test
    public void test() {
        try {
            // 如果只有一个mchId，可以不用切换
            aliPayService.switchoverTo("appId");
            String pageRedirectionData = aliPayService.pagePay("sn", BigDecimal.valueOf(0.01), "测试支付");
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

}
```

- 银商使用示例

```java
@SpringBootTest
public class PayTest {

    @Resource
    private UmsPayService umsPayService;

    @Bean
    public UmsPayService umsPayService() {
        UmsPayService umsPayService = new UmsPayServiceImpl();
        UmsPayConfig umsPayConfig = new UmsPayConfig("appId", "appKey", "mid", "tid", "sysCodePrefix", "md5Key");
        umsPayConfig.setNotifyUrl("notifyUrl");
        umsPayConfig.setSubAppId("subAppId");
        umsPayService.setConfig(umsPayConfig);
        return umsPayService;
    }

    @Test
    public void test() {
        try {
            UmsPayMiniResponse response = umsPayService.miniPay(new UmsPayMiniRequest()
                    .setTotalAmount(BigDecimal.valueOf(0.01))
                    .setSubOpenId("subOpenId"));
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

}
```

