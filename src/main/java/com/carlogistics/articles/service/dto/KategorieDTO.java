package com.carlogistics.articles.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Kategorie entity.
 */
public class KategorieDTO implements Serializable {

    private Long id;

    private String beschreibung;

    @Size(min = 3)
    private String titel;

    private Long ueberkategorieId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Long getUeberkategorieId() {
        return ueberkategorieId;
    }

    public void setUeberkategorieId(Long kategorieId) {
        this.ueberkategorieId = kategorieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KategorieDTO kategorieDTO = (KategorieDTO) o;
        if (kategorieDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kategorieDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KategorieDTO{" +
            "id=" + getId() +
            ", beschreibung='" + getBeschreibung() + "'" +
            ", titel='" + getTitel() + "'" +
            ", ueberkategorie=" + getUeberkategorieId() +
            "}";
    }
}
