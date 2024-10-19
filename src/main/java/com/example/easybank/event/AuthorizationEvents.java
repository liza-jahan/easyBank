package com.example.easybank.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvents {

    @EventListener
    public  void  onFailure(AuthorizationDeniedEvent deniedEvent ){
        log.error("Login failure for the user :{} due to{}",deniedEvent.getAuthentication().get().getName(),deniedEvent.getAuthorizationDecision().toString());
    }
}
