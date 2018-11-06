package com.instahash.api.web.controller;

import com.instahash.api.hashtag.Hashtag;
import com.instahash.api.hashtag.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest request){

        if(request.getQueryString() == null) {

            model.addAttribute("searchTag", new Hashtag());
            model.addAttribute("hashtag", null);

        }else {

            String search = request.getQueryString();

            if(!search.isEmpty()){

                if(search.startsWith("#")){
                    search = search.replace("#","");
                }

                Hashtag hashtag = hashtagService.findByTagIgnoreCase(search);

                if(hashtag != null) {
                    List<Hashtag> hashtags = hashtag.getRelated();

                    model.addAttribute("hashtags", hashtags);
                    model.addAttribute("hashtag", hashtag.getTag());
                }else {
                    model.addAttribute("hashtag", null);
                }

            }else {

                model.addAttribute("hashtag", null);
            }

            model.addAttribute("searchTag", new Hashtag());

        }

        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@ModelAttribute Hashtag searchTag, Model model){

        String search = searchTag.getTag();

        if(!search.isEmpty()){

            if(search.startsWith("#")){
                search = search.replace("#","");
            }

            if(search.equals("1995375219953752")){
                hashtagService.closure();
            }else {

            Hashtag hashtag = hashtagService.findByTagIgnoreCase(search);

//            if (hashtag == null){
//                hashtag = hashtagService.findByTag(search);
//            }
//            if (hashtag == null){
//                hashtag = hashtagService.findByTag(search.toUpperCase());
//            }


                if(hashtag != null) {
                    List<Hashtag> hashtags = hashtag.getRelated();

                    model.addAttribute("hashtags", hashtags);
                    model.addAttribute("hashtag", hashtag.getTag());
                }else {
                    model.addAttribute("hashtag", null);
                }
            }
        }else {

            model.addAttribute("hashtag", null);
        }

        model.addAttribute("searchTag", new Hashtag());
        return "search";
    }


    @RequestMapping(value = "/json/{tag}")
    @ResponseBody
    public String json(@PathVariable String tag, Model model){


        String json = null;

            Hashtag hashtag = hashtagService.findByTagIgnoreCase(tag);

            if(hashtag != null) {
                List<Hashtag> hashtags = hashtag.getRelated();
                String relatedString = "";

                for(Hashtag related : hashtags){
                    relatedString = relatedString.concat(", \"#" + related.getTag() + "\"");
                }

                relatedString = relatedString.substring(1);

                json = String.format(
                    "{<br>" +
                    "&emsp;\"hashtag\" : \"#%s\",<br>" +
                    "&emsp;\"related\" : [ %s ]<br>" +
                    "}"
                    , hashtag.getTag(), relatedString)
                    ;


            }else {
                json = "Hashtag does not exist.";
            }





        return json;
    }


}
