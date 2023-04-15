package com.fox.expenseincomemanage.vo;

import com.fox.expenseincomemanage.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码时的VO
 *
 * @author 狐狸半面添
 * @create 2023-01-29 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePwdVO {

    /**
     * 新密码
     */
    @NotBlank(message = "密码不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PASSWORD_REGEX, message = "密码格式错误，应为8-16位的数字或字母")
    private String password;
    /**
     * 图片验证码字符
     */
    @NotBlank(message = "验证码不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.CAPTCHA_CODE_REGEX, message = "验证码格式错误")
    private String code;
}
