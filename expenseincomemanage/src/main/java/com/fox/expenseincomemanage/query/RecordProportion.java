package com.fox.expenseincomemanage.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 狐狸半面添
 * @create 2023-04-15 12:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordProportion {
    /**
     * 记录类型
     */
    private Integer type;
    /**
     * 总金额
     */
    private Integer allMoney;
}
