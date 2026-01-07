package com.amq.amq;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;

import java.util.Set;

public class SimpleSecurityManager implements ActiveMQSecurityManager {

    private final String user;
    private final String password;

    public SimpleSecurityManager(String user, String password) {
        this.password = password;
        this.user = user;
    }


    @Override
    public boolean validateUser(String username, String pwd) {
        return user.equals(username) && password.equals(pwd);
    }

    @Override
    public boolean validateUserAndRole(String user, String password, Set<Role> set, CheckType checkType) {
        return validateUser(user, password);
    }
}
