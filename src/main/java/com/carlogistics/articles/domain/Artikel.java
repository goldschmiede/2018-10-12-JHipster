package com.carlogistics.articles.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.carlogistics.articles.domain.enumeration.Verfuegbarkeit;

/**
 * A Artikel.
 */
@Entity
@Table(name = "artikel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artikel")
public class Artikel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "artikelnummer", nullable = false)
    private String artikelnummer;

    @Size(min = 20)
    @Column(name = "beschreibung")
    private String beschreibung;

    @Size(min = 3, max = 200)
    @Column(name = "titel", length = 200)
    private String titel;

    @NotNull
    @Column(name = "preis", nullable = false)
    private Double preis;

    @Column(name = "eingestellt_am")
    private LocalDate eingestelltAm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "verfuegbarkeit", nullable = false)
    private Verfuegbarkeit verfuegbarkeit;

    @ManyToOne
    @JsonIgnoreProperties("artikels")
    private Kategorie kategorie;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "artikel_tags",
               joinColumns = @JoinColumn(name = "artikels_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtikelnummer() {
        return artikelnummer;
    }

    public Artikel artikelnummer(String artikelnummer) {
        this.artikelnummer = artikelnummer;
        return this;
    }

    public void setArtikelnummer(String artikelnummer) {
        this.artikelnummer = artikelnummer;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Artikel beschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
        return this;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getTitel() {
        return titel;
    }

    public Artikel titel(String titel) {
        this.titel = titel;
        return this;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Double getPreis() {
        return preis;
    }

    public Artikel preis(Double preis) {
        this.preis = preis;
        return this;
    }

    public void setPreis(Double preis) {
        this.preis = preis;
    }

    public LocalDate getEingestelltAm() {
        return eingestelltAm;
    }

    public Artikel eingestelltAm(LocalDate eingestelltAm) {
        this.eingestelltAm = eingestelltAm;
        return this;
    }

    public void setEingestelltAm(LocalDate eingestelltAm) {
        this.eingestelltAm = eingestelltAm;
    }

    public Verfuegbarkeit getVerfuegbarkeit() {
        return verfuegbarkeit;
    }

    public Artikel verfuegbarkeit(Verfuegbarkeit verfuegbarkeit) {
        this.verfuegbarkeit = verfuegbarkeit;
        return this;
    }

    public void setVerfuegbarkeit(Verfuegbarkeit verfuegbarkeit) {
        this.verfuegbarkeit = verfuegbarkeit;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public Artikel kategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
        return this;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Artikel tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Artikel addTags(Tag tag) {
        this.tags.add(tag);
        tag.getArtikels().add(this);
        return this;
    }

    public Artikel removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getArtikels().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
        Artikel artikel = (Artikel) o;
        if (artikel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artikel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Artikel{" +
            "id=" + getId() +
            ", artikelnummer='" + getArtikelnummer() + "'" +
            ", beschreibung='" + getBeschreibung() + "'" +
            ", titel='" + getTitel() + "'" +
            ", preis=" + getPreis() +
            ", eingestelltAm='" + getEingestelltAm() + "'" +
            ", verfuegbarkeit='" + getVerfuegbarkeit() + "'" +
            "}";
    }
}
