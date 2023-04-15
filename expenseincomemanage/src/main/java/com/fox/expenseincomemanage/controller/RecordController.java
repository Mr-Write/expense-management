package com.fox.expenseincomemanage.controller;


import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.service.RecordService;
import com.fox.expenseincomemanage.vo.RecordSaveVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}
