package com.system.springboot.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.system.springboot.common.Constants;
import com.system.springboot.controller.dto.UserDTO;
import com.system.springboot.controller.dto.UserDTO2;
import com.system.springboot.exception.ServiceException;
import com.system.springboot.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);

        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401,"token验证失败");
        }


        //从redis中获取用户信息
        String redisKey = "login:" + userId;
        UserDTO2 userDTO = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(userDTO)){
            throw new RuntimeException("用户未登录");
        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userDTO.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        }
        catch (TokenExpiredException e){  //过期异常
            throw new ServiceException(Constants.CODE_401,"token过期");
        }
        catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败");
//            throw new RuntimeException("401");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO,null,userDTO.getAuthorities());
        //设置权限，存储到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
