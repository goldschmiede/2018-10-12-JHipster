package com.carlogistics.articles.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ArtikelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ArtikelSearchRepositoryMockConfiguration {

    @MockBean
    private ArtikelSearchRepository mockArtikelSearchRepository;

}
