package com.instahash.api.hashtag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagDao extends PagingAndSortingRepository<Hashtag, String> {

    List<Hashtag> findAll();
    Page<Hashtag> findAll(Pageable pageable);

    Hashtag findByTagIgnoreCase(String hashtag);




}
