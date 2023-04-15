package com.fox.expenseincomemanage.controller;


import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.service.RecordService;
import com.fox.expenseincomemanage.vo.RecordModifyVO;
import com.fox.expenseincomemanage.vo.RecordSaveVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 支出收入记录表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    /**
     * 添加收入或支出记录
     *
     * @param recordSaveVO 金额 + 时间 + 事件 + 类型
     * @return 记录id
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody RecordSaveVO recordSaveVO){
        return recordService.saveRecord(recordSaveVO);
    }

    /**
     * 删除一条记录
     *
     * @param recordId 记录id
     * @return 删除状况
     */
    @DeleteMapping("/remove")
    public Result remove(@RequestParam("recordId")Integer recordId){
        return recordService.removeRecord(recordId);
    }

    /**
     * 修改一条记录
     *
     * @param recordModifyVO 金额 + 时间 + 事件 + 记录id
     * @return 修改状况
     */
    @PutMapping("/modify")
    public Result modify(@Validated @RequestBody RecordModifyVO recordModifyVO){
        return recordService.modifyRecord(recordModifyVO);
    }
}
