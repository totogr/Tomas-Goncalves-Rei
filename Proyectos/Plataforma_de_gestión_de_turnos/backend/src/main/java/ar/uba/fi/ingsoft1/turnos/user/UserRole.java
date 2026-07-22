package ar.uba.fi.ingsoft1.turnos.user;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum UserRole {
    @FieldNameConstants.Include
    CLIENT,
    @FieldNameConstants.Include
    PROFESSIONAL;

    public String toStringWithPrefix() {
        return "ROLE_" + this;
    }
}
