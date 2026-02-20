package com.prog.negocio.business;

import ch.qos.logback.core.spi.ErrorCodes;
import com.prog.negocio.exceptions.BcExceptionFactory;

public class ErrorCodesNegocio extends ErrorCodes {
    protected ErrorCodesNegocio() {

    }
    public static final int RANGO_CIERRE_EXISTE = BcExceptionFactory.Code.FIRST_USER_ERROR_CODE + 1;
}
