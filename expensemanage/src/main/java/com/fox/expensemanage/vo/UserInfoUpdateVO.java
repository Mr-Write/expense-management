package com.fox.expensemanage.vo;

import com.fox.expensemanage.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author 狐狸半面添
 * @create 2023-04-15 2:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateVO {
    /**
     * 昵称
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.NAME_REGEX, message = "用户名格式错误，应为2-16位的汉字、字母或数字")
    private String nickName;

    /**
     * 头像
     */
    @Pattern(regexp = RegexUtils.RegexPatterns.IMAGE_REGEX, message = "错误的图片链接")
    private String icon;
}
