package com.fox.expenseincomemanage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fox.expenseincomemanage.constant.HttpStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回给前端的统一工具类
 *
 * @author 狐狸半面添
 * @create 2023-01-16 19:19
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;

    }

    public static Result ok() {
        return new Result(HttpStatus.HTTP_OK.getCode(), HttpStatus.HTTP_OK.getValue());
    }


    public static Result ok(Object data) {
        return new Result(HttpStatus.HTTP_OK.getCode(), HttpStatus.HTTP_OK.getValue(), data);
    }

    public static Result ok(String msg, Object data) {
        return new Result(HttpStatus.HTTP_OK.getCode(), msg, data);
    }

    public static Result error() {
        return new Result(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), HttpStatus.HTTP_INTERNAL_ERROR.getValue());
    }

    public static Result error(String msg) {
        return new Result(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), msg);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }
    public static Result error(String msg, Object data) {
        return new Result(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), msg, data);
    }

    public static Result error(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
