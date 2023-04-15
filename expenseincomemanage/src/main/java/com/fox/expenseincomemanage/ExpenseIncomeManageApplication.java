package com.fox.expenseincomemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class ExpenseIncomeManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseIncomeManageApplication.class, args);
    }
}
