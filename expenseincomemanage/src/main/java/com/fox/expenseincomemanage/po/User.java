package com.fox.expenseincomemanage.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id(雪花算法)
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String icon;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0表示未删除，1表示已删除）
     */
    @TableLogic
    private Integer isDeleted;
}
