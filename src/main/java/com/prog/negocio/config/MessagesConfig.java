package com.prog.negocio.config;

import com.prog.negocio.i18n.Messages;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * Configuración para inicializar el sistema de mensajes al arrancar la aplicación
 */
@Configuration
public class MessagesConfig {

    @PostConstruct
    public void initializeMessages() {
        Locale localeColombia = new Locale("es", "CO");

        String[] bundles = {
                "messages"
        };

        Messages.configure(bundles, localeColombia);
    }
}
