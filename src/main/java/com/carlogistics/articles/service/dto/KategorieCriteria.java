package com.carlogistics.articles.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Kategorie entity. This class is used in KategorieResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /kategories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KategorieCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter beschreibung;

    private StringFilter titel;

    private LongFilter ueberkategorieId;

    private LongFilter artikelId;

    private LongFilter unterkategorienId;

    public KategorieCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(StringFilter beschreibung) {
        this.beschreibung = beschreibung;
    }

    public StringFilter getTitel() {
        return titel;
    }

    public void setTitel(StringFilter titel) {
        this.titel = titel;
    }

    public LongFilter getUeberkategorieId() {
        return ueberkategorieId;
    }

    public void setUeberkategorieId(LongFilter ueberkategorieId) {
        this.ueberkategorieId = ueberkategorieId;
    }

    public LongFilter getArtikelId() {
        return artikelId;
    }

    public void setArtikelId(LongFilter artikelId) {
        this.artikelId = artikelId;
    }

    public LongFilter getUnterkategorienId() {
        return unterkategorienId;
    }

    public void setUnterkategorienId(LongFilter unterkategorienId) {
        this.unterkategorienId = unterkategorienId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KategorieCriteria that = (KategorieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(beschreibung, that.beschreibung) &&
            Objects.equals(titel, that.titel) &&
            Objects.equals(ueberkategorieId, that.ueberkategorieId) &&
            Objects.equals(artikelId, that.artikelId) &&
            Objects.equals(unterkategorienId, that.unterkategorienId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        beschreibung,
        titel,
        ueberkategorieId,
        artikelId,
        unterkategorienId
        );
    }

    @Override
    public String toString() {
        return "KategorieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (beschreibung != null ? "beschreibung=" + beschreibung + ", " : "") +
                (titel != null ? "titel=" + titel + ", " : "") +
                (ueberkategorieId != null ? "ueberkategorieId=" + ueberkategorieId + ", " : "") +
                (artikelId != null ? "artikelId=" + artikelId + ", " : "") +
                (unterkategorienId != null ? "unterkategorienId=" + unterkategorienId + ", " : "") +
            "}";
    }

}
