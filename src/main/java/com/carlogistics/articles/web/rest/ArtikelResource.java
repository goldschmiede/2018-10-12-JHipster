package com.carlogistics.articles.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.carlogistics.articles.service.ArtikelService;
import com.carlogistics.articles.web.rest.errors.BadRequestAlertException;
import com.carlogistics.articles.web.rest.util.HeaderUtil;
import com.carlogistics.articles.web.rest.util.PaginationUtil;
import com.carlogistics.articles.service.dto.ArtikelDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Artikel.
 */
@RestController
@RequestMapping("/api")
public class ArtikelResource {

    private final Logger log = LoggerFactory.getLogger(ArtikelResource.class);

    private static final String ENTITY_NAME = "artikel";

    private final ArtikelService artikelService;

    public ArtikelResource(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    /**
     * POST  /artikels : Create a new artikel.
     *
     * @param artikelDTO the artikelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artikelDTO, or with status 400 (Bad Request) if the artikel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/artikels")
    @Timed
    public ResponseEntity<ArtikelDTO> createArtikel(@Valid @RequestBody ArtikelDTO artikelDTO) throws URISyntaxException {
        log.debug("REST request to save Artikel : {}", artikelDTO);
        if (artikelDTO.getId() != null) {
            throw new BadRequestAlertException("A new artikel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtikelDTO result = artikelService.save(artikelDTO);
        return ResponseEntity.created(new URI("/api/artikels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artikels : Updates an existing artikel.
     *
     * @param artikelDTO the artikelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artikelDTO,
     * or with status 400 (Bad Request) if the artikelDTO is not valid,
     * or with status 500 (Internal Server Error) if the artikelDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artikels")
    @Timed
    public ResponseEntity<ArtikelDTO> updateArtikel(@Valid @RequestBody ArtikelDTO artikelDTO) throws URISyntaxException {
        log.debug("REST request to update Artikel : {}", artikelDTO);
        if (artikelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtikelDTO result = artikelService.save(artikelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, artikelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artikels : get all the artikels.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of artikels in body
     */
    @GetMapping("/artikels")
    @Timed
    public ResponseEntity<List<ArtikelDTO>> getAllArtikels(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Artikels");
        Page<ArtikelDTO> page;
        if (eagerload) {
            page = artikelService.findAllWithEagerRelationships(pageable);
        } else {
            page = artikelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/artikels?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artikels/:id : get the "id" artikel.
     *
     * @param id the id of the artikelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artikelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/artikels/{id}")
    @Timed
    public ResponseEntity<ArtikelDTO> getArtikel(@PathVariable Long id) {
        log.debug("REST request to get Artikel : {}", id);
        Optional<ArtikelDTO> artikelDTO = artikelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artikelDTO);
    }

    /**
     * DELETE  /artikels/:id : delete the "id" artikel.
     *
     * @param id the id of the artikelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artikels/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtikel(@PathVariable Long id) {
        log.debug("REST request to delete Artikel : {}", id);
        artikelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/artikels?query=:query : search for the artikel corresponding
     * to the query.
     *
     * @param query the query of the artikel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/artikels")
    @Timed
    public ResponseEntity<List<ArtikelDTO>> searchArtikels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Artikels for query {}", query);
        Page<ArtikelDTO> page = artikelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/artikels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
