# 工程简介

友好的接口封装，完善的日志记录，大幅简化的支付开发，避免了支付功能开发中常常踩到的坑。目前已覆盖了微信、支付宝，通联、银商等交易渠道的小程序、H5，APP，PC网页，扫码付等各种常用的场景。

# 使用说明

- 引用：Maven

  ```xml
  <dependency>
    <groupId>io.github.tuhe32</groupId>
    <artifactId>bin-pay</artifactId>
    <version>1.0.7</version>
  </dependency>
  ```
  
# 特点
- 支持多账户，多租户
- 友好的接口封装，最多三行代码
- 支持多种支付渠道
- 封装多种交易场景的支付接口，如H5，APP，小程序，PC网页，扫码枪等

# 基本使用
- 通联使用示例

具体Demo参考test下的AllinPayTest.java
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
            allinPayService.switchoverTo("cusId");
            String payInfo = allinPayService.unitOrderPay(new AllinPayUnitOrderRequest()
                    .setReqSn("sn").setTrxAmt(BigDecimal.valueOf(0.01)).setPayType("W02").setSubOpenId("openId"));
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

具体Demo参考test下的WechatTest.java
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

具体Demo参考test下的AlipayTest.java
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

具体Demo参考test下的UmsTest.java
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

### 不喜欢try-catch的可以用以下方式

```java
    public void testPay2() {
        String orderSn = "订单号";
        BigDecimal orderAmount = BigDecimal.ONE;
        String subject = "测试支付";
        // 不喜欢try-catch的可以用以下两种方式实现成功和失败，所有的方法都可以这样封装
        PayBroker.broker(() -> wxPayI.nativePay(orderSn, orderAmount, subject), result -> {
            System.out.println(result);
        }, e -> {
            log.error("查询失败：{}", e.getMessage());
        });

        PayOptional.of(() -> wxPayI.nativePay(orderSn, orderAmount, subject))
                .success(result -> {
                    System.out.println(result);
                }).fail(e -> {
                    log.error(e.getMessage());
                }).get();
    }
```


