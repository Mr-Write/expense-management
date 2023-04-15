package com.fox.expenseincomemanage.service.impl;

import com.fox.expenseincomemanage.po.Record;
import com.fox.expenseincomemanage.dao.RecordMapper;
import com.fox.expenseincomemanage.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
