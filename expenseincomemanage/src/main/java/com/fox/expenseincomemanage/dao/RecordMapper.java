package com.fox.expenseincomemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fox.expenseincomemanage.po.Record;
import com.fox.expenseincomemanage.query.RecordProportion;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 支出收入记录表 Mapper 接口
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
public interface RecordMapper extends BaseMapper<Record> {

    /**
     * 查询收入支出比例
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param minMoney  最小金额
     * @param maxMoney  最大金额
     * @param userId 用户id
     * @return 两种类型的总金额信息
     */
    List<RecordProportion> selectProportion(@Param("startTime") LocalDate startTime,
                                            @Param("endTime") LocalDate endTime,
                                            @Param("minMoney") Integer minMoney,
                                            @Param("maxMoney") Integer maxMoney,
                                            @Param("userId")Long userId);
}
