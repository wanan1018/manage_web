package com.icinfo.task.controller;

import com.icinfo.task.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private static final String QQ_APPKEY = "27d3f9c8-5379-4585-9e32-c7b1822dbd44";
    private static final String QQ_BASE = "https://r.inews.qq.com/web_feed/getWebList";
    private static final String HN_BASE = "https://hacker-news.firebaseio.com/v0";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public Result<List<Map<String, Object>>> list(@RequestParam(defaultValue = "") String q) {
        List<Map<String, Object>> articles = fetchFromQQ(q);
        if (articles.isEmpty()) {
            articles = fetchFromHackerNews();
        }
        if (!q.isEmpty()) {
            String keyword = q.toLowerCase();
            articles = articles.stream()
                    .filter(a -> {
                        String title = (String) a.getOrDefault("title", "");
                        String desc = (String) a.getOrDefault("description", "");
                        return title.toLowerCase().contains(keyword) || desc.toLowerCase().contains(keyword);
                    })
                    .collect(Collectors.toList());
        }
        return Result.success(articles);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> fetchFromQQ(String keyword) {
        try {
            String channel = keyword.isEmpty() ? "news_news_tech" : "news_news_home";
            String url = QQ_BASE + "?chlid=" + channel + "&from=pc&size=10&appkey=" + QQ_APPKEY;
            Map<String, Object> resp = restTemplate.getForObject(url, Map.class);
            if (resp != null && resp.get("data") != null) {
                return (List<Map<String, Object>>) resp.get("data");
            }
        } catch (Exception ignored) {
            // QQ API unavailable, fall through to HackerNews
        }
        return List.of();
    }

    private List<Map<String, Object>> fetchFromHackerNews() {
        try {
            List<Integer> ids = restTemplate.getForObject(HN_BASE + "/topstories.json", List.class);
            if (ids == null || ids.isEmpty()) return List.of();

            List<Map<String, Object>> articles = new ArrayList<>();
            int limit = Math.min(ids.size(), 10);
            for (int i = 0; i < limit; i++) {
                try {
                    String itemUrl = HN_BASE + "/item/" + ids.get(i) + ".json";
                    Map<String, Object> item = restTemplate.getForObject(itemUrl, Map.class);
                    if (item != null && "story".equals(item.get("type"))) {
                        Map<String, Object> article = new LinkedHashMap<>();
                        article.put("title", item.getOrDefault("title", ""));
                        article.put("url", item.getOrDefault("url", "https://news.ycombinator.com/item?id=" + item.get("id")));
                        article.put("description", "");
                        article.put("publishedAt", formatUnixTime((Number) item.get("time")));
                        Map<String, Object> source = new LinkedHashMap<>();
                        source.put("name", "HackerNews");
                        article.put("source", source);
                        articles.add(article);
                    }
                } catch (Exception ignored) {
                }
            }
            return articles;
        } catch (Exception e) {
            return List.of();
        }
    }

    private String formatUnixTime(Number time) {
        if (time == null) return "";
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date(time.longValue() * 1000));
    }
}
