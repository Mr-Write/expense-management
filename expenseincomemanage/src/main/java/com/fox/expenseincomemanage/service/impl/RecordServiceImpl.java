package com.fox.expenseincomemanage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fox.expenseincomemanage.constant.HttpStatus;
import com.fox.expenseincomemanage.dao.RecordMapper;
import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.po.Record;
import com.fox.expenseincomemanage.service.RecordService;
import com.fox.expenseincomemanage.util.UserHolderUtils;
import com.fox.expenseincomemanage.vo.RecordModifyVO;
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

    @Override
    public Result removeRecord(Integer recordId) {
        // 1.校验格式
        if (recordId < 1000) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "记录id格式错误");
        }

        // 2.删除记录
        int deleteCount = recordMapper.delete(new LambdaQueryWrapper<Record>()
                .eq(Record::getId, recordId)
                .eq(Record::getUserId, UserHolderUtils.getUserId()));
        if (deleteCount == 0) {
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "记录不存在或权限不足");
        }

        // 3.返回成功
        return Result.ok();
    }

    @Override
    public Result modifyRecord(RecordModifyVO recordModifyVO) {
        int updateCount = recordMapper.update(null, new LambdaUpdateWrapper<Record>()
                .eq(Record::getId, recordModifyVO.getId())
                .eq(Record::getUserId, UserHolderUtils.getUserId())
                .set(recordModifyVO.getTime() != null, Record::getTime, recordModifyVO.getTime())
                .set(recordModifyVO.getMoney() != null, Record::getMoney, recordModifyVO.getMoney())
                .set(recordModifyVO.getEvent() != null, Record::getEvent, recordModifyVO.getEvent()));

        if (updateCount==0){
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "记录不存在或权限不足");
        }

        return Result.ok();
    }
}
