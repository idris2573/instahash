package com.instahash.api.selenium;

import java.util.ArrayList;
import java.util.List;

public class HashtagScrape {

    private String hashtag;

    private List<String> related = new ArrayList<>();

    public HashtagScrape(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public List<String> getRelated() {
        return related;
    }

    public void setRelated(List<String> related) {
        this.related = related;
    }
}
