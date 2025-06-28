package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaNac,
        @JsonAlias("death_year") Integer fechaDeceso
) {
    public boolean estaVivo() {
        return fechaDeceso == null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)",
                nombre,
                fechaNac != null ? fechaNac : "?",
                fechaDeceso != null ? fechaDeceso : "presente");
    }
}
