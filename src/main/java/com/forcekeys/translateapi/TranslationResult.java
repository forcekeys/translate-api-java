package com.forcekeys.translateapi;

/**
 * Translation result
 */
public class TranslationResult {
    private final String translatedText;
    private final String sourceLang;
    private final String targetLang;
    private final String detectedLang;
    private final int charactersUsed;
    private final int processingTimeMs;
    
    public TranslationResult(String translatedText, String sourceLang, String targetLang, 
                            String detectedLang, int charactersUsed, int processingTimeMs) {
        this.translatedText = translatedText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.detectedLang = detectedLang;
        this.charactersUsed = charactersUsed;
        this.processingTimeMs = processingTimeMs;
    }
    
    public String getTranslatedText() { return translatedText; }
    public String getSourceLang() { return sourceLang; }
    public String getTargetLang() { return targetLang; }
    public String getDetectedLang() { return detectedLang; }
    public int getCharactersUsed() { return charactersUsed; }
    public int getProcessingTimeMs() { return processingTimeMs; }
}
