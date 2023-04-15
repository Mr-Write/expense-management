package com.fox.expenseincomemanage.service;

import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.po.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fox.expenseincomemanage.vo.RecordSaveVO;

/**
 * <p>
 * 支出收入记录表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
public interface RecordService extends IService<Record> {

    /**
     * 添加收入或支出记录
     *
     * @param recordSaveVO 金额 + 时间 + 事件 + 类型
     * @return 记录id
     */
    Result saveRecord(RecordSaveVO recordSaveVO);
}
