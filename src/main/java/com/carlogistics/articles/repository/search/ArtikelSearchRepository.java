package com.carlogistics.articles.repository.search;

import com.carlogistics.articles.domain.Artikel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Artikel entity.
 */
public interface ArtikelSearchRepository extends ElasticsearchRepository<Artikel, Long> {
}
