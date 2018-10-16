package com.carlogistics.articles.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.carlogistics.articles.domain.enumeration.Verfuegbarkeit;

/**
 * A DTO for the Artikel entity.
 */
public class ArtikelDTO implements Serializable {

    private Long id;

    @NotNull
    private String artikelnummer;

    @Size(min = 20)
    private String beschreibung;

    @Size(min = 3, max = 200)
    private String titel;

    @NotNull
    private Double preis;

    private LocalDate eingestelltAm;

    @NotNull
    private Verfuegbarkeit verfuegbarkeit;

    private Long kategorieId;

    private String kategorieTitel;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtikelnummer() {
        return artikelnummer;
    }

    public void setArtikelnummer(String artikelnummer) {
        this.artikelnummer = artikelnummer;
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

    public Double getPreis() {
        return preis;
    }

    public void setPreis(Double preis) {
        this.preis = preis;
    }

    public LocalDate getEingestelltAm() {
        return eingestelltAm;
    }

    public void setEingestelltAm(LocalDate eingestelltAm) {
        this.eingestelltAm = eingestelltAm;
    }

    public Verfuegbarkeit getVerfuegbarkeit() {
        return verfuegbarkeit;
    }

    public void setVerfuegbarkeit(Verfuegbarkeit verfuegbarkeit) {
        this.verfuegbarkeit = verfuegbarkeit;
    }

    public Long getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(Long kategorieId) {
        this.kategorieId = kategorieId;
    }

    public String getKategorieTitel() {
        return kategorieTitel;
    }

    public void setKategorieTitel(String kategorieTitel) {
        this.kategorieTitel = kategorieTitel;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtikelDTO artikelDTO = (ArtikelDTO) o;
        if (artikelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artikelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtikelDTO{" +
            "id=" + id +
            ", artikelnummer='" + artikelnummer + '\'' +
            ", beschreibung='" + beschreibung + '\'' +
            ", titel='" + titel + '\'' +
            ", preis=" + preis +
            ", eingestelltAm=" + eingestelltAm +
            ", verfuegbarkeit=" + verfuegbarkeit +
            ", kategorieId=" + kategorieId +
            ", kategorieTitel='" + kategorieTitel + '\'' +
            ", tags=" + tags +
            '}';
    }
}
