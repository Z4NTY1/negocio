package com.prog.negocio.exceptions;

public final class BcExceptionFactory {
    private BcExceptionFactory(){
    }
    public static final class Code{
        public static final int FIRST_USER_ERROR_CODE = 1;
        private Code(){
        }
    }
    public static RuntimeException create(int errorCode) {
        return new RuntimeException("Error de negocio, código: " + errorCode);
    }
}
