package com.instahash.api.web.controller;

import com.instahash.api.hashtag.HashtagService;
import com.instahash.api.selenium.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UpdateController {

    @Autowired
    private HashtagService hashtagService;

    @RequestMapping("update/{hashtag}")
    @ResponseBody
    public String update(@PathVariable String hashtag) throws Exception{

        BuildService buildService = new BuildService(hashtagService);

        if(hashtag.startsWith("#")){
            hashtag = hashtag.replace("#","");
        }

        try {
            buildService.updateDatabase(hashtag, "L");
        }catch (Exception e){
            buildService.closeDriver();
        }

        return "updated";
    }

    @RequestMapping("updateW/{hashtag}")
    @ResponseBody
    public String updateWindows(@PathVariable String hashtag) throws Exception{

        BuildService buildService = new BuildService(hashtagService);

        if(hashtag.startsWith("#")){
            hashtag = hashtag.replace("#","");
        }

        try {
            buildService.updateDatabase(hashtag, "W");
        }catch (Exception e){
            buildService.closeDriver();
        }

        return "updated";
    }
}
