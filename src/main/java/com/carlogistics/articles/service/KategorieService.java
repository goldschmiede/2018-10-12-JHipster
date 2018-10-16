package com.carlogistics.articles.service;

import com.carlogistics.articles.domain.Kategorie;
import com.carlogistics.articles.repository.KategorieRepository;
import com.carlogistics.articles.repository.search.KategorieSearchRepository;
import com.carlogistics.articles.service.dto.KategorieDTO;
import com.carlogistics.articles.service.mapper.KategorieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Kategorie.
 */
@Service
@Transactional
public class KategorieService {

    private final Logger log = LoggerFactory.getLogger(KategorieService.class);

    private final KategorieRepository kategorieRepository;

    private final KategorieMapper kategorieMapper;

    private final KategorieSearchRepository kategorieSearchRepository;

    public KategorieService(KategorieRepository kategorieRepository, KategorieMapper kategorieMapper, KategorieSearchRepository kategorieSearchRepository) {
        this.kategorieRepository = kategorieRepository;
        this.kategorieMapper = kategorieMapper;
        this.kategorieSearchRepository = kategorieSearchRepository;
    }

    /**
     * Save a kategorie.
     *
     * @param kategorieDTO the entity to save
     * @return the persisted entity
     */
    public KategorieDTO save(KategorieDTO kategorieDTO) {
        log.debug("Request to save Kategorie : {}", kategorieDTO);
        Kategorie kategorie = kategorieMapper.toEntity(kategorieDTO);
        kategorie = kategorieRepository.save(kategorie);
        KategorieDTO result = kategorieMapper.toDto(kategorie);
        kategorieSearchRepository.save(kategorie);
        return result;
    }

    /**
     * Get all the kategories.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<KategorieDTO> findAll() {
        log.debug("Request to get all Kategories");
        return kategorieRepository.findAll().stream()
            .map(kategorieMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one kategorie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<KategorieDTO> findOne(Long id) {
        log.debug("Request to get Kategorie : {}", id);
        return kategorieRepository.findById(id)
            .map(kategorieMapper::toDto);
    }

    /**
     * Delete the kategorie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Kategorie : {}", id);
        kategorieRepository.deleteById(id);
        kategorieSearchRepository.deleteById(id);
    }

    /**
     * Search for the kategorie corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<KategorieDTO> search(String query) {
        log.debug("Request to search Kategories for query {}", query);
        return StreamSupport
            .stream(kategorieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(kategorieMapper::toDto)
            .collect(Collectors.toList());
    }
}
