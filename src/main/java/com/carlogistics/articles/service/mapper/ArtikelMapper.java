package com.carlogistics.articles.service.mapper;

import com.carlogistics.articles.domain.*;
import com.carlogistics.articles.service.dto.ArtikelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Artikel and its DTO ArtikelDTO.
 */
@Mapper(componentModel = "spring", uses = {KategorieMapper.class, TagMapper.class})
public interface ArtikelMapper extends EntityMapper<ArtikelDTO, Artikel> {

    @Mapping(source = "kategorie.id", target = "kategorieId")
    @Mapping(source = "kategorie.titel", target = "kategorieTitel")
    ArtikelDTO toDto(Artikel artikel);

    @Mapping(source = "kategorieId", target = "kategorie")
    Artikel toEntity(ArtikelDTO artikelDTO);

    default Artikel fromId(Long id) {
        if (id == null) {
            return null;
        }
        Artikel artikel = new Artikel();
        artikel.setId(id);
        return artikel;
    }
}
