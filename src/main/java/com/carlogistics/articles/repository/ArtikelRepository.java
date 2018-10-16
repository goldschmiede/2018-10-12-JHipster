package com.carlogistics.articles.repository;

import com.carlogistics.articles.domain.Artikel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Artikel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, Long> {

    @Query(value = "select distinct artikel from Artikel artikel left join fetch artikel.tags",
        countQuery = "select count(distinct artikel) from Artikel artikel")
    Page<Artikel> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct artikel from Artikel artikel left join fetch artikel.tags")
    List<Artikel> findAllWithEagerRelationships();

    @Query("select artikel from Artikel artikel left join fetch artikel.tags where artikel.id =:id")
    Optional<Artikel> findOneWithEagerRelationships(@Param("id") Long id);

}
