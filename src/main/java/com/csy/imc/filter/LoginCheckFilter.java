package com.csy.imc.filter;

import com.alibaba.fastjson.JSON;
import com.csy.imc.common.BaseContext;
import com.csy.imc.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("请求{}", request.getRequestURI());

        String requestUris = request.getRequestURI();
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/common/**",
        };

        boolean check = check(uris, requestUris);
        if(check) {
            filterChain.doFilter(request, response);
            return;
        }

        //登录状态放行即可
        if (request.getSession().getAttribute("employee") != null) {
            //相同线程，这里直接将id设置完成即可
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    /**
     * 进行路径匹配比较
     * @param uris
     * @param requestUri
     * @return
     */
    public boolean check(String[] uris, String requestUri) {
        for (String uri : uris) {
            if (PATH_MATCHER.match(uri, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
