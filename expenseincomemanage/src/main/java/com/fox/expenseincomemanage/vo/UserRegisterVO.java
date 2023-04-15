package com.fox.expenseincomemanage.vo;

import com.fox.expenseincomemanage.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 狐狸半面添
 * @create 2023-04-15 1:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterVO {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PHONE_REGEX, message = "手机号格式错误")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.VERIFY_CODE_REGEX, message = "验证码格式错误")
    private String code;

    /**
     * 密码
     */
    @NotBlank(message = "密码不允许为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.PASSWORD_REGEX, message = "密码格式错误，应为8-16位的数字或字母")
    private String password;
}
