package com.wisdom.auth.provider.config.auth.provider;

import com.alibaba.fastjson.JSONObject;
import com.wisdom.auth.provider.config.auth.token.MyAuthenticationToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by fp295 on 2018/6/16.
 * 自定义 AuthenticationProvider， 以使用自定义的 MyAuthenticationToken
 */
public abstract class MyAbstractUserDetailsAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected final Log logger = LogFactory.getLog(this.getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public MyAbstractUserDetailsAuthenticationProvider() {
    }

    protected abstract void additionalAuthenticationChecks(UserDetails var1, MyAuthenticationToken var2) throws AuthenticationException;

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        this.doAfterPropertiesSet();
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 此处修改断言自定义的 MyAuthenticationToken
//        Assert.isInstanceOf(MyAuthenticationToken.class, authentication, this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.onlySupports", "Only MyAuthenticationToken is supported"));
        String username = authentication.getPrincipal() == null?"NONE_PROVIDED":authentication.getName();
        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);
        MyAuthenticationToken myAuthenticationToken=null;
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            HashMap<String,String> map=JSONObject.parseObject(JSONObject.toJSONString(authentication.getDetails()), HashMap.class);
            if("wx".equals(map.get("deviceType"))){
                myAuthenticationToken = new MyAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(),"wx","");
            }else{
                myAuthenticationToken = new MyAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(),"user","");
            }

        }
        else{
            myAuthenticationToken = (MyAuthenticationToken)authentication;
        }
        if(user == null) {
            cacheWasUsed = false;

            try {
                user = this.retrieveUser(username, myAuthenticationToken);
            } catch (UsernameNotFoundException var6) {
                this.logger.debug("User \'" + username + "\' not found");
                if(this.hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                }

                throw var6;
            }

            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            this.preAuthenticationChecks.check(user);
            this.additionalAuthenticationChecks(user, myAuthenticationToken);
        } catch (AuthenticationException var7) {
            if(!cacheWasUsed) {
                throw var7;
            }

            cacheWasUsed = false;
            user = this.retrieveUser(username, myAuthenticationToken);
            this.preAuthenticationChecks.check(user);
            this.additionalAuthenticationChecks(user, myAuthenticationToken);
        }

        this.postAuthenticationChecks.check(user);
        if(!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;
        if(this.forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }

        return this.createSuccessAuthentication(principalToReturn, myAuthenticationToken, user);
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        MyAuthenticationToken result = new MyAuthenticationToken(principal, authentication.getCredentials(),((MyAuthenticationToken) authentication).getType(),((MyAuthenticationToken) authentication).getMobile(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected void doAfterPropertiesSet() throws Exception {
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public boolean isForcePrincipalAsString() {
        return this.forcePrincipalAsString;
    }

    public boolean isHideUserNotFoundExceptions() {
        return this.hideUserNotFoundExceptions;
    }

    protected abstract UserDetails retrieveUser(String var1, MyAuthenticationToken var2) throws AuthenticationException;

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public boolean supports(Class<?> authentication) {
        return MyAuthenticationToken.class.isAssignableFrom(authentication)||UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return this.preAuthenticationChecks;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return this.postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        public void check(UserDetails user) {
            if(!user.isCredentialsNonExpired()) {
                MyAbstractUserDetailsAuthenticationProvider.this.logger.debug("User account credentials have expired");
                throw new CredentialsExpiredException(MyAbstractUserDetailsAuthenticationProvider.this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
            }
        }
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        private DefaultPreAuthenticationChecks() {
        }

        public void check(UserDetails user) {
            if(!user.isAccountNonLocked()) {
                MyAbstractUserDetailsAuthenticationProvider.this.logger.debug("User account is locked");
                throw new LockedException(MyAbstractUserDetailsAuthenticationProvider.this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if(!user.isEnabled()) {
                MyAbstractUserDetailsAuthenticationProvider.this.logger.debug("User account is disabled");
                throw new DisabledException(MyAbstractUserDetailsAuthenticationProvider.this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if(!user.isAccountNonExpired()) {
                MyAbstractUserDetailsAuthenticationProvider.this.logger.debug("User account is expired");
                throw new AccountExpiredException(MyAbstractUserDetailsAuthenticationProvider.this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
