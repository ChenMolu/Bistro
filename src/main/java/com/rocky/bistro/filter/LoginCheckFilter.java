package com.rocky.bistro.filter;

import com.alibaba.fastjson.JSON;
import com.rocky.bistro.common.R;
import com.sun.prism.impl.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{

    //路径匹配器，支持通配符
    //test
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    //放行资源列表
    String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/user/**"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("获取到的URI路径：{}",requestURI);
        //判断请求是否需要处理
        Boolean check = check(requestURI);
        //如果为true，资源放行
        if (check){
            log.info("放行的资源为：{}",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //结果为false时，验证用户是否已登陆，判断用户session中的id值
        if (request.getSession().getAttribute("employee") != null){
            log.info("已登录，id号为{}", request.getSession().getAttribute("employee"));
            //存储用户id到threadLocal，本地线程可获取
//            Long employeeId = (Long)request.getSession().getAttribute("employee");
//            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request,response);
            return;
        }
        //验证前端用户
        if (request.getSession().getAttribute("user") != null){
            log.info("已登录，id号为{}", request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
//            BaseContext.setCurrentId(userId);
//            filterChain.doFilter(request,response);
            return;
        }

        //登录失败，通过输出流返回json数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }
    public Boolean check(String RequestURI){
        for (String url : urls){
            //配对成功，返回true，
            if (PATH_MATCHER.match(url,RequestURI)){
                return true;
            }
        }
        //其他资源路径，返回false
        return false;
    }

}
