package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.service.OpenGraphService;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
@Log4j2
public class OpenGraphServiceImpl implements OpenGraphService {
    private final OkHttpClient client = new OkHttpClient();
    public Map<String, String> getCourseInfo(String url) {
        Map<String, String> ogData = new HashMap<>();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String html = response.body().string();
                    Document document = Jsoup.parse(html);
                    Elements metaTags = document.select("meta[property^=og:]");
                    for (Element metaTag : metaTags) {
                        String property = metaTag.attr("property");
                        String content = metaTag.attr("content");
                        String key = property.replace("og:", "");
                        ogData.put(key, content);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error fetching OpenGraph data for url: {}", url, e);
        }
        return ogData;
    }
}
