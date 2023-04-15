package com.fox.expenseincomemanage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fox.expenseincomemanage.dao.RecordMapper;
import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.po.Record;
import com.fox.expenseincomemanage.service.RecordService;
import com.fox.expenseincomemanage.util.UserHolderUtils;
import com.fox.expenseincomemanage.vo.RecordSaveVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 支出收入记录表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Resource
    private RecordMapper recordMapper;


    @Override
    public Result saveRecord(RecordSaveVO recordSaveVO) {
        Record record = BeanUtil.copyProperties(recordSaveVO, Record.class);
        record.setUserId(UserHolderUtils.getUserId());
        recordMapper.insert(record);
        return Result.ok(record.getId());
    }
}
