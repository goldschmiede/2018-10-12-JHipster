package com.carlogistics.articles.service.mapper;

import com.carlogistics.articles.domain.*;
import com.carlogistics.articles.service.dto.KategorieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Kategorie and its DTO KategorieDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KategorieMapper extends EntityMapper<KategorieDTO, Kategorie> {

    @Mapping(source = "ueberkategorie.id", target = "ueberkategorieId")
    KategorieDTO toDto(Kategorie kategorie);

    @Mapping(source = "ueberkategorieId", target = "ueberkategorie")
    @Mapping(target = "artikels", ignore = true)
    @Mapping(target = "unterkategoriens", ignore = true)
    Kategorie toEntity(KategorieDTO kategorieDTO);

    default Kategorie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Kategorie kategorie = new Kategorie();
        kategorie.setId(id);
        return kategorie;
    }
}
