package ar.uba.fi.ingsoft1.turnos.config.security;

import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resuelve los parámetros anotados con {@link CurrentClientId} / {@link CurrentProfessionalId}
 * al id del usuario autenticado, evitando repetir el bloque de resolución en cada controller.
 */
@Component
public class AuthenticatedUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectProvider<ClientRepository> clientRepository;
    private final ObjectProvider<ProfessionalRepository> professionalRepository;

    public AuthenticatedUserIdArgumentResolver(ObjectProvider<ClientRepository> clientRepository,
                                               ObjectProvider<ProfessionalRepository> professionalRepository) {
        this.clientRepository = clientRepository;
        this.professionalRepository = professionalRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && (parameter.hasParameterAnnotation(CurrentClientId.class)
                || parameter.hasParameterAnnotation(CurrentProfessionalId.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String email = authenticatedUsername();

        if (parameter.hasParameterAnnotation(CurrentClientId.class)) {
            return clientRepository.getObject().findByEmail(email)
                    .orElseThrow(() -> new ForbiddenException("Solo los clientes pueden realizar esta acción"))
                    .getId();
        }

        return professionalRepository.getObject().findByEmail(email)
                .orElseThrow(() -> new ForbiddenException("Solo los profesionales pueden realizar esta acción"))
                .getId();
    }

    private String authenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUserDetails userDetails)) {
            throw new ForbiddenException("Autenticación requerida");
        }
        return userDetails.username();
    }
}
