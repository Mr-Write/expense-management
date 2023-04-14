package com.fox.expensemanage.util;


import com.fox.expensemanage.constant.SmsConstants;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送短信工具类，需要注入容器
 *
 * @author 狐狸半面添
 * @create 2023-01-16 20:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxSmsTemplateUtils {
    /**
     * 应用的AppID
     */
    private int appId;
    /**
     * 应用的AppKey
     */
    private String appKey;
    /**
     * 签名的内容
     */
    private String signContent;
    /**
     * 注册短信模板的id
     */
    private int registerTemplateId;

    /**
     * 发送登录&注册验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否成功
     */
    public boolean sendRegisterCode(String phone, String code) {
        try {
            // 短信的模板参数
            String[] params = {code, Integer.toString(SmsConstants.LOGIN_EFFECTIVE_TIME)};
            // 构建短信发送器
            SmsSingleSender sender = new SmsSingleSender(appId, appKey);
            // 发送短信
            sender.sendWithParam(SmsConstants.CHINA_NATION_CODE, phone, registerTemplateId, params, signContent
                    , "", "");
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
