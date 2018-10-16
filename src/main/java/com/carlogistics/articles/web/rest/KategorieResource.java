package com.carlogistics.articles.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.carlogistics.articles.service.KategorieService;
import com.carlogistics.articles.web.rest.errors.BadRequestAlertException;
import com.carlogistics.articles.web.rest.util.HeaderUtil;
import com.carlogistics.articles.service.dto.KategorieDTO;
import com.carlogistics.articles.service.dto.KategorieCriteria;
import com.carlogistics.articles.service.KategorieQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Kategorie.
 */
@RestController
@RequestMapping("/api")
public class KategorieResource {

    private final Logger log = LoggerFactory.getLogger(KategorieResource.class);

    private static final String ENTITY_NAME = "kategorie";

    private final KategorieService kategorieService;

    private final KategorieQueryService kategorieQueryService;

    public KategorieResource(KategorieService kategorieService, KategorieQueryService kategorieQueryService) {
        this.kategorieService = kategorieService;
        this.kategorieQueryService = kategorieQueryService;
    }

    /**
     * POST  /kategories : Create a new kategorie.
     *
     * @param kategorieDTO the kategorieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new kategorieDTO, or with status 400 (Bad Request) if the kategorie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/kategories")
    @Timed
    public ResponseEntity<KategorieDTO> createKategorie(@Valid @RequestBody KategorieDTO kategorieDTO) throws URISyntaxException {
        log.debug("REST request to save Kategorie : {}", kategorieDTO);
        if (kategorieDTO.getId() != null) {
            throw new BadRequestAlertException("A new kategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KategorieDTO result = kategorieService.save(kategorieDTO);
        return ResponseEntity.created(new URI("/api/kategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /kategories : Updates an existing kategorie.
     *
     * @param kategorieDTO the kategorieDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated kategorieDTO,
     * or with status 400 (Bad Request) if the kategorieDTO is not valid,
     * or with status 500 (Internal Server Error) if the kategorieDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/kategories")
    @Timed
    public ResponseEntity<KategorieDTO> updateKategorie(@Valid @RequestBody KategorieDTO kategorieDTO) throws URISyntaxException {
        log.debug("REST request to update Kategorie : {}", kategorieDTO);
        if (kategorieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KategorieDTO result = kategorieService.save(kategorieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, kategorieDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /kategories : get all the kategories.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of kategories in body
     */
    @GetMapping("/kategories")
    @Timed
    public ResponseEntity<List<KategorieDTO>> getAllKategories(KategorieCriteria criteria) {
        log.debug("REST request to get Kategories by criteria: {}", criteria);
        List<KategorieDTO> entityList = kategorieQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /kategories/:id : get the "id" kategorie.
     *
     * @param id the id of the kategorieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the kategorieDTO, or with status 404 (Not Found)
     */
    @GetMapping("/kategories/{id}")
    @Timed
    public ResponseEntity<KategorieDTO> getKategorie(@PathVariable Long id) {
        log.debug("REST request to get Kategorie : {}", id);
        Optional<KategorieDTO> kategorieDTO = kategorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kategorieDTO);
    }

    /**
     * DELETE  /kategories/:id : delete the "id" kategorie.
     *
     * @param id the id of the kategorieDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/kategories/{id}")
    @Timed
    public ResponseEntity<Void> deleteKategorie(@PathVariable Long id) {
        log.debug("REST request to delete Kategorie : {}", id);
        kategorieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/kategories?query=:query : search for the kategorie corresponding
     * to the query.
     *
     * @param query the query of the kategorie search
     * @return the result of the search
     */
    @GetMapping("/_search/kategories")
    @Timed
    public List<KategorieDTO> searchKategories(@RequestParam String query) {
        log.debug("REST request to search Kategories for query {}", query);
        return kategorieService.search(query);
    }

}
