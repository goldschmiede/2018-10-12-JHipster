package com.carlogistics.articles.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.carlogistics.articles.domain.Kategorie;
import com.carlogistics.articles.domain.*; // for static metamodels
import com.carlogistics.articles.repository.KategorieRepository;
import com.carlogistics.articles.repository.search.KategorieSearchRepository;
import com.carlogistics.articles.service.dto.KategorieCriteria;
import com.carlogistics.articles.service.dto.KategorieDTO;
import com.carlogistics.articles.service.mapper.KategorieMapper;

/**
 * Service for executing complex queries for Kategorie entities in the database.
 * The main input is a {@link KategorieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KategorieDTO} or a {@link Page} of {@link KategorieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KategorieQueryService extends QueryService<Kategorie> {

    private final Logger log = LoggerFactory.getLogger(KategorieQueryService.class);

    private final KategorieRepository kategorieRepository;

    private final KategorieMapper kategorieMapper;

    private final KategorieSearchRepository kategorieSearchRepository;

    public KategorieQueryService(KategorieRepository kategorieRepository, KategorieMapper kategorieMapper, KategorieSearchRepository kategorieSearchRepository) {
        this.kategorieRepository = kategorieRepository;
        this.kategorieMapper = kategorieMapper;
        this.kategorieSearchRepository = kategorieSearchRepository;
    }

    /**
     * Return a {@link List} of {@link KategorieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KategorieDTO> findByCriteria(KategorieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Kategorie> specification = createSpecification(criteria);
        return kategorieMapper.toDto(kategorieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KategorieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KategorieDTO> findByCriteria(KategorieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Kategorie> specification = createSpecification(criteria);
        return kategorieRepository.findAll(specification, page)
            .map(kategorieMapper::toDto);
    }

    /**
     * Function to convert KategorieCriteria to a {@link Specification}
     */
    private Specification<Kategorie> createSpecification(KategorieCriteria criteria) {
        Specification<Kategorie> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Kategorie_.id));
            }
            if (criteria.getBeschreibung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBeschreibung(), Kategorie_.beschreibung));
            }
            if (criteria.getTitel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitel(), Kategorie_.titel));
            }
            if (criteria.getUeberkategorieId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUeberkategorieId(), Kategorie_.ueberkategorie, Kategorie_.id));
            }
            if (criteria.getArtikelId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getArtikelId(), Kategorie_.artikels, Artikel_.id));
            }
            if (criteria.getUnterkategorienId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUnterkategorienId(), Kategorie_.unterkategoriens, Kategorie_.id));
            }
        }
        return specification;
    }
}
