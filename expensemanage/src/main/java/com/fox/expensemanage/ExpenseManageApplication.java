package com.fox.expensemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动类
 * EnableAspectJAutoProxy 暴露代理对象
 *
 * @author 狐狸半面添
 * @create 2023-04-14 23:25
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
//@ComponentScan(basePackages = {"com.xxx.xxx.dao"})
public class ExpenseManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseManageApplication.class, args);
    }
}
