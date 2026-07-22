package ar.uba.fi.ingsoft1.turnos.config;

import ar.uba.fi.ingsoft1.turnos.config.security.AuthenticatedUserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedUserIdArgumentResolver authenticatedUserIdArgumentResolver;

    public WebConfig(AuthenticatedUserIdArgumentResolver authenticatedUserIdArgumentResolver) {
        this.authenticatedUserIdArgumentResolver = authenticatedUserIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserIdArgumentResolver);
    }
}
