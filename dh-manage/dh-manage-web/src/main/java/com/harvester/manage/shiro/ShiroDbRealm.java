package com.harvester.manage.shiro;

import com.harvester.manage.pojo.UserInfo;
import com.harvester.manage.service.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cui on 2017/5/17.
 */
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;

    public ShiroDbRealm() {
        setAuthenticationTokenClass(UsernamePasswordToken.class);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken authcToken = (UsernamePasswordToken) authenticationToken;
        String accountName = authcToken.getUsername();
        String password = new String(authcToken.getPassword());

        UserInfo loginUser = new UserInfo();
        loginUser.setUserAccount(accountName);
        loginUser.setUserPsw(password);
        UserInfo user=userInfoService.checkUser(loginUser);
        if (null == user) {
            throw new AuthenticationException("用户名或者密码错误");
        } else {
            return new SimpleAuthenticationInfo(user, password, getName());
        }
    }
}
