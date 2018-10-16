package com.carlogistics.articles.web.rest;

import com.carlogistics.articles.ArticlesApp;

import com.carlogistics.articles.domain.Artikel;
import com.carlogistics.articles.repository.ArtikelRepository;
import com.carlogistics.articles.repository.search.ArtikelSearchRepository;
import com.carlogistics.articles.service.ArtikelService;
import com.carlogistics.articles.service.dto.ArtikelDTO;
import com.carlogistics.articles.service.mapper.ArtikelMapper;
import com.carlogistics.articles.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.carlogistics.articles.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carlogistics.articles.domain.enumeration.Verfuegbarkeit;
/**
 * Test class for the ArtikelResource REST controller.
 *
 * @see ArtikelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticlesApp.class)
public class ArtikelResourceIntTest {

    private static final String DEFAULT_ARTIKELNUMMER = "AAAAAAAAAA";
    private static final String UPDATED_ARTIKELNUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_BESCHREIBUNG = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BESCHREIBUNG = "BBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_TITEL = "AAAAAAAAAA";
    private static final String UPDATED_TITEL = "BBBBBBBBBB";

    private static final Double DEFAULT_PREIS = 1D;
    private static final Double UPDATED_PREIS = 2D;

    private static final LocalDate DEFAULT_EINGESTELLT_AM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EINGESTELLT_AM = LocalDate.now(ZoneId.systemDefault());

    private static final Verfuegbarkeit DEFAULT_VERFUEGBARKEIT = Verfuegbarkeit.VORRAETIG;
    private static final Verfuegbarkeit UPDATED_VERFUEGBARKEIT = Verfuegbarkeit.AUSVERKAUFT;

    @Autowired
    private ArtikelRepository artikelRepository;

    @Mock
    private ArtikelRepository artikelRepositoryMock;

    @Autowired
    private ArtikelMapper artikelMapper;
    

    @Mock
    private ArtikelService artikelServiceMock;

    @Autowired
    private ArtikelService artikelService;

    /**
     * This repository is mocked in the com.carlogistics.articles.repository.search test package.
     *
     * @see com.carlogistics.articles.repository.search.ArtikelSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArtikelSearchRepository mockArtikelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtikelMockMvc;

    private Artikel artikel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtikelResource artikelResource = new ArtikelResource(artikelService);
        this.restArtikelMockMvc = MockMvcBuilders.standaloneSetup(artikelResource)
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
    public static Artikel createEntity(EntityManager em) {
        Artikel artikel = new Artikel()
            .artikelnummer(DEFAULT_ARTIKELNUMMER)
            .beschreibung(DEFAULT_BESCHREIBUNG)
            .titel(DEFAULT_TITEL)
            .preis(DEFAULT_PREIS)
            .eingestelltAm(DEFAULT_EINGESTELLT_AM)
            .verfuegbarkeit(DEFAULT_VERFUEGBARKEIT);
        return artikel;
    }

    @Before
    public void initTest() {
        artikel = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtikel() throws Exception {
        int databaseSizeBeforeCreate = artikelRepository.findAll().size();

        // Create the Artikel
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);
        restArtikelMockMvc.perform(post("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isCreated());

        // Validate the Artikel in the database
        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeCreate + 1);
        Artikel testArtikel = artikelList.get(artikelList.size() - 1);
        assertThat(testArtikel.getArtikelnummer()).isEqualTo(DEFAULT_ARTIKELNUMMER);
        assertThat(testArtikel.getBeschreibung()).isEqualTo(DEFAULT_BESCHREIBUNG);
        assertThat(testArtikel.getTitel()).isEqualTo(DEFAULT_TITEL);
        assertThat(testArtikel.getPreis()).isEqualTo(DEFAULT_PREIS);
        assertThat(testArtikel.getEingestelltAm()).isEqualTo(DEFAULT_EINGESTELLT_AM);
        assertThat(testArtikel.getVerfuegbarkeit()).isEqualTo(DEFAULT_VERFUEGBARKEIT);

        // Validate the Artikel in Elasticsearch
        verify(mockArtikelSearchRepository, times(1)).save(testArtikel);
    }

    @Test
    @Transactional
    public void createArtikelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artikelRepository.findAll().size();

        // Create the Artikel with an existing ID
        artikel.setId(1L);
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtikelMockMvc.perform(post("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artikel in the database
        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Artikel in Elasticsearch
        verify(mockArtikelSearchRepository, times(0)).save(artikel);
    }

    @Test
    @Transactional
    public void checkArtikelnummerIsRequired() throws Exception {
        int databaseSizeBeforeTest = artikelRepository.findAll().size();
        // set the field null
        artikel.setArtikelnummer(null);

        // Create the Artikel, which fails.
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);

        restArtikelMockMvc.perform(post("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isBadRequest());

        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreisIsRequired() throws Exception {
        int databaseSizeBeforeTest = artikelRepository.findAll().size();
        // set the field null
        artikel.setPreis(null);

        // Create the Artikel, which fails.
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);

        restArtikelMockMvc.perform(post("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isBadRequest());

        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVerfuegbarkeitIsRequired() throws Exception {
        int databaseSizeBeforeTest = artikelRepository.findAll().size();
        // set the field null
        artikel.setVerfuegbarkeit(null);

        // Create the Artikel, which fails.
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);

        restArtikelMockMvc.perform(post("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isBadRequest());

        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtikels() throws Exception {
        // Initialize the database
        artikelRepository.saveAndFlush(artikel);

        // Get all the artikelList
        restArtikelMockMvc.perform(get("/api/artikels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artikel.getId().intValue())))
            .andExpect(jsonPath("$.[*].artikelnummer").value(hasItem(DEFAULT_ARTIKELNUMMER.toString())))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG.toString())))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL.toString())))
            .andExpect(jsonPath("$.[*].preis").value(hasItem(DEFAULT_PREIS.doubleValue())))
            .andExpect(jsonPath("$.[*].eingestelltAm").value(hasItem(DEFAULT_EINGESTELLT_AM.toString())))
            .andExpect(jsonPath("$.[*].verfuegbarkeit").value(hasItem(DEFAULT_VERFUEGBARKEIT.toString())));
    }
    
    public void getAllArtikelsWithEagerRelationshipsIsEnabled() throws Exception {
        ArtikelResource artikelResource = new ArtikelResource(artikelServiceMock);
        when(artikelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restArtikelMockMvc = MockMvcBuilders.standaloneSetup(artikelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restArtikelMockMvc.perform(get("/api/artikels?eagerload=true"))
        .andExpect(status().isOk());

        verify(artikelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllArtikelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ArtikelResource artikelResource = new ArtikelResource(artikelServiceMock);
            when(artikelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restArtikelMockMvc = MockMvcBuilders.standaloneSetup(artikelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restArtikelMockMvc.perform(get("/api/artikels?eagerload=true"))
        .andExpect(status().isOk());

            verify(artikelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getArtikel() throws Exception {
        // Initialize the database
        artikelRepository.saveAndFlush(artikel);

        // Get the artikel
        restArtikelMockMvc.perform(get("/api/artikels/{id}", artikel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artikel.getId().intValue()))
            .andExpect(jsonPath("$.artikelnummer").value(DEFAULT_ARTIKELNUMMER.toString()))
            .andExpect(jsonPath("$.beschreibung").value(DEFAULT_BESCHREIBUNG.toString()))
            .andExpect(jsonPath("$.titel").value(DEFAULT_TITEL.toString()))
            .andExpect(jsonPath("$.preis").value(DEFAULT_PREIS.doubleValue()))
            .andExpect(jsonPath("$.eingestelltAm").value(DEFAULT_EINGESTELLT_AM.toString()))
            .andExpect(jsonPath("$.verfuegbarkeit").value(DEFAULT_VERFUEGBARKEIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtikel() throws Exception {
        // Get the artikel
        restArtikelMockMvc.perform(get("/api/artikels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtikel() throws Exception {
        // Initialize the database
        artikelRepository.saveAndFlush(artikel);

        int databaseSizeBeforeUpdate = artikelRepository.findAll().size();

        // Update the artikel
        Artikel updatedArtikel = artikelRepository.findById(artikel.getId()).get();
        // Disconnect from session so that the updates on updatedArtikel are not directly saved in db
        em.detach(updatedArtikel);
        updatedArtikel
            .artikelnummer(UPDATED_ARTIKELNUMMER)
            .beschreibung(UPDATED_BESCHREIBUNG)
            .titel(UPDATED_TITEL)
            .preis(UPDATED_PREIS)
            .eingestelltAm(UPDATED_EINGESTELLT_AM)
            .verfuegbarkeit(UPDATED_VERFUEGBARKEIT);
        ArtikelDTO artikelDTO = artikelMapper.toDto(updatedArtikel);

        restArtikelMockMvc.perform(put("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isOk());

        // Validate the Artikel in the database
        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeUpdate);
        Artikel testArtikel = artikelList.get(artikelList.size() - 1);
        assertThat(testArtikel.getArtikelnummer()).isEqualTo(UPDATED_ARTIKELNUMMER);
        assertThat(testArtikel.getBeschreibung()).isEqualTo(UPDATED_BESCHREIBUNG);
        assertThat(testArtikel.getTitel()).isEqualTo(UPDATED_TITEL);
        assertThat(testArtikel.getPreis()).isEqualTo(UPDATED_PREIS);
        assertThat(testArtikel.getEingestelltAm()).isEqualTo(UPDATED_EINGESTELLT_AM);
        assertThat(testArtikel.getVerfuegbarkeit()).isEqualTo(UPDATED_VERFUEGBARKEIT);

        // Validate the Artikel in Elasticsearch
        verify(mockArtikelSearchRepository, times(1)).save(testArtikel);
    }

    @Test
    @Transactional
    public void updateNonExistingArtikel() throws Exception {
        int databaseSizeBeforeUpdate = artikelRepository.findAll().size();

        // Create the Artikel
        ArtikelDTO artikelDTO = artikelMapper.toDto(artikel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtikelMockMvc.perform(put("/api/artikels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artikelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artikel in the database
        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Artikel in Elasticsearch
        verify(mockArtikelSearchRepository, times(0)).save(artikel);
    }

    @Test
    @Transactional
    public void deleteArtikel() throws Exception {
        // Initialize the database
        artikelRepository.saveAndFlush(artikel);

        int databaseSizeBeforeDelete = artikelRepository.findAll().size();

        // Get the artikel
        restArtikelMockMvc.perform(delete("/api/artikels/{id}", artikel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Artikel> artikelList = artikelRepository.findAll();
        assertThat(artikelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Artikel in Elasticsearch
        verify(mockArtikelSearchRepository, times(1)).deleteById(artikel.getId());
    }

    @Test
    @Transactional
    public void searchArtikel() throws Exception {
        // Initialize the database
        artikelRepository.saveAndFlush(artikel);
        when(mockArtikelSearchRepository.search(queryStringQuery("id:" + artikel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(artikel), PageRequest.of(0, 1), 1));
        // Search the artikel
        restArtikelMockMvc.perform(get("/api/_search/artikels?query=id:" + artikel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artikel.getId().intValue())))
            .andExpect(jsonPath("$.[*].artikelnummer").value(hasItem(DEFAULT_ARTIKELNUMMER.toString())))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG.toString())))
            .andExpect(jsonPath("$.[*].titel").value(hasItem(DEFAULT_TITEL.toString())))
            .andExpect(jsonPath("$.[*].preis").value(hasItem(DEFAULT_PREIS.doubleValue())))
            .andExpect(jsonPath("$.[*].eingestelltAm").value(hasItem(DEFAULT_EINGESTELLT_AM.toString())))
            .andExpect(jsonPath("$.[*].verfuegbarkeit").value(hasItem(DEFAULT_VERFUEGBARKEIT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artikel.class);
        Artikel artikel1 = new Artikel();
        artikel1.setId(1L);
        Artikel artikel2 = new Artikel();
        artikel2.setId(artikel1.getId());
        assertThat(artikel1).isEqualTo(artikel2);
        artikel2.setId(2L);
        assertThat(artikel1).isNotEqualTo(artikel2);
        artikel1.setId(null);
        assertThat(artikel1).isNotEqualTo(artikel2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtikelDTO.class);
        ArtikelDTO artikelDTO1 = new ArtikelDTO();
        artikelDTO1.setId(1L);
        ArtikelDTO artikelDTO2 = new ArtikelDTO();
        assertThat(artikelDTO1).isNotEqualTo(artikelDTO2);
        artikelDTO2.setId(artikelDTO1.getId());
        assertThat(artikelDTO1).isEqualTo(artikelDTO2);
        artikelDTO2.setId(2L);
        assertThat(artikelDTO1).isNotEqualTo(artikelDTO2);
        artikelDTO1.setId(null);
        assertThat(artikelDTO1).isNotEqualTo(artikelDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(artikelMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(artikelMapper.fromId(null)).isNull();
    }
}
