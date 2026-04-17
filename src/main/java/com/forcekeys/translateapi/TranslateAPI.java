package com.forcekeys.translateapi;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;

/**
 * TranslateAPI Java SDK
 * Official Java client for the TranslateAPI translation service.
 * https://github.com/forcekeys/translate-api-java
 * 
 * Example:
 * ```java
 * TranslateAPI api = new TranslateAPI("your_api_key");
 * TranslationResult result = api.translate("Hello, world!", "en", "fr");
 * System.out.println(result.getTranslatedText());
 * ```
 */
public class TranslateAPI {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    
    private static final String DEFAULT_BASE_URL = "https://api.translate.forcekeys.com/api/v1";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    
    /**
     * Initialize TranslateAPI client with default settings
     * 
     * @param apiKey Your API key
     */
    public TranslateAPI(String apiKey) {
        this(apiKey, DEFAULT_BASE_URL, DEFAULT_TIMEOUT);
    }
    
    /**
     * Initialize TranslateAPI client with custom settings
     * 
     * @param apiKey Your API key
     * @param baseUrl API base URL
     * @param timeout Request timeout
     */
    public TranslateAPI(String apiKey, String baseUrl, Duration timeout) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key is required");
        }
        
        this.apiKey = apiKey.trim();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(timeout)
            .build();
    }
    
    /**
     * Make API request
     * 
     * @param endpoint API endpoint
     * @param method HTTP method
     * @param body Request body (can be null)
     * @return API response as JSON object
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    private Map<String, Object> makeRequest(String endpoint, String method, String body) 
            throws APIException, IOException {
        String url = baseUrl + "/" + endpoint;
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + apiKey)
            .header("User-Agent", "TranslateAPI-Java/1.0.0");
        
        if (method.equals("POST") && body != null) {
            requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json");
        } else if (method.equals("GET")) {
            requestBuilder.GET();
        }
        
        HttpRequest request = requestBuilder.build();
        
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Parse JSON response
            Map<String, Object> result = parseJson(response.body());
            
            // Check for errors
            if (response.statusCode() >= 400) {
                String errorCode = (String) result.getOrDefault("code", "http_error");
                String errorMsg = (String) result.getOrDefault("message", "HTTP error: " + response.statusCode());
                Integer retryAfter = (Integer) result.get("retry_after");
                throw new APIException(errorMsg, errorCode, response.statusCode(), retryAfter);
            }
            
            if ("error".equals(result.get("status"))) {
                String errorCode = (String) result.getOrDefault("code", "api_error");
                String errorMsg = (String) result.getOrDefault("message", "Unknown API error");
                Integer retryAfter = (Integer) result.get("retry_after");
                throw new APIException(errorMsg, errorCode, response.statusCode(), retryAfter);
            }
            
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
    
    /**
     * Make multipart file upload request
     * 
     * @param endpoint API endpoint
     * @param formData Form data
     * @param fileField File field name
     * @param filePath Path to file
     * @return API response as JSON object
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    private Map<String, Object> makeMultipartRequest(String endpoint, Map<String, String> formData, 
            String fileField, Path filePath) throws APIException, IOException {
        String url = baseUrl + "/" + endpoint;
        
        // Create multipart body
        String boundary = "----FormBoundary" + System.currentTimeMillis();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, "UTF-8"), true);
        
        // Add form data
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").append("\r\n");
            writer.append("\r\n");
            writer.append(entry.getValue()).append("\r\n");
        }
        
        // Add file
        String fileName = filePath.getFileName().toString();
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        
        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"" + fileField + "\"; filename=\"" + fileName + "\"").append("\r\n");
        writer.append("Content-Type: " + mimeType).append("\r\n");
        writer.append("\r\n");
        writer.flush();
        
        // Write file content
        Files.copy(filePath, baos);
        writer.append("\r\n");
        writer.append("--" + boundary + "--").append("\r\n");
        writer.flush();
        
        byte[] body = baos.toByteArray();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .header("User-Agent", "TranslateAPI-Java/1.0.0")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();
        
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Parse JSON response
            Map<String, Object> result = parseJson(response.body());
            
            // Check for errors
            if (response.statusCode() >= 400) {
                String errorCode = (String) result.getOrDefault("code", "http_error");
                String errorMsg = (String) result.getOrDefault("message", "HTTP error: " + response.statusCode());
                Integer retryAfter = (Integer) result.get("retry_after");
                throw new APIException(errorMsg, errorCode, response.statusCode(), retryAfter);
            }
            
            if ("error".equals(result.get("status"))) {
                String errorCode = (String) result.getOrDefault("code", "api_error");
                String errorMsg = (String) result.getOrDefault("message", "Unknown API error");
                Integer retryAfter = (Integer) result.get("retry_after");
                throw new APIException(errorMsg, errorCode, response.statusCode(), retryAfter);
            }
            
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
    
    /**
     * Parse JSON string to Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        // Simple JSON parser for basic structures
        // In a real implementation, use a proper JSON library like Jackson or Gson
        try {
            // This is a simplified parser - in production use a proper JSON library
            if (json == null || json.trim().isEmpty()) {
                return new HashMap<>();
            }
            
            // Remove whitespace
            json = json.trim();
            
            if (json.startsWith("{") && json.endsWith("}")) {
                Map<String, Object> map = new HashMap<>();
                String content = json.substring(1, json.length() - 1).trim();
                
                if (!content.isEmpty()) {
                    String[] pairs = content.split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":", 2);
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                            String value = keyValue[1].trim();
                            
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                map.put(key, value.substring(1, value.length() - 1));
                            } else if (value.equals("true") || value.equals("false")) {
                                map.put(key, Boolean.parseBoolean(value));
                            } else if (value.matches("-?\\d+")) {
                                map.put(key, Integer.parseInt(value));
                            } else if (value.matches("-?\\d+\\.\\d+")) {
                                map.put(key, Double.parseDouble(value));
                            } else if (value.equals("null")) {
                                map.put(key, null);
                            } else {
                                map.put(key, value);
                            }
                        }
                    }
                }
                return map;
            }
        } catch (Exception e) {
            // Fallback to empty map
        }
        return new HashMap<>();
    }
    
    /**
     * Translate text
     * 
     * @param text Text to translate
     * @param targetLang Target language code (e.g., "fr")
     * @param sourceLang Source language code (optional, auto-detect if null)
     * @param formality Formality level: "formal" or "informal" (optional)
     * @return Translation result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public TranslationResult translate(String text, String targetLang, String sourceLang, String formality) 
            throws APIException, IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("text", text);
        request.put("target_lang", targetLang);
        
        if (sourceLang != null) {
            request.put("source_lang", sourceLang);
        }
        
        if (formality != null) {
            request.put("formality", formality);
        }
        
        String body = "{\"text\":\"" + escapeJson(text) + "\",\"target_lang\":\"" + targetLang + "\"" +
                     (sourceLang != null ? ",\"source_lang\":\"" + sourceLang + "\"" : "") +
                     (formality != null ? ",\"formality\":\"" + formality + "\"" : "") + "}";
        
        Map<String, Object> response = makeRequest("translate", "POST", body);
        
        return new TranslationResult(
            (String) response.get("translated_text"),
            (String) response.get("source_lang"),
            (String) response.get("target_lang"),
            (String) response.get("detected_lang"),
            ((Number) response.getOrDefault("characters_used", 0)).intValue(),
            ((Number) response.getOrDefault("processing_time_ms", 0)).intValue()
        );
    }
    
    /**
     * Translate text (simplified version)
     * 
     * @param text Text to translate
     * @param targetLang Target language code
     * @return Translation result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public TranslationResult translate(String text, String targetLang) throws APIException, IOException {
        return translate(text, targetLang, null, null);
    }
    
    /**
     * Translate document
     * 
     * @param filePath Path to document file (PDF, DOCX, TXT)
     * @param targetLang Target language code
     * @param sourceLang Source language code (optional)
     * @return Document translation result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public DocumentTranslationResult translateDocument(String filePath, String targetLang, String sourceLang) 
            throws APIException, IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        Map<String, String> formData = new HashMap<>();
        formData.put("target_lang", targetLang);
        
        if (sourceLang != null) {
            formData.put("source_lang", sourceLang);
        }
        
        Map<String, Object> response = makeMultipartRequest("translate/document", formData, "file", path);
        
        return new DocumentTranslationResult(
            (String) response.get("translated_text"),
            (String) response.get("source_lang"),
            (String) response.get("target_lang"),
            ((Number) response.getOrDefault("pages", 0)).intValue(),
            ((Number) response.getOrDefault("characters_used", 0)).intValue(),
            ((Number) response.getOrDefault("processing_time_ms", 0)).intValue()
        );
    }
    
    /**
     * Translate document (simplified version)
     * 
     * @param filePath Path to document file
     * @param targetLang Target language code
     * @return Document translation result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public DocumentTranslationResult translateDocument(String filePath, String targetLang) 
            throws APIException, IOException {
        return translateDocument(filePath, targetLang, null);
    }
    
    /**
     * Extract text from image (OCR)
     * 
     * @param filePath Path to image file (PNG, JPG, WEBP, BMP)
     * @param lang Expected language (optional, improves accuracy)
     * @param enhance Apply image enhancement (optional)
     * @return OCR result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public OCRResult ocr(String filePath, String lang, boolean enhance) throws APIException, IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        Map<String, String> formData = new HashMap<>();
        
        if (lang != null) {
            formData.put("lang", lang);
        }
        
        if (enhance) {
            formData.put("enhance", "true");
        }
        
        Map<String, Object> response = makeMultipartRequest("ocr", formData, "image", path);
        
        return new OCRResult(
            (String) response.get("text"),
            ((Number) response.getOrDefault("confidence", 0.0)).doubleValue(),
            (String) response.getOrDefault("language_detected", ""),
            ((Number) response.getOrDefault("processing_time_ms", 0)).intValue()
        );
    }
    
    /**
     * OCR (simplified version)
     * 
     * @param filePath Path to image file
     * @return OCR result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public OCRResult ocr(String filePath) throws APIException, IOException {
        return ocr(filePath, null, false);
    }
    
    /**
     * Detect language of text
     * 
     * @param text Text to analyze
     * @return Language detection result
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public LanguageDetectionResult detectLanguage(String text) throws APIException, IOException {
        String body = "{\"text\":\"" + escapeJson(text) + "\"}";
        Map<String, Object> response = makeRequest("detect", "POST", body);
        
        List<Map<String, Object>> alternatives = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> altList = (List<Map<String, Object>>) response.getOrDefault("alternatives", new ArrayList<>());
        for (Map<String, Object> alt : altList) {
            Map<String, Object> alternative = new HashMap<>();
            alternative.put("language", alt.get("language"));
            alternative.put("confidence", alt.get("confidence"));
            alternative.put("language_name", alt.getOrDefault("language_name", ""));
            alternatives.add(alternative);
        }
        
        return new LanguageDetectionResult(
            (String) response.get("language"),
            (String) response.getOrDefault("language_name", ""),
            ((Number) response.getOrDefault("confidence", 0.0)).doubleValue(),
            alternatives
        );
    }
    
    /**
     * Get supported languages
     * 
     * @return List of supported languages
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public List<Language> getSupportedLanguages() throws APIException, IOException {
        Map<String, Object> response = makeRequest("languages", "GET", null);
        
        List<Language> languages = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> langList = (List<Map<String, Object>>) response.getOrDefault("languages", new ArrayList<>());
        for (Map<String, Object> lang : langList) {
            languages.add(new Language(
                (String) lang.get("code"),
                (String) lang.get("name"),
                (String) lang.getOrDefault("flag", "")
            ));
        }
        
        return languages;
    }
    
    /**
     * Batch translate multiple texts
     * 
     * @param texts Array of texts to translate
     * @param targetLang Target language code
     * @param sourceLang Source language code (optional)
     * @return Map of original text to translated text
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public Map<String, String> batchTranslate(List<String> texts, String targetLang, String sourceLang) 
            throws APIException, IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"texts\":[");
        
        for (int i = 0; i < texts.size(); i++) {
            if (i > 0) jsonBuilder.append(",");
            jsonBuilder.append("\"").append(escapeJson(texts.get(i))).append("\"");
        }
        
        jsonBuilder.append("],\"target_lang\":\"").append(targetLang).append("\"");
        
        if (sourceLang != null && !sourceLang.trim().isEmpty()) {
            jsonBuilder.append(",\"source_lang\":\"").append(sourceLang).append("\"");
        }
        
        jsonBuilder.append("}");
        
        Map<String, Object> response = makeRequest("translate/batch", "POST", jsonBuilder.toString());
        
        Map<String, String> result = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> translations = (List<Map<String, Object>>) response.getOrDefault("translations", new ArrayList<>());
        
        for (Map<String, Object> translation : translations) {
            String original = (String) translation.get("original");
            String translated = (String) translation.get("translated");
            result.put(original, translated);
        }
        
        return result;
    }
    
    /**
     * Get account information
     * 
     * @return Account information as Map
     * @throws APIException If API returns an error
     * @throws IOException If network error occurs
     */
    public Map<String, Object> getAccountInfo() throws APIException, IOException {
        Map<String, Object> response = makeRequest("account", "GET", null);
        @SuppressWarnings("unchecked")
        Map<String, Object> account = (Map<String, Object>) response.getOrDefault("account", new HashMap<>());
        return account;
    }
    
    /**
     * Escape JSON string
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
