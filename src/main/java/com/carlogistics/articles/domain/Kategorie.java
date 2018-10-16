package com.carlogistics.articles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Kategorie.
 */
@Entity
@Table(name = "kategorie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "kategorie")
public class Kategorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Size(min = 3)
    @Column(name = "titel")
    private String titel;

    @ManyToOne
    @JsonIgnoreProperties("unterkategoriens")
    private Kategorie ueberkategorie;

    @OneToMany(mappedBy = "kategorie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artikel> artikels = new HashSet<>();

    @OneToMany(mappedBy = "ueberkategorie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Kategorie> unterkategoriens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Kategorie beschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
        return this;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getTitel() {
        return titel;
    }

    public Kategorie titel(String titel) {
        this.titel = titel;
        return this;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Kategorie getUeberkategorie() {
        return ueberkategorie;
    }

    public Kategorie ueberkategorie(Kategorie kategorie) {
        this.ueberkategorie = kategorie;
        return this;
    }

    public void setUeberkategorie(Kategorie kategorie) {
        this.ueberkategorie = kategorie;
    }

    public Set<Artikel> getArtikels() {
        return artikels;
    }

    public Kategorie artikels(Set<Artikel> artikels) {
        this.artikels = artikels;
        return this;
    }

    public Kategorie addArtikel(Artikel artikel) {
        this.artikels.add(artikel);
        artikel.setKategorie(this);
        return this;
    }

    public Kategorie removeArtikel(Artikel artikel) {
        this.artikels.remove(artikel);
        artikel.setKategorie(null);
        return this;
    }

    public void setArtikels(Set<Artikel> artikels) {
        this.artikels = artikels;
    }

    public Set<Kategorie> getUnterkategoriens() {
        return unterkategoriens;
    }

    public Kategorie unterkategoriens(Set<Kategorie> kategories) {
        this.unterkategoriens = kategories;
        return this;
    }

    public Kategorie addUnterkategorien(Kategorie kategorie) {
        this.unterkategoriens.add(kategorie);
        kategorie.setUeberkategorie(this);
        return this;
    }

    public Kategorie removeUnterkategorien(Kategorie kategorie) {
        this.unterkategoriens.remove(kategorie);
        kategorie.setUeberkategorie(null);
        return this;
    }

    public void setUnterkategoriens(Set<Kategorie> kategories) {
        this.unterkategoriens = kategories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Kategorie kategorie = (Kategorie) o;
        if (kategorie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kategorie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Kategorie{" +
            "id=" + getId() +
            ", beschreibung='" + getBeschreibung() + "'" +
            ", titel='" + getTitel() + "'" +
            "}";
    }
}
