package com.leo.reggie.filtre;


import com.alibaba.fastjson.JSON;
import com.leo.reggie.common.BaseContext;
import com.leo.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginCheckFiltre", urlPatterns = "/*")
public class LoginCheckFiltre implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        log.info("filtre request ============ {}",httpServletRequest.getRequestURI());



        String requestURL = httpServletRequest.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "user/sendMsg",
                "user/login"


        };

        boolean check = check(urls,requestURL);
        if(check){
            log.info("passed filtre directly");
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        if(httpServletRequest.getSession().getAttribute("employee") != null){

            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            log.info("the current user is ======= {}",empId);
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(httpServletRequest,httpServletResponse);


            return;
        }

        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


        //log.info("filtre---:{}",httpServletRequest.getRequestURI());


    }

    public boolean check(String[] urls, String requestURL){
        for(String url: urls){
           boolean match = PATH_MATCHER.match(url, requestURL);
           if(match){
               return true;
           }
        }
        return false;

    }
}
