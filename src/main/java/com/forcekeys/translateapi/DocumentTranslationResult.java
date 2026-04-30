package com.forcekeys.translateapi;

/**
 * Document translation result
 */
public class DocumentTranslationResult {
    private final String translatedText;
    private final String sourceLang;
    private final String targetLang;
    private final int pages;
    private final int charactersUsed;
    private final int processingTimeMs;
    
    public DocumentTranslationResult(String translatedText, String sourceLang, String targetLang, 
                                    int pages, int charactersUsed, int processingTimeMs) {
        this.translatedText = translatedText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.pages = pages;
        this.charactersUsed = charactersUsed;
        this.processingTimeMs = processingTimeMs;
    }
    
    public String getTranslatedText() { return translatedText; }
    public String getSourceLang() { return sourceLang; }
    public String getTargetLang() { return targetLang; }
    public int getPages() { return pages; }
    public int getCharactersUsed() { return charactersUsed; }
    public int getProcessingTimeMs() { return processingTimeMs; }
}
