package com.cclsr.eat.filter;

import com.alibaba.fastjson.JSON;
import com.cclsr.eat.common.BaseContext;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{}", request.getRequestURI());
        // 获取本次请求的url
        String requestURI = request.getRequestURI();
        // 判断是否需要用户登录
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/upload"
        };
        boolean check = check(urls, requestURI);
        log.info("校验状态:{}", check);
        // 未登录
        if (!check) {
            log.info("session信息：{}", request.getSession().getAttribute("employee"));
            if (request.getSession().getAttribute("employee") == null) {
                // 未登录则返回未登录结果，通过输出流方式向客户端相应数据
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                return;
            }
        }
        Long empid = (Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empid);

        long id = Thread.currentThread().getId();
        log.info("当前线程id：{}", id);

        filterChain.doFilter(request, response);
    }

    /**
     * 检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
