package com.fox.expenseincomemanage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author 狐狸半面添
 * @create 2023-04-15 10:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordModifyVO {
    /**
     * 金额，单位：分
     */
    @Min(value = 0, message = "金额不允许为负")
    @Max(value = Integer.MAX_VALUE / 100, message = "金额过大，请重新指定")
    private Integer money;
    /**
     * 时间
     */
    private LocalDate time;
    /**
     * 事件，最多128字
     */
    @Length(max = 128, message = "事件描述最多128字")
    private String event;

    @NotNull(message = "记录id不允许为空")
    @Min(value = 1000, message = "记录id格式错误")
    private Integer id;
}
