package com.carlogistics.articles.repository.search;

import com.carlogistics.articles.domain.Kategorie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Kategorie entity.
 */
public interface KategorieSearchRepository extends ElasticsearchRepository<Kategorie, Long> {
}
