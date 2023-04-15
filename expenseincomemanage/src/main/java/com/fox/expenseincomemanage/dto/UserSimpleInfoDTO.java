package com.fox.expenseincomemanage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-04-15 2:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleInfoDTO {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String icon;
}
