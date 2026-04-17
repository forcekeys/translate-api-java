package com.forcekeys.translateapi;

import java.util.List;
import java.util.Map;

/**
 * Language detection result
 * 
 * Represents the result of detecting the language of a text.
 * 
 * Example:
 * ```java
 * LanguageDetectionResult result = api.detectLanguage("Bonjour le monde");
 * System.out.println("Detected language: " + result.getLanguage());
 * System.out.println("Confidence: " + result.getConfidence() + "%");
 * ```
 */
public class LanguageDetectionResult {
    private final String language;
    private final String languageName;
    private final double confidence;
    private final List<Map<String, Object>> alternatives;
    
    /**
     * Constructor
     * 
     * @param language Detected language code (e.g., "fr")
     * @param languageName Detected language name (e.g., "French")
     * @param confidence Confidence score (0-100)
     * @param alternatives Alternative language candidates
     */
    public LanguageDetectionResult(String language, String languageName, double confidence, 
                                  List<Map<String, Object>> alternatives) {
        this.language = language;
        this.languageName = languageName;
        this.confidence = confidence;
        this.alternatives = alternatives;
    }
    
    /**
     * Get detected language code
     * 
     * @return Language code (e.g., "fr", "en", "es")
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * Get detected language name
     * 
     * @return Language name (e.g., "French", "English", "Spanish")
     */
    public String getLanguageName() {
        return languageName;
    }
    
    /**
     * Get confidence score
     * 
     * @return Confidence score (0-100)
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * Get alternative language candidates
     * 
     * @return List of alternative language candidates with confidence scores
     */
    public List<Map<String, Object>> getAlternatives() {
        return alternatives;
    }
    
    /**
     * Check if detection is confident (above 90%)
     * 
     * @return True if confidence is high
     */
    public boolean isConfident() {
        return confidence >= 90.0;
    }
    
    /**
     * Check if detection is uncertain (below 70%)
     * 
     * @return True if confidence is low
     */
    public boolean isUncertain() {
        return confidence < 70.0;
    }
    
    /**
     * Get the top alternative language
     * 
     * @return Top alternative language code or null if no alternatives
     */
    public String getTopAlternativeLanguage() {
        if (alternatives == null || alternatives.isEmpty()) {
            return null;
        }
        
        Map<String, Object> topAlt = alternatives.get(0);
        return (String) topAlt.get("language");
    }
    
    /**
     * Get the top alternative confidence
     * 
     * @return Top alternative confidence or 0 if no alternatives
     */
    public double getTopAlternativeConfidence() {
        if (alternatives == null || alternatives.isEmpty()) {
            return 0.0;
        }
        
        Map<String, Object> topAlt = alternatives.get(0);
        Object conf = topAlt.get("confidence");
        if (conf instanceof Number) {
            return ((Number) conf).doubleValue();
        }
        return 0.0;
    }
    
    /**
     * Get the difference between primary and top alternative confidence
     * 
     * @return Confidence difference
     */
    public double getConfidenceDifference() {
        double altConfidence = getTopAlternativeConfidence();
        return confidence - altConfidence;
    }
    
    /**
     * Check if the detection is ambiguous (difference < 10%)
     * 
     * @return True if detection is ambiguous
     */
    public boolean isAmbiguous() {
        return getConfidenceDifference() < 10.0;
    }
    
    @Override
    public String toString() {
        return "LanguageDetectionResult{" +
               "language='" + language + '\'' +
               ", languageName='" + languageName + '\'' +
               ", confidence=" + confidence +
               ", alternatives=" + (alternatives != null ? alternatives.size() : 0) +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        LanguageDetectionResult that = (LanguageDetectionResult) o;
        
        if (Double.compare(that.confidence, confidence) != 0) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (languageName != null ? !languageName.equals(that.languageName) : that.languageName != null) return false;
        return alternatives != null ? alternatives.equals(that.alternatives) : that.alternatives == null;
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = language != null ? language.hashCode() : 0;
        result = 31 * result + (languageName != null ? languageName.hashCode() : 0);
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (alternatives != null ? alternatives.hashCode() : 0);
        return result;
    }
}