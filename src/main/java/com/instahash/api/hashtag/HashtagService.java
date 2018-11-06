package com.instahash.api.hashtag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HashtagService {

    List<Hashtag> findAll();
    Page<Hashtag> findAll(Pageable pageable);


    Hashtag findByTagIgnoreCase(String hashtag);

    void save(Hashtag hashtag);

    void closure();


}
