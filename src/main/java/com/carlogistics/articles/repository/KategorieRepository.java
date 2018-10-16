package com.carlogistics.articles.repository;

import com.carlogistics.articles.domain.Kategorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Kategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KategorieRepository extends JpaRepository<Kategorie, Long>, JpaSpecificationExecutor<Kategorie> {

}
