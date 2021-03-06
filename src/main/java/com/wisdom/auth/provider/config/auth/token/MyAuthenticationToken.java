package com.wisdom.auth.provider.config.auth.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义AbstractAuthenticationToken，
 * 新增属性 type: 登陆类型、mobile：移动端设备id
 */
public class MyAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 110L;
    private final Object principal;
    private Object credentials;
    private String type;
    private String mobile;
    private String deviceType;

    public MyAuthenticationToken(Object principal, Object credentials) {
        super(principal,credentials);
        this.principal = principal;
        this.credentials = credentials;

        this.setAuthenticated(false);
    }
    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link
     * #isAuthenticated()} will return <code>false</code>.
     *
     */
    public MyAuthenticationToken(Object principal, Object credentials,String type, String mobile,String deviceType) {
        super(principal,credentials);
        this.principal = principal;
        this.credentials = credentials;
        this.type = type;
        this.mobile = mobile;
        this.deviceType = deviceType;
        this.setAuthenticated(false);
    }

    public MyAuthenticationToken(Object principal, Object credentials,String type, String mobile) {
        super(principal,credentials);
        this.principal = principal;
        this.credentials = credentials;
        this.type = type;
        this.mobile = mobile;
        this.setAuthenticated(false);
    }
    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
     * implementations that are satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * token token.
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public MyAuthenticationToken(Object principal, Object credentials,String type, String mobile, Collection<? extends GrantedAuthority> authorities) {
        super(principal,credentials,authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.type = type;
        this.mobile = mobile;

//        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getType() {
        return this.type;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if(isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
