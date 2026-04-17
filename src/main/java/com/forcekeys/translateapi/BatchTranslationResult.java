package com.forcekeys.translateapi;

import java.util.List;
import java.util.Map;

/**
 * Batch translation result
 * 
 * Represents the result of translating multiple texts in a single request.
 * 
 * Example:
 * ```java
 * List<String> texts = Arrays.asList("Hello", "Goodbye", "Thank you");
 * Map<String, String> result = api.batchTranslate(texts, "fr", "en");
 * for (Map.Entry<String, String> entry : result.entrySet()) {
 *     System.out.println(entry.getKey() + " => " + entry.getValue());
 * }
 * ```
 */
public class BatchTranslationResult {
    private final List<Map<String, String>> translations;
    private final int charactersUsed;
    private final int processingTimeMs;
    
    /**
     * Constructor
     * 
     * @param translations List of translation pairs (original -> translated)
     * @param charactersUsed Total characters used in translation
     * @param processingTimeMs Total processing time in milliseconds
     */
    public BatchTranslationResult(List<Map<String, String>> translations, int charactersUsed, int processingTimeMs) {
        this.translations = translations;
        this.charactersUsed = charactersUsed;
        this.processingTimeMs = processingTimeMs;
    }
    
    /**
     * Get translation pairs
     * 
     * @return List of maps containing original and translated text pairs
     */
    public List<Map<String, String>> getTranslations() {
        return translations;
    }
    
    /**
     * Get total characters used
     * 
     * @return Total number of characters translated
     */
    public int getCharactersUsed() {
        return charactersUsed;
    }
    
    /**
     * Get processing time
     * 
     * @return Total processing time in milliseconds
     */
    public int getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    /**
     * Get number of translations
     * 
     * @return Number of texts translated
     */
    public int getCount() {
        return translations != null ? translations.size() : 0;
    }
    
    /**
     * Get average characters per translation
     * 
     * @return Average characters per translation or 0 if no translations
     */
    public double getAverageCharactersPerTranslation() {
        int count = getCount();
        if (count == 0) {
            return 0.0;
        }
        return (double) charactersUsed / count;
    }
    
    /**
     * Get average processing time per translation
     * 
     * @return Average processing time per translation in milliseconds
     */
    public double getAverageProcessingTimePerTranslation() {
        int count = getCount();
        if (count == 0) {
            return 0.0;
        }
        return (double) processingTimeMs / count;
    }
    
    /**
     * Get translation for a specific original text
     * 
     * @param originalText Original text to find
     * @return Translated text or null if not found
     */
    public String getTranslationFor(String originalText) {
        if (translations == null || originalText == null) {
            return null;
        }
        
        for (Map<String, String> translation : translations) {
            String original = translation.get("original");
            if (originalText.equals(original)) {
                return translation.get("translated");
            }
        }
        
        return null;
    }
    
    /**
     * Check if batch contains a specific original text
     * 
     * @param originalText Original text to check
     * @return True if batch contains the text
     */
    public boolean containsOriginalText(String originalText) {
        return getTranslationFor(originalText) != null;
    }
    
    /**
     * Get all original texts
     * 
     * @return Array of all original texts
     */
    public String[] getAllOriginalTexts() {
        if (translations == null) {
            return new String[0];
        }
        
        String[] originals = new String[translations.size()];
        for (int i = 0; i < translations.size(); i++) {
            originals[i] = translations.get(i).get("original");
        }
        return originals;
    }
    
    /**
     * Get all translated texts
     * 
     * @return Array of all translated texts
     */
    public String[] getAllTranslatedTexts() {
        if (translations == null) {
            return new String[0];
        }
        
        String[] translated = new String[translations.size()];
        for (int i = 0; i < translations.size(); i++) {
            translated[i] = translations.get(i).get("translated");
        }
        return translated;
    }
    
    /**
     * Convert to simple map (original -> translated)
     * 
     * @return Map of original to translated text
     */
    public Map<String, String> toMap() {
        java.util.HashMap<String, String> map = new java.util.HashMap<>();
        
        if (translations != null) {
            for (Map<String, String> translation : translations) {
                String original = translation.get("original");
                String translated = translation.get("translated");
                if (original != null && translated != null) {
                    map.put(original, translated);
                }
            }
        }
        
        return map;
    }
    
    @Override
    public String toString() {
        return "BatchTranslationResult{" +
               "translations=" + getCount() +
               ", charactersUsed=" + charactersUsed +
               ", processingTimeMs=" + processingTimeMs +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BatchTranslationResult that = (BatchTranslationResult) o;
        
        if (charactersUsed != that.charactersUsed) return false;
        if (processingTimeMs != that.processingTimeMs) return false;
        return translations != null ? translations.equals(that.translations) : that.translations == null;
    }
    
    @Override
    public int hashCode() {
        int result = translations != null ? translations.hashCode() : 0;
        result = 31 * result + charactersUsed;
        result = 31 * result + processingTimeMs;
        return result;
    }
}