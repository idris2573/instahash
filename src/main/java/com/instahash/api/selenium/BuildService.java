package com.instahash.api.selenium;

import com.instahash.api.hashtag.Hashtag;
import com.instahash.api.hashtag.HashtagService;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@Configuration
public class BuildService {
    private WebDriver driver;
    private Queue<String> tagQueue = new ArrayDeque<>();
    private List<String> completed = new ArrayList<>();
    private HashtagService hashtagService;

    public void updateDatabase(String tag, String os) throws Exception{

        tag = removeHashtagFromStart(tag);

        chooseOS(os);

        driver.get("https://www.instagram.com/explore/tags/love/");

        /// if hashtag dont exist make a new one
        Hashtag hashtag = createOrGetHashtag(tag);
        System.out.println("\n\nmain tag - " + hashtag.getTag());
        hashtag.getRelated().addAll(scrapeRelated(tag));
        hashtagService.save(hashtag);

        completed.add(tag);

        while (!tagQueue.isEmpty()){
            tag = tagQueue.poll();
            hashtag = createOrGetHashtag(tag);

            if(!completed.contains(hashtag.getTag())) {
                System.out.println("\n\nmain tag - " + hashtag.getTag());

                try {
                    hashtag.getRelated().addAll(scrapeRelated(tag));
                    hashtagService.save(hashtag);
                    completed.add(hashtag.getTag());
                }catch (Exception e){}

            }
        }

        driver.close();
        driver.quit();
    }


    private List<Hashtag> scrapeRelated(String tag) throws Exception{

        List<Hashtag> relatedTagList = new ArrayList<>();

        tag = removeHashtagFromStart(tag);

        driver.findElement(By.tagName("input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(By.tagName("input")).sendKeys("#" + tag);

        Hashtag hashtag = new Hashtag(tag);

        //////////////////////////////////////////////////////////
        String tagText = getTagText(tag);
        String[] tagArray = tagText.split("#");

        for(String relatedString : tagArray){
            if(relatedString.length() > 0 && !relatedString.equalsIgnoreCase("No results found.")) {

                relatedString = removeBadChar(relatedString.substring(0, relatedString.indexOf("\n")));

                ///////////////////////////////////////////////

//                relatedString = removeBadChar(relatedString);

                if (!relatedString.equalsIgnoreCase(tag)) {

                    Hashtag relatedHashtag = createOrGetHashtag(relatedString);

                    if (!hashtag.getRelated().contains(relatedHashtag)) {
                        hashtag.getRelated().add(relatedHashtag);
                    }

                    if (!tagQueue.contains(relatedString)){
                        tagQueue.add(relatedString);
                    }

                    relatedTagList.add(relatedHashtag);

                    System.out.println(relatedString);

                 }

            }
        }

        return relatedTagList;
    }

    private String getTagText(String tag) throws Exception{
        String tagText = null;

        int fail = 0;

        do {
            try {
                tagText = driver.findElement(By.className("_dv59m")).getText();
            }catch (Exception e){}

            if(fail > 10){
                break;
            }
            fail++;
            Thread.sleep(500);
        }while (tagText == null || !tagText.toLowerCase().startsWith("#" + tag.toLowerCase()));

        return tagText;
    }


    private String removeBadChar(String relatedTag) throws Exception{
//        byte tagWithIcons[] = relatedTag.getBytes();
//        relatedTag = new String(tagWithIcons, "UTF-8").replace("?", "");
//        relatedTag = relatedTag.replace("�","");
        return  relatedTag.replaceAll("[^A-Za-z0-9]", "");
    }



    public BuildService (HashtagService hashtagService){
        this.hashtagService = hashtagService;
    }



    private Hashtag createOrGetHashtag(String tag){
        Hashtag hashtag;
        try {
            hashtag = hashtagService.findByTagIgnoreCase(tag);
        }catch (Exception e){
            hashtag = null;
        }

        if(hashtag == null){
            hashtag = new Hashtag(tag);
            hashtagService.save(hashtag);
        }

        return hashtag;
    }

    private String removeHashtagFromStart(String tag){
        if(tag.startsWith("#")){
            tag = tag.replace("#", "");
        }
        return tag;
    }




    // OS STUIFF

    private void chooseOS(String os) throws Exception{
        if(os.equals("L")){
            driver = driverL();
        }else {
            driver = driverW();
        }
    }
    private WebDriver driverW()  throws Exception{
        ChromeOptions chromeOptions = new ChromeOptions();
//         chromeOptions.addArguments("--headless");

        URL resource = BuildService.class.getResource("/chromedriver3.exe");
        File file = Paths.get(resource.toURI()).toFile();
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

        return new ChromeDriver(chromeOptions);
    }
    private WebDriver driverL()  throws Exception{
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox");


        return new ChromeDriver(chromeOptions);
    }
    public void closeDriver(){
        driver.close();
        driver.quit();
    }























    // OLD SHIT
    private HashtagScrape getHashtagOld(String tag) throws Exception{

        if(tag.startsWith("#")){
            tag = tag.replace("#", "");
        }

//        driver.findElement(By.tagName("input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
//        driver.findElement(By.tagName("input")).sendKeys(tag);

        driver.findElement(By.tagName("input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(By.tagName("input")).sendKeys(tag);

        HashtagScrape hashtag = new HashtagScrape(tag);
        Hashtag hashtagModel = new Hashtag(tag);

        Thread.sleep(1500);

        List<WebElement> elements = driver.findElements(By.tagName("li"));

        for(WebElement element : elements){

            String relatedTag = element.getText();

            if(relatedTag.startsWith("#")){
                relatedTag = relatedTag.replace("#", "");
            }


            if(!relatedTag.equals(tag)) {

                hashtag.getRelated().add(relatedTag);
                System.out.println(relatedTag);

                Hashtag hashtagRelatedModel = hashtagService.findByTagIgnoreCase(relatedTag);

                if(hashtagRelatedModel == null){
                    hashtagRelatedModel = new Hashtag(relatedTag);
                    hashtagService.save(hashtagRelatedModel);
                }

                if(!hashtagModel.getRelated().contains(hashtagRelatedModel)) {
                    hashtagModel.getRelated().add(hashtagRelatedModel);
                }

            }
        }

        hashtagService.save(hashtagModel);
        return hashtag;
    }
}
