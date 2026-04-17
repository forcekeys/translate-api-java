package com.forcekeys.translateapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TranslateAPI Java SDK
 * https://github.com/forcekeys/translate-api-java
 */
public class TranslateAPI {
    private static final String BASE_URL = "https://api.translate.forcekeys.com/api/v1";
    
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient client;
    
    public TranslateAPI(String apiKey) {
        this(apiKey, BASE_URL);
    }
    
    public TranslateAPI(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }
    
    private String request(String endpoint, String method, String body) throws Exception {
        String url = baseUrl + "/" + endpoint;
        
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json");
        
        if ("POST".equals(method) && body != null) {
            builder.POST(HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.GET();
        }
        
        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() >= 400) {
            throw new RuntimeException("API error: " + response.statusCode());
        }
        
        return response.body();
    }
    
    public TranslationResult translate(String text, String source, String target) throws Exception {
        String body = String.format(
            "{\"text\":\"%s\",\"source_lang\":\"%s\",\"target_lang\":\"%s\"}",
            text, source, target
        );
        
        String json = request("translate", "POST", body);
        return new TranslationResult(json);
    }
    
    public String detect(String text) throws Exception {
        String body = String.format("{\"text\":\"%s\"}", text);
        String json = request("detect", "POST", body);
        // Parse JSON to extract language
        return "fr"; // Simplified
    }
    
    public List<Language> languages() throws Exception {
        String json = request("languages", "GET", null);
        // Parse JSON to extract languages
        return List.of(); // Simplified
    }
    
    public AccountInfo account() throws Exception {
        String json = request("account", "GET", null);
        // Parse JSON to extract account info
        return new AccountInfo(); // Simplified
    }
}

class TranslationResult {
    private final String translatedText;
    private final String sourceLang;
    private final String targetLang;
    
    public TranslationResult(String json) {
        // Simplified JSON parsing
        this.translatedText = json;
        this.sourceLang = "auto";
        this.targetLang = "en";
    }
    
    public String getTranslatedText() { return translatedText; }
    public String getSourceLang() { return sourceLang; }
    public String getTargetLang() { return targetLang; }
}

class Language {
    private final String code;
    private final String name;
    
    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() { return code; }
    public String getName() { return name; }
}

class AccountInfo {
    private final String email;
    private final String plan;
    private final int credits;
    private final int monthlyUsage;
    
    public AccountInfo() {
        this.email = "";
        this.plan = "free";
        this.credits = 0;
        this.monthlyUsage = 0;
    }
    
    public String getEmail() { return email; }
    public String getPlan() { return plan; }
    public int getCredits() { return credits; }
    public int getMonthlyUsage() { return monthlyUsage; }
}