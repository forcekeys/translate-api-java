package com.forcekeys.translateapi;

/**
 * OCR (Optical Character Recognition) result
 * 
 * Represents the result of extracting text from an image using OCR.
 * 
 * Example:
 * ```java
 * OCRResult result = api.ocr("image.png");
 * System.out.println("Extracted text: " + result.getText());
 * System.out.println("Confidence: " + result.getConfidence() + "%");
 * ```
 */
public class OCRResult {
    private final String text;
    private final double confidence;
    private final String languageDetected;
    private final int processingTimeMs;
    
    /**
     * Constructor
     * 
     * @param text Extracted text from image
     * @param confidence Confidence score (0-100)
     * @param languageDetected Detected language code
     * @param processingTimeMs Processing time in milliseconds
     */
    public OCRResult(String text, double confidence, String languageDetected, int processingTimeMs) {
        this.text = text;
        this.confidence = confidence;
        this.languageDetected = languageDetected;
        this.processingTimeMs = processingTimeMs;
    }
    
    /**
     * Get extracted text
     * 
     * @return Extracted text from image
     */
    public String getText() {
        return text;
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
     * Get detected language
     * 
     * @return Detected language code
     */
    public String getLanguageDetected() {
        return languageDetected;
    }
    
    /**
     * Get processing time
     * 
     * @return Processing time in milliseconds
     */
    public int getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    /**
     * Check if confidence is high (above 90%)
     * 
     * @return True if confidence is high
     */
    public boolean isHighConfidence() {
        return confidence >= 90.0;
    }
    
    /**
     * Check if confidence is medium (50-90%)
     * 
     * @return True if confidence is medium
     */
    public boolean isMediumConfidence() {
        return confidence >= 50.0 && confidence < 90.0;
    }
    
    /**
     * Check if confidence is low (below 50%)
     * 
     * @return True if confidence is low
     */
    public boolean isLowConfidence() {
        return confidence < 50.0;
    }
    
    @Override
    public String toString() {
        return "OCRResult{" +
               "text='" + (text != null ? text.substring(0, Math.min(text.length(), 50)) + "..." : "null") + '\'' +
               ", confidence=" + confidence +
               ", languageDetected='" + languageDetected + '\'' +
               ", processingTimeMs=" + processingTimeMs +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        OCRResult ocrResult = (OCRResult) o;
        
        if (Double.compare(ocrResult.confidence, confidence) != 0) return false;
        if (processingTimeMs != ocrResult.processingTimeMs) return false;
        if (text != null ? !text.equals(ocrResult.text) : ocrResult.text != null) return false;
        return languageDetected != null ? languageDetected.equals(ocrResult.languageDetected) : ocrResult.languageDetected == null;
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = text != null ? text.hashCode() : 0;
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (languageDetected != null ? languageDetected.hashCode() : 0);
        result = 31 * result + processingTimeMs;
        return result;
    }
}