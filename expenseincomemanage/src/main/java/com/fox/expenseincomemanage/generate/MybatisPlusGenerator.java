package com.fox.expenseincomemanage.generate;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.Collections;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 15:36
 */
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.130.97.145:3306/expense_income_manage?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "root")
                .globalConfig(builder -> {
                    builder.author("狐狸半面添") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\SoftwareEngineering\\java\\exercise\\expense-management\\expenseincomemanage\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.fox") // 设置父包名
                            .moduleName("expenseincomemanage") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\SoftwareEngineering\\java\\exercise\\expense-management\\expenseincomemanage\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("record"); // 设置需要生成的表名

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
