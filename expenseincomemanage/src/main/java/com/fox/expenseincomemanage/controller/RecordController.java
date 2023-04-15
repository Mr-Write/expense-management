package com.fox.expenseincomemanage.controller;


import com.fox.expenseincomemanage.service.RecordService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
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
}
