package ar.uba.fi.ingsoft1.turnos.config.security;

import ar.uba.fi.ingsoft1.turnos.user.UserRole;

public record JwtUserDetails(
                String username,
                UserRole role) {
}