package com.carlogistics.articles.service;

import com.carlogistics.articles.domain.Artikel;
import com.carlogistics.articles.repository.ArtikelRepository;
import com.carlogistics.articles.repository.search.ArtikelSearchRepository;
import com.carlogistics.articles.service.dto.ArtikelDTO;
import com.carlogistics.articles.service.mapper.ArtikelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Artikel.
 */
@Service
@Transactional
public class ArtikelService {

    private final Logger log = LoggerFactory.getLogger(ArtikelService.class);

    private final ArtikelRepository artikelRepository;

    private final ArtikelMapper artikelMapper;

    private final ArtikelSearchRepository artikelSearchRepository;

    public ArtikelService(ArtikelRepository artikelRepository, ArtikelMapper artikelMapper, ArtikelSearchRepository artikelSearchRepository) {
        this.artikelRepository = artikelRepository;
        this.artikelMapper = artikelMapper;
        this.artikelSearchRepository = artikelSearchRepository;
    }

    /**
     * Save a artikel.
     *
     * @param artikelDTO the entity to save
     * @return the persisted entity
     */
    public ArtikelDTO save(ArtikelDTO artikelDTO) {
        log.debug("Request to save Artikel : {}", artikelDTO);
        Artikel artikel = artikelMapper.toEntity(artikelDTO);
        artikel = artikelRepository.save(artikel);
        ArtikelDTO result = artikelMapper.toDto(artikel);
        artikelSearchRepository.save(artikel);
        return result;
    }

    /**
     * Get all the artikels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArtikelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Artikels");
        return artikelRepository.findAll(pageable)
            .map(artikelMapper::toDto);
    }

    /**
     * Get all the Artikel with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ArtikelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return artikelRepository.findAllWithEagerRelationships(pageable).map(artikelMapper::toDto);
    }
    

    /**
     * Get one artikel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ArtikelDTO> findOne(Long id) {
        log.debug("Request to get Artikel : {}", id);
        return artikelRepository.findOneWithEagerRelationships(id)
            .map(artikelMapper::toDto);
    }

    /**
     * Delete the artikel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Artikel : {}", id);
        artikelRepository.deleteById(id);
        artikelSearchRepository.deleteById(id);
    }

    /**
     * Search for the artikel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArtikelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Artikels for query {}", query);
        return artikelSearchRepository.search(queryStringQuery(query), pageable)
            .map(artikelMapper::toDto);
    }
}
