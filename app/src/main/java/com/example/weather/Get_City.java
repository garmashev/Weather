package com.example.weather;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Get_City {
    List<String> cities = new ArrayList<>();
   public void Get_Country(String country) throws IOException {
        Document doc = Jsoup.connect("https://pogoda.yandex.ru/static/cities.xml").get();
        Elements links = doc.getElementsByTag("country");
        for (Element link : links)
        if (link.attr("name").equals(country))
          cities.add(link.text());
    }
    }
