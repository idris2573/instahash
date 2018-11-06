package com.instahash.api.hashtag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hashtag {

    @Id
    private String tag;

    @ManyToMany
    private List<Hashtag> related = new ArrayList<>();

    public Hashtag() {
    }

    public Hashtag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Hashtag> getRelated() {
        return related;
    }

    public String removeHash(String tag){
        return tag.replace("#", "");
    }
}
