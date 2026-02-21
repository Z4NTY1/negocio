package com.prog.negocio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Escribe en log al arrancar la aplicación para que quede registrado
 * en el archivo de logs (incl. ruta y puerto).
 */
@Component
public class LoggingStartupListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingStartupListener.class);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String logPath = System.getProperty("LOG_PATH", "logs");
        log.info("Aplicación lista. Servidor en puerto {}. Logs en: {}/negocio.log (errores: {}/negocio-error.log)",
                port, logPath, logPath);
    }
}
