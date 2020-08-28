package com.wisdom.auth.provider.config.auth.filter;

import com.wisdom.auth.provider.config.auth.token.MyAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登陆filter，新增登陆方式：验证码、二维码扫码、账号密码；
 * 验证码登陆：
 *       post: /login?type=phone&phone=13000000000&verifyCode=1000
 * 二维码登陆：
 *       post: /login?type=qr&qrCode=token
 * 账号密码登陆：
 *       post: /login?username=username&password=password
 * 此filter 为生成自定义的 MyAuthenticationToken
 */
public class MyLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_RESTFUL_TYPE_PHONE = "phone";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_WX = "wx";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_QR = "qr";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_DEFAULT = "user";

    // 登陆类型：user:用户密码登陆；phone:手机验证码登陆；qr:二维码扫码登陆
    private static final String SPRING_SECURITY_RESTFUL_TYPE_KEY = "type";
    // 登陆终端：1：移动端登陆，包括微信公众号、小程序等；0：PC后台登陆
    private static final String SPRING_SECURITY_RESTFUL_MOBILE_KEY = "mobile";

    private static final String SPRING_SECURITY_RESTFUL_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_RESTFUL_PASSWORD_KEY = "password";
    private static final String SPRING_SECURITY_RESTFUL_PHONE_KEY = "phone";
    private static final String SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY = "verifyCode";
    private static final String SPRING_SECURITY_RESTFUL_QR_CODE_KEY = "qrCode";
    
//    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/auth/oauth/token";
    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/login";
    private boolean postOnly = true;

    public MyLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String type = obtainParameter(request, SPRING_SECURITY_RESTFUL_TYPE_KEY);
        String mobile = obtainParameter(request, SPRING_SECURITY_RESTFUL_MOBILE_KEY);
        MyAuthenticationToken authRequest;
        String principal;
        String credentials;

        // 手机验证码登陆
        if(SPRING_SECURITY_RESTFUL_TYPE_PHONE.equals(type)){
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_PHONE_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY);
        }
        // 二维码扫码登陆
        else if(SPRING_SECURITY_RESTFUL_TYPE_QR.equals(type)){
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_QR_CODE_KEY);
            credentials = null;
        }
        // 账号密码登陆
        else {
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_USERNAME_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_PASSWORD_KEY);
            if(type == null)
                type = SPRING_SECURITY_RESTFUL_TYPE_DEFAULT;
        }
        if (principal == null) {
            principal = "";
        }
        if (credentials == null) {
            credentials = "";
        }
        principal = principal.trim();
        authRequest = new MyAuthenticationToken(
                principal, credentials, type, mobile);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter);
    }
}
