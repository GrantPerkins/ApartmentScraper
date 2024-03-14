package org.perkins.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class FloorplanScraper {
    public String mainURL = "https://smithandburnsseattle.com/floorplans/";
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";

    private Document doc;

    public FloorplanScraper() {

    }

    public Document fetchDocument(String url) {
        try {
            doc = Jsoup
                    .connect(url)
                    .userAgent(userAgent)
                    .header("Accept-Language", "*")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    public ArrayList<String> getFloorplanURLs(Document doc) {
        ArrayList<String> out = new ArrayList<>();
        Element greatDiv = doc.selectFirst("div.skylease");
        Element list = greatDiv.selectFirst("ul");
        Elements listElements = list.select("li");
        for (Element listElement : listElements) {
            out.add(listElement.selectFirst("a").attr("href"));
        }
        return out;
    }

    public ArrayList<Floorplan> getAvailabilities(Document doc, String url) {
        ArrayList<Floorplan> data = new ArrayList<>();
        Element list = doc.selectFirst("ul.skylease-unit-table__cell-more-dropdown-list");
        String name = doc.selectFirst("h1").text();
        if (list != null) {
            int i = 0;
            Floorplan fp = new Floorplan(name);
            Elements availabilities = list.select("li");
            for (Element availability : availabilities) {
                fp.setUrl(url);
                boolean second = false;
                for (Element span : availability.select("span")) {
                    String text = span.text();
                    if (second) {
                        switch (i) {
                            case 0:
                                fp.setNumber(text);
                                break;
                            case 1:
                                fp.setFloor(text);
                                break;
                            case 2:
                                fp.setPrice(text);
                                break;
                            case 3:
                                fp.setDeposit(text);
                                break;
                            case 4:
                                fp.setDate(text);
                                break;
                            default:
                                break;
                        }
                    } else {
                        second = true;
                    }

                }
                i++;
                if (i == 5) {
                    data.add(fp);
                    fp = new Floorplan(name);
                    i = 0;
                }
            }
        }
        return data;
    }

    public ArrayList<Floorplan> getAvailabilities() {
        ArrayList<Floorplan> floorplans = new ArrayList<>();
        Document mainPage = fetchDocument(mainURL);
        ArrayList<String> urls = getFloorplanURLs(mainPage);
        for (String url : urls) {
//            if (url.contains("sm-urban")) {
            System.out.println(url);
            Document tmpPage = fetchDocument(url);
            ArrayList<Floorplan> availabilities = getAvailabilities(tmpPage, url);
            floorplans.addAll(availabilities);
//            }
        }
        return floorplans;
    }
}
