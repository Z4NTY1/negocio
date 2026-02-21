package com.prog.negocio.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Clase utilitaria para obtener mensajes internacionalizados
 * desde archivos .properties
 */
public class Messages {

    private static final Logger log = LoggerFactory.getLogger(Messages.class);

    private static ResourceBundle[] bundles;
    private static Locale currentLocale = new Locale("es", "CO");

    private Messages() {
    }

    /**
     * Configura los archivos de mensajes a cargar
     * @param bundleNames nombres de los archivos (sin extensión .properties)
     * @param locale locale a utilizar
     */
    public static void configure(String[] bundleNames, Locale locale) {
        currentLocale = locale;
        bundles = new ResourceBundle[bundleNames.length];

        for (int i = 0; i < bundleNames.length; i++) {
            try {
                bundles[i] = ResourceBundle.getBundle(bundleNames[i], locale);
            } catch (MissingResourceException e) {
                log.warn("No se pudo cargar el bundle: {}", bundleNames[i]);
            }
        }
    }

    /**
     * Obtiene un mensaje por su clave
     * @param key clave del mensaje (ej: "error.RANGO_CIERRE_EXISTE")
     * @return el mensaje o la clave si no se encuentra
     */
    public static String get(String key) {
        if (bundles == null) {
            return key;
        }

        for (ResourceBundle bundle : bundles) {
            if (bundle != null) {
                try {
                    return bundle.getString(key);
                } catch (MissingResourceException e) {
                    // Continúa buscando en otros bundles
                }
            }
        }
        return key;
    }

    /**
     * Obtiene un mensaje con parámetros formateados
     * @param key clave del mensaje
     * @param params parámetros a sustituir ({0}, {1}, etc.)
     * @return mensaje formateado
     */
    public static String get(String key, Object... params) {
        String pattern = get(key);
        if (params != null && params.length > 0) {
            return MessageFormat.format(pattern, params);
        }
        return pattern;
    }
}
