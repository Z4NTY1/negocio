package com.prog.negocio.exceptions;

import com.prog.negocio.i18n.Messages;

/**
 * Excepción de negocio que utiliza el sistema de mensajes
 */
public class BusinessException extends RuntimeException {

    private final int errorCode;
    private final String messageKey;
    private final Object[] params;

    public BusinessException(int errorCode, String messageKey) {
        this(errorCode, messageKey, (Object[]) null);
    }

    public BusinessException(int errorCode, String messageKey, Object... params) {
        super(Messages.get(messageKey, params));
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.params = params;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String getMessage() {
        return Messages.get(messageKey, params);
    }
}
