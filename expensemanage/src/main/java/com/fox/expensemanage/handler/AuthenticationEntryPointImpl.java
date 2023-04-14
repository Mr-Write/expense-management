package com.fox.expensemanage.handler;

import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fox.expensemanage.constant.HttpStatus.HTTP_UNAUTHORIZED;

/**
 * 处理认证失败的异常
 *
 * @author 狐狸半面添
 * @create 2023-01-17 17:13
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 需要通过HTTP认证，或认证失败
        Result result =  Result.error(HTTP_UNAUTHORIZED.getCode(), "请先登录再访问");
        WebUtils.renderString(response,result);
    }
}
