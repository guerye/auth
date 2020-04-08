package com.wisdom.auth.provider.config.auth.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtils jwtTokenUtils;
    @Resource
    private UserRepository userRepository;

    private String tokenHeader = "Authorization";

    private String tokenHead = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        //先从url中取token
        String authToken = request.getParameter("token");
        String authHeader = request.getHeader(this.tokenHeader);
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenHead)) {
            //如果header中存在token，则覆盖掉url中的token
            authToken = authHeader.substring(tokenHead.length()); // "Bearer "之后的内容
        }


        if (StringUtils.isNotBlank(authToken)) {
            authToken = authToken.substring("Bearer".length()).trim();
            int commaIndex = authToken.indexOf(44);
            if (commaIndex > 0) {
                authToken = authToken.substring(0, commaIndex);
            }
            String username = jwtTokenUtils.getUsernameFromToken(authToken);

            logger.info("checking authentication {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //从已有的user缓存中取了出user信息
                User user = userRepository.findByUsername(username);

                //检查token是否有效
                if (jwtTokenUtils.validateToken(authToken, user)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //设置用户登录状态
                    logger.info("authenticated user {}, setting security context",username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}