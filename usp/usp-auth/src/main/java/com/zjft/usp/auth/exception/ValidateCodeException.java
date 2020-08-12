package com.zjft.usp.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author CK
 * @date 2019-08-23 15:56
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
