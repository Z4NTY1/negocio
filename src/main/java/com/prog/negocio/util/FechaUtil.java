package com.prog.negocio.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utilidad para rangos de fechas usados en consultas y reportes.
 * Evita números mágicos y centraliza la lógica de "inicio/fin de día" y quincenas.
 */
public final class FechaUtil {

    private static final int HORA_FIN_DIA = 23;
    private static final int MINUTO_FIN_DIA = 59;
    private static final int SEGUNDO_FIN_DIA = 59;
    /** Día del mes en que termina la primera quincena (1-15). */
    public static final int QUINCENA_PRIMER_CORTE_DIA = 15;

    private FechaUtil() {
    }

    /**
     * Inicio del día (00:00:00) para una fecha.
     */
    public static LocalDateTime inicioDelDia(LocalDate fecha) {
        return fecha.atStartOfDay();
    }

    /**
     * Fin del día (23:59:59) para una fecha. Inclusivo en consultas por rango.
     */
    public static LocalDateTime finDelDia(LocalDate fecha) {
        return fecha.atTime(HORA_FIN_DIA, MINUTO_FIN_DIA, SEGUNDO_FIN_DIA);
    }

    /**
     * Devuelve el rango de la quincena que contiene la fecha dada.
     * Quincena 1: días 1-15; Quincena 2: días 16-fin de mes.
     *
     * @param fechaReferencia fecha dentro de la quincena
     * @return [inicio, fin] de la quincena (LocalDate)
     */
    public static RangoQuincena rangoQuincena(LocalDate fechaReferencia) {
        LocalDate inicio;
        LocalDate fin;
        if (fechaReferencia.getDayOfMonth() <= QUINCENA_PRIMER_CORTE_DIA) {
            inicio = fechaReferencia.withDayOfMonth(1);
            fin = fechaReferencia.withDayOfMonth(QUINCENA_PRIMER_CORTE_DIA);
        } else {
            inicio = fechaReferencia.withDayOfMonth(QUINCENA_PRIMER_CORTE_DIA + 1);
            fin = fechaReferencia.withDayOfMonth(fechaReferencia.lengthOfMonth());
        }
        return new RangoQuincena(inicio, fin);
    }

    public record RangoQuincena(LocalDate inicio, LocalDate fin) {}
}
