package com.fox.expenseincomemanage.service;

import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.po.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fox.expenseincomemanage.vo.RecordModifyVO;
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

    /**
     * 删除一条记录
     *
     * @param recordId 记录id
     * @return 删除状况
     */
    Result removeRecord(Integer recordId);

    /**
     * 修改一条记录
     *
     * @param recordModifyVO 金额 + 时间 + 时间 + 记录id
     * @return 修改状况
     */
    Result modifyRecord(RecordModifyVO recordModifyVO);
}
