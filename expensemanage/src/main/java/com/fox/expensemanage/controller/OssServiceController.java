package com.fox.expensemanage.controller;

import com.fox.expensemanage.constant.OssConstants;
import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.util.OssClientUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * oss服务端签名后直传
 *
 * @author 狐狸半面添
 * @create 2023-01-29 1:30
 */
@RestController
@RequestMapping("/oss")
public class OssServiceController {
    @Resource
    private OssClientUtils ossClientUtils;

    /**
     * 获取头像的签名
     *
     * @return 签名信息
     */
    @GetMapping("/headIcon")
    public Result headImage() {
        return ossClientUtils.policy(OssConstants.HEAD_IMAGE_DIR);
    }
}
