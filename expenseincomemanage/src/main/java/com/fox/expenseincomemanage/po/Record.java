package com.fox.expenseincomemanage.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 支出收入记录表
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id(雪花算法)
     */
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 类型，0-支出，1-收入
     */
    private Integer type;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 事件
     */
    private String event;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Record{" +
            "id=" + id +
            ", userId=" + userId +
            ", type=" + type +
            ", money=" + money +
            ", event=" + event +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
