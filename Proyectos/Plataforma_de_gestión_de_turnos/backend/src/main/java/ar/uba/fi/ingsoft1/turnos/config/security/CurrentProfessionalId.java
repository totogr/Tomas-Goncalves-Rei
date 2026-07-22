package ar.uba.fi.ingsoft1.turnos.config.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inyecta el id del profesional autenticado (resuelto desde el email del JWT).
 * Lanza ForbiddenException si el usuario autenticado no es un profesional.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentProfessionalId {
}
