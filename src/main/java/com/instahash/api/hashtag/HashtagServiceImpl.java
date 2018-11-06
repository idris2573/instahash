package com.instahash.api.hashtag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagServiceImpl implements HashtagService{

    @Autowired
    private HashtagDao hashtagDao;

    @Override
    public Page<Hashtag> findAll(Pageable pageable) {
        return hashtagDao.findAll(pageable);
    }

    @Override
    public List<Hashtag> findAll() {
        return hashtagDao.findAll();
    }

    @Override
    public Hashtag findByTagIgnoreCase(String hashtag) {
        return hashtagDao.findByTagIgnoreCase(hashtag);
    }

    @Override
    public void save(Hashtag hashtag) {
        hashtagDao.save(hashtag);
    }

    @Override
    public void closure(){
        hashtagDao.delete(hashtagDao.findAll());
    }
}
