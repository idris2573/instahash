package com.instahash.api.web.controller;

import com.instahash.api.hashtag.Hashtag;
import com.instahash.api.hashtag.HashtagService;
import com.instahash.api.selenium.BuildService;
import com.instahash.api.selenium.Save;
import com.instahash.api.tools.Read;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

//    @Autowired
//    private BuildService buildService;

    @Autowired
    private HashtagService hashtagService;

    @RequestMapping("/admin")
    public String admin(Model model, @PageableDefault(size = 1000) Pageable pageable){

        Page<Hashtag> hashtags = hashtagService.findAll(pageable);


        model.addAttribute("hashtags", hashtags);
        model.addAttribute("searchTag", new Hashtag());
        model.addAttribute("size", hashtags.getTotalElements());

        model.addAttribute("next", pageable.getPageNumber()+1);
        model.addAttribute("prev", pageable.getPageNumber()-1);
        model.addAttribute("pages", hashtags.getTotalPages());
        return "admin";
    }

    @RequestMapping(value = "/update-admin", method = RequestMethod.POST)
    @ResponseBody
    public String updateAdmin(@ModelAttribute Hashtag searchTag) throws Exception{

        BuildService buildService = new BuildService(hashtagService);

        String hashtag = searchTag.getTag();

        if(hashtag.startsWith("#")){
            hashtag = hashtag.replace("#","");
        }

        try {
            buildService.updateDatabase(hashtag, "L");
        }catch (Exception e){
            e.printStackTrace();
            buildService.closeDriver();
        }

        return "updated";
    }

    @RequestMapping("/auto{no}")
    @ResponseBody
    public String autoUpdate(@PathVariable String no) throws Exception{

        BuildService buildService = new BuildService(hashtagService);

        //windows
        Read read = new Read("H:\\Work\\Arfan C - Instahash\\src\\main\\resources\\list" + no + ".txt", ",");
        List<String> completed = new Read("save/completed.txt", ",").getLine();
        completed.addAll(new Read("save/failed.txt", ",").getLine());

        //linux
//        Read read = new Read("/hashtags/resources/main/list" + no + ".txt", ",");
//        List<String> completed = new Read("/hashtags/save/completed.txt", ",").getLine();
//        completed.addAll(new Read("/hashtags/save/failed.txt", ",").getLine());



        Save save = new Save();

        for(String hashtag : read.getLine()) {

            if(!completed.contains(hashtag)) {
                if (hashtag.startsWith("#")) {
                    hashtag = hashtag.replace("#", "");
                }

                try {
                    buildService.updateDatabase(hashtag, "W");
                } catch (Exception e) {
                    e.printStackTrace();
                    buildService.closeDriver();
                    save.saveStringToTxt(String.format(hashtag), "failed");
//                    save.saveStringToTxt(String.format("list %s: %s", no, hashtag), "failed");
                }
                save.saveStringToTxt(String.format(hashtag), "completed");
//                save.saveStringToTxt(String.format("list %s: %s", no, hashtag), "completed");
            }
        }
        return "updated";
    }
}
