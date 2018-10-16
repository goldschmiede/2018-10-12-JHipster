package com.carlogistics.articles.web.rest;

import com.carlogistics.articles.ArticlesApp;

import com.carlogistics.articles.domain.Kategorie;
import com.carlogistics.articles.domain.Kategorie;
import com.carlogistics.articles.domain.Artikel;
import com.carlogistics.articles.domain.Kategorie;
import com.carlogistics.articles.repository.KategorieRepository;
import com.carlogistics.articles.repository.search.KategorieSearchRepository;
import com.carlogistics.articles.service.KategorieService;
import com.carlogistics.articles.service.dto.KategorieDTO;
import com.carlogistics.articles.service.mapper.KategorieMapper;
import com.carlogistics.articles.web.rest.errors.ExceptionTranslator;
import com.carlogistics.articles.service.dto.KategorieCriteria;
import com.carlogistics.articles.service.KategorieQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.carlogistics.articles.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the KategorieResource REST controller.
 *
 * @see KategorieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticlesApp.class)
public class KategorieResourceIntTest {

    private static final String DEFAULT_BESCHREIBUNG = "AAAAAAAAAA";
    private static final String UPDATED_BESCHREIBUNG = "BBBBBBBBBB";

    private static final String DEFAULT_TITEL = "AAAAAAAAAA";
    private static final String UPDATED_TITEL = "BBBBBBBBBB";

    @Autowired
    private KategorieRepository kategorieRepository;

    @Autowired
    private KategorieMapper kategorieMapper;
    
    @Autowired
    private KategorieService kategorieService;

    /**
     * This repository is mocked in the com.carlogistics.articles.repository.search test package.
     *
     * @see com.carlogistics.articles.repository.search.KategorieSearchRepositoryMockConfiguration
     */
    @Autowired
    private KategorieSearchRepository mockKategorieSearchRepository;

    @Autowired
    private KategorieQueryService kategorieQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKategorieMockMvc;

    private Kategorie kategorie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KategorieResource kategorieResource = new KategorieResource(kategorieService, kategorieQueryService);
        this.restKategorieMockMvc = MockMvcBuilders.standaloneSetup(kategorieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kategorie createEntity(EntityManager em) {
        Kategorie kategorie = new Kategorie()
            .beschreibung(DEFAULT_BESCHREIBUNG)
            .titel(DEFAULT_TITEL);
        return kategorie;
    }

    @Before
    public void initTest() {
        kategorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createKategorie() throws Exception {
        int databaseSizeBeforeCreate = kategorieRepository.findAll().size();

        // Create the Kategorie
        KategorieDTO kategorieDTO = kategorieMapper.toDto(kategorie);
        restKategorieMockMvc.perform(post("/api/kategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kategorieDTO)))
            .andExpect(status().isCreated());

        // Validate the Kategorie in the database
        List<Kategorie> kategorieList = kategorieRepository.findAll();
        assertThat(kategorieList).hasSize(databaseSizeBeforeCreate + 1);
        Kategorie testKategorie = kategorieList.get(kategorieList.size() - 1);
        assertThat(testKategorie.getBeschreibung()).isEqualTo(DEFAULT_BESCHREIBUNG);
        assertThat(testKategorie.getTitel()).isEqualTo(DEFAULT_TITEL);

        // Validate the Kategorie in Elasticsearch
        verify(mockKategorieSearchRepository, times(1)).save(testKategorie);
    }

    @Test
    @Transactional
    public void createKategorieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kategorieRepository.findAll().size();

        // Create the Kategorie with an existing ID
        kategorie.setId(1L);
        KategorieDTO kategorieDTO = kategorieMapper.toDto(kategorie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKategorieMockMvc.perform(post("/api/kategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kategorieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kategorie in the database
        List<Kategorie> kategorieList = kategorieRepository.findAll();
        assertThat(kategorieList).hasSize(databaseSizeBeforeCreate);

        // Validate the Kategorie in Elasticsearch
        verify(mockKategorieSearchRepository, times(0)).save(kategorie);
    }

    @Test
    @Transactional
    public void getAllKategories() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList
        restKategorieMockMvc.perform(get("/api/kategories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG.toString())))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL.toString())));
    }
    
    @Test
    @Transactional
    public void getKategorie() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get the kategorie
        restKategorieMockMvc.perform(get("/api/kategories/{id}", kategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(kategorie.getId().intValue()))
            .andExpect(jsonPath("$.beschreibung").value(DEFAULT_BESCHREIBUNG.toString()))
            .andExpect(jsonPath("$.titel").value(DEFAULT_TITEL.toString()));
    }

    @Test
    @Transactional
    public void getAllKategoriesByBeschreibungIsEqualToSomething() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where beschreibung equals to DEFAULT_BESCHREIBUNG
        defaultKategorieShouldBeFound("beschreibung.equals=" + DEFAULT_BESCHREIBUNG);

        // Get all the kategorieList where beschreibung equals to UPDATED_BESCHREIBUNG
        defaultKategorieShouldNotBeFound("beschreibung.equals=" + UPDATED_BESCHREIBUNG);
    }

    @Test
    @Transactional
    public void getAllKategoriesByBeschreibungIsInShouldWork() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where beschreibung in DEFAULT_BESCHREIBUNG or UPDATED_BESCHREIBUNG
        defaultKategorieShouldBeFound("beschreibung.in=" + DEFAULT_BESCHREIBUNG + "," + UPDATED_BESCHREIBUNG);

        // Get all the kategorieList where beschreibung equals to UPDATED_BESCHREIBUNG
        defaultKategorieShouldNotBeFound("beschreibung.in=" + UPDATED_BESCHREIBUNG);
    }

    @Test
    @Transactional
    public void getAllKategoriesByBeschreibungIsNullOrNotNull() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where beschreibung is not null
        defaultKategorieShouldBeFound("beschreibung.specified=true");

        // Get all the kategorieList where beschreibung is null
        defaultKategorieShouldNotBeFound("beschreibung.specified=false");
    }

    @Test
    @Transactional
    public void getAllKategoriesByTitelIsEqualToSomething() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where titel equals to DEFAULT_TITEL
        defaultKategorieShouldBeFound("titel.equals=" + DEFAULT_TITEL);

        // Get all the kategorieList where titel equals to UPDATED_TITEL
        defaultKategorieShouldNotBeFound("titel.equals=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllKategoriesByTitelIsInShouldWork() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where titel in DEFAULT_TITEL or UPDATED_TITEL
        defaultKategorieShouldBeFound("titel.in=" + DEFAULT_TITEL + "," + UPDATED_TITEL);

        // Get all the kategorieList where titel equals to UPDATED_TITEL
        defaultKategorieShouldNotBeFound("titel.in=" + UPDATED_TITEL);
    }

    @Test
    @Transactional
    public void getAllKategoriesByTitelIsNullOrNotNull() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        // Get all the kategorieList where titel is not null
        defaultKategorieShouldBeFound("titel.specified=true");

        // Get all the kategorieList where titel is null
        defaultKategorieShouldNotBeFound("titel.specified=false");
    }

    @Test
    @Transactional
    public void getAllKategoriesByUeberkategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        Kategorie ueberkategorie = KategorieResourceIntTest.createEntity(em);
        em.persist(ueberkategorie);
        em.flush();
        kategorie.setUeberkategorie(ueberkategorie);
        kategorieRepository.saveAndFlush(kategorie);
        Long ueberkategorieId = ueberkategorie.getId();

        // Get all the kategorieList where ueberkategorie equals to ueberkategorieId
        defaultKategorieShouldBeFound("ueberkategorieId.equals=" + ueberkategorieId);

        // Get all the kategorieList where ueberkategorie equals to ueberkategorieId + 1
        defaultKategorieShouldNotBeFound("ueberkategorieId.equals=" + (ueberkategorieId + 1));
    }


    @Test
    @Transactional
    public void getAllKategoriesByArtikelIsEqualToSomething() throws Exception {
        // Initialize the database
        Artikel artikel = ArtikelResourceIntTest.createEntity(em);
        em.persist(artikel);
        em.flush();
        kategorie.addArtikel(artikel);
        kategorieRepository.saveAndFlush(kategorie);
        Long artikelId = artikel.getId();

        // Get all the kategorieList where artikel equals to artikelId
        defaultKategorieShouldBeFound("artikelId.equals=" + artikelId);

        // Get all the kategorieList where artikel equals to artikelId + 1
        defaultKategorieShouldNotBeFound("artikelId.equals=" + (artikelId + 1));
    }


    @Test
    @Transactional
    public void getAllKategoriesByUnterkategorienIsEqualToSomething() throws Exception {
        // Initialize the database
        Kategorie unterkategorien = KategorieResourceIntTest.createEntity(em);
        em.persist(unterkategorien);
        em.flush();
        kategorie.addUnterkategorien(unterkategorien);
        kategorieRepository.saveAndFlush(kategorie);
        Long unterkategorienId = unterkategorien.getId();

        // Get all the kategorieList where unterkategorien equals to unterkategorienId
        defaultKategorieShouldBeFound("unterkategorienId.equals=" + unterkategorienId);

        // Get all the kategorieList where unterkategorien equals to unterkategorienId + 1
        defaultKategorieShouldNotBeFound("unterkategorienId.equals=" + (unterkategorienId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultKategorieShouldBeFound(String filter) throws Exception {
        restKategorieMockMvc.perform(get("/api/kategories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG.toString())))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultKategorieShouldNotBeFound(String filter) throws Exception {
        restKategorieMockMvc.perform(get("/api/kategories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingKategorie() throws Exception {
        // Get the kategorie
        restKategorieMockMvc.perform(get("/api/kategories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKategorie() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        int databaseSizeBeforeUpdate = kategorieRepository.findAll().size();

        // Update the kategorie
        Kategorie updatedKategorie = kategorieRepository.findById(kategorie.getId()).get();
        // Disconnect from session so that the updates on updatedKategorie are not directly saved in db
        em.detach(updatedKategorie);
        updatedKategorie
            .beschreibung(UPDATED_BESCHREIBUNG)
            .titel(UPDATED_TITEL);
        KategorieDTO kategorieDTO = kategorieMapper.toDto(updatedKategorie);

        restKategorieMockMvc.perform(put("/api/kategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kategorieDTO)))
            .andExpect(status().isOk());

        // Validate the Kategorie in the database
        List<Kategorie> kategorieList = kategorieRepository.findAll();
        assertThat(kategorieList).hasSize(databaseSizeBeforeUpdate);
        Kategorie testKategorie = kategorieList.get(kategorieList.size() - 1);
        assertThat(testKategorie.getBeschreibung()).isEqualTo(UPDATED_BESCHREIBUNG);
        assertThat(testKategorie.getTitel()).isEqualTo(UPDATED_TITEL);

        // Validate the Kategorie in Elasticsearch
        verify(mockKategorieSearchRepository, times(1)).save(testKategorie);
    }

    @Test
    @Transactional
    public void updateNonExistingKategorie() throws Exception {
        int databaseSizeBeforeUpdate = kategorieRepository.findAll().size();

        // Create the Kategorie
        KategorieDTO kategorieDTO = kategorieMapper.toDto(kategorie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKategorieMockMvc.perform(put("/api/kategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kategorieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kategorie in the database
        List<Kategorie> kategorieList = kategorieRepository.findAll();
        assertThat(kategorieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Kategorie in Elasticsearch
        verify(mockKategorieSearchRepository, times(0)).save(kategorie);
    }

    @Test
    @Transactional
    public void deleteKategorie() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);

        int databaseSizeBeforeDelete = kategorieRepository.findAll().size();

        // Get the kategorie
        restKategorieMockMvc.perform(delete("/api/kategories/{id}", kategorie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Kategorie> kategorieList = kategorieRepository.findAll();
        assertThat(kategorieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Kategorie in Elasticsearch
        verify(mockKategorieSearchRepository, times(1)).deleteById(kategorie.getId());
    }

    @Test
    @Transactional
    public void searchKategorie() throws Exception {
        // Initialize the database
        kategorieRepository.saveAndFlush(kategorie);
        when(mockKategorieSearchRepository.search(queryStringQuery("id:" + kategorie.getId())))
            .thenReturn(Collections.singletonList(kategorie));
        // Search the kategorie
        restKategorieMockMvc.perform(get("/api/_search/kategories?query=id:" + kategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG.toString())))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kategorie.class);
        Kategorie kategorie1 = new Kategorie();
        kategorie1.setId(1L);
        Kategorie kategorie2 = new Kategorie();
        kategorie2.setId(kategorie1.getId());
        assertThat(kategorie1).isEqualTo(kategorie2);
        kategorie2.setId(2L);
        assertThat(kategorie1).isNotEqualTo(kategorie2);
        kategorie1.setId(null);
        assertThat(kategorie1).isNotEqualTo(kategorie2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KategorieDTO.class);
        KategorieDTO kategorieDTO1 = new KategorieDTO();
        kategorieDTO1.setId(1L);
        KategorieDTO kategorieDTO2 = new KategorieDTO();
        assertThat(kategorieDTO1).isNotEqualTo(kategorieDTO2);
        kategorieDTO2.setId(kategorieDTO1.getId());
        assertThat(kategorieDTO1).isEqualTo(kategorieDTO2);
        kategorieDTO2.setId(2L);
        assertThat(kategorieDTO1).isNotEqualTo(kategorieDTO2);
        kategorieDTO1.setId(null);
        assertThat(kategorieDTO1).isNotEqualTo(kategorieDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(kategorieMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(kategorieMapper.fromId(null)).isNull();
    }
}
