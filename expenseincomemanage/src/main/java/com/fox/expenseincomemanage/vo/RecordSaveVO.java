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
public class RecordSaveVO {
    /**
     * 金额，单位：分
     */
    @NotNull(message = "金额不允许为空")
    @Min(value = 0,message = "金额不允许为负")
    @Max(value = Integer.MAX_VALUE/100,message = "金额过大，请重新指定")
    private Integer money;
    /**
     * 时间
     */
    @NotNull(message = "时间不允许为空")
    private LocalDate time;
    /**
     * 事件，最多128字
     */
    @NotNull(message = "时间描述不允许为空")
    @Length(max = 128,message = "事件描述最多128字")
    private String event;

    /**
     * 类型，0-支出，1-收入
     */
    @NotNull(message = "类型不允许为空")
    @Range(min = 0,max = 1,message = "错误的类型选择")
    private Integer type;
}
