# 支付模块化

> 现模块已继承支付类型有：  

- 微信支付
- 支付宝支付

## 1. 引入jar
```xml
<dependency>
  <groupId>com.github.catalpaflat</groupId>
  <artifactId>module-pay</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 2. 模块使用配置：

```yaml
catalpaflat：
  pay:
    wx:
      mch_id:
      merchant_pay_key:
      notify_url:
      app_ids:
        app:
        mini:
        h5:
        official_account:
        slot_card:
        scan_code:
      h5_info:
        type: 场景类型:Wap
        wap_url: WAP网站URL地址:https://pay.qq.com
        wap_name: WAP 网站名:腾讯充值
    alipay:
      app_id:
      private_key:
      alipay_public_key:
      partner:
      notify_url:
```

## 3. 微信注入Spring配置

```java
@Configuration
public class CFPayResourceConfig {
    @Bean
    public CFPayConfig cfPayConfig() {
        return new CFPayConfig();
    }

    @Bean
    public CFWXPayHandlerEndpoint cfwxPayHandlerEndpoint(CFPayConfig cfPayConfig) {
        return new CFWXPayHandlerEndpoint(cfPayConfig);
    }

    @Bean
    public CFWXPayUnifiedOrderHandler cfwxPayUnifiedOrderHandler(CFPayConfig cfPayConfig) {
        return new CFWXPayUnifiedOrderHandler(cfPayConfig);
    }

    @Bean
    public CFWXPayOrderStatusQueryHandler cfwxPayOrderStatusQueryHandler(CFPayConfig cfPayConfig) {
        return new CFWXPayOrderStatusQueryHandler(cfPayConfig);
    }

    @Bean
    public CFAlipayHandlerEndpoint cfAlipayHandlerEndpoint(CFPayConfig cfPayConfig) {
        return new CFAlipayHandlerEndpoint(cfPayConfig);
    }

    @Bean
    public CFAlipayH5Handler cfAlipayH5Handler(CFPayConfig cfPayConfig) {
        return new CFAlipayH5Handler(cfPayConfig);
    }

    @Bean
    public CFAlipayAppHandler cfAlipayAppHandler(CFPayConfig cfPayConfig) {
        return new CFAlipayAppHandler(cfPayConfig);
    }

    @Bean
    public CFAlipayQueryHandler cfAlipayQueryHandler(CFPayConfig cfPayConfig) {
        return new CFAlipayQueryHandler(cfPayConfig);
    }
}

```

## 4. 微信支付使用

```java
@RestController
@RequestMapping("test")
public class EmptyIdal {
    @Resource
    private CFWXPayUnifiedOrderHandler cfwxPayUnifiedOrderHandler;
    @Resource
    private CFWXPayOrderStatusQueryHandler cfwxPayOrderStatusQueryHandler;

    @Resource
    private CFAlipayAppHandler cfAlipayAppHandler;
    @Resource
    private CFAlipayQueryHandler cfAlipayQueryHandler;
    
    @GetMapping("wx")
    public Map<String, Object> testWx() throws Exception {
        CFWxPayInfo cfWxPayInfo = new CFWxPayInfo("100", "odyO05BBjHEqIrPu1R5k9WbNwaC4", "192.168.10.31",
                "用户充值-微信支付", "C15221183443359J", WX_PAY_DEFAULT_TRADE_TYPE, WXPayGenreEnum.OFFICIAL_ACCOUNT);
        return cfwxPayUnifiedOrderHandler.unifiedOrder(cfWxPayInfo);
    }

    @GetMapping("wx/query")
    public Map<String, Object> testWxQuery() throws Exception {
        return cfwxPayOrderStatusQueryHandler.obtainOrderStatusByOrderNum("C15221183443359J", WXPayGenreEnum.OFFICIAL_ACCOUNT);
    }
    
    @GetMapping("alipay")
    public AlipayTradeAppPayResponse testAlipay(){
        CFAlipayCreateInfo cfAlipayCreateInfo = new CFAlipayCreateInfo();
        cfAlipayCreateInfo.setBillNo("C15221183443359J");
        cfAlipayCreateInfo.setBody("Iphone6 16G");
        cfAlipayCreateInfo.setSubject("大乐透");
        cfAlipayCreateInfo.setTotalAmount(new BigDecimal(22.67));
        return cfAlipayAppHandler.aliAppPay(cfAlipayCreateInfo);
    }
    
    @GetMapping("alipay/query")
    public AlipayTradeQueryResponse testAlipayQuery(){
        return cfAlipayQueryHandler.queryPayResult(new CfAlipayQueryInfo("C15221183443359J","2014112611001004680 073956707"));
    }
}
```
