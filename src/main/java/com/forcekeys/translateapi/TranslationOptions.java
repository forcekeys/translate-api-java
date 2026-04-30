package com.forcekeys.translateapi;

/**
 * Translation options for customizing translation behavior
 * 
 * This class provides options to customize how translations are performed,
 * including formality level, context preservation, and sentence splitting.
 * 
 * Example:
 * ```java
 * TranslationOptions options = new TranslationOptions()
 *     .setFormality(Formality.FORMAL)
 *     .setContext("This is a legal document")
 *     .setPreserveFormatting(true)
 *     .setSplitSentences(false);
 * ```
 */
public class TranslationOptions {
    private Formality formality;
    private String context;
    private boolean preserveFormatting;
    private boolean splitSentences;
    
    /**
     * Formality levels for translation
     */
    public enum Formality {
        FORMAL("formal"),
        INFORMAL("informal");
        
        private final String value;
        
        Formality(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    /**
     * Default constructor with default values
     */
    public TranslationOptions() {
        this.formality = null;
        this.context = null;
        this.preserveFormatting = false;
        this.splitSentences = true;
    }
    
    /**
     * Constructor with all parameters
     * 
     * @param formality Formality level (FORMAL or INFORMAL)
     * @param context Context for translation (optional)
     * @param preserveFormatting Whether to preserve formatting
     * @param splitSentences Whether to split sentences
     */
    public TranslationOptions(Formality formality, String context, 
                             boolean preserveFormatting, boolean splitSentences) {
        this.formality = formality;
        this.context = context;
        this.preserveFormatting = preserveFormatting;
        this.splitSentences = splitSentences;
    }
    
    /**
     * Get formality level
     * 
     * @return Formality level or null if not set
     */
    public Formality getFormality() {
        return formality;
    }
    
    /**
     * Set formality level
     * 
     * @param formality Formality level (FORMAL or INFORMAL)
     * @return This instance for method chaining
     */
    public TranslationOptions setFormality(Formality formality) {
        this.formality = formality;
        return this;
    }
    
    /**
     * Get context for translation
     * 
     * @return Context string or null if not set
     */
    public String getContext() {
        return context;
    }
    
    /**
     * Set context for translation
     * 
     * @param context Context string to help with translation accuracy
     * @return This instance for method chaining
     */
    public TranslationOptions setContext(String context) {
        this.context = context;
        return this;
    }
    
    /**
     * Check if formatting should be preserved
     * 
     * @return True if formatting should be preserved
     */
    public boolean isPreserveFormatting() {
        return preserveFormatting;
    }
    
    /**
     * Set whether to preserve formatting
     * 
     * @param preserveFormatting True to preserve formatting
     * @return This instance for method chaining
     */
    public TranslationOptions setPreserveFormatting(boolean preserveFormatting) {
        this.preserveFormatting = preserveFormatting;
        return this;
    }
    
    /**
     * Check if sentences should be split
     * 
     * @return True if sentences should be split
     */
    public boolean isSplitSentences() {
        return splitSentences;
    }
    
    /**
     * Set whether to split sentences
     * 
     * @param splitSentences True to split sentences
     * @return This instance for method chaining
     */
    public TranslationOptions setSplitSentences(boolean splitSentences) {
        this.splitSentences = splitSentences;
        return this;
    }
    
    /**
     * Convert options to query parameters string
     * 
     * @return Query parameters string or empty string if no options
     */
    public String toQueryString() {
        StringBuilder query = new StringBuilder();
        
        if (formality != null) {
            addParam(query, "formality", formality.getValue());
        }
        
        if (context != null && !context.trim().isEmpty()) {
            addParam(query, "context", context);
        }
        
        if (preserveFormatting) {
            addParam(query, "preserve_formatting", "true");
        }
        
        if (!splitSentences) {
            addParam(query, "split_sentences", "false");
        }
        
        return query.toString();
    }
    
    /**
     * Convert options to JSON string for request body
     * 
     * @return JSON string representation
     */
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        if (formality != null) {
            if (!first) json.append(",");
            json.append("\"formality\":\"").append(formality.getValue()).append("\"");
            first = false;
        }
        
        if (context != null && !context.trim().isEmpty()) {
            if (!first) json.append(",");
            json.append("\"context\":\"").append(escapeJson(context)).append("\"");
            first = false;
        }
        
        if (preserveFormatting) {
            if (!first) json.append(",");
            json.append("\"preserve_formatting\":true");
            first = false;
        }
        
        if (!splitSentences) {
            if (!first) json.append(",");
            json.append("\"split_sentences\":false");
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Add parameter to query string
     */
    private void addParam(StringBuilder query, String key, String value) {
        if (query.length() > 0) {
            query.append("&");
        }
        query.append(key).append("=").append(encodeUrl(value));
    }
    
    /**
     * URL encode a value
     */
    private String encodeUrl(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return value;
        }
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
    
    @Override
    public String toString() {
        return "TranslationOptions{" +
               "formality=" + formality +
               ", context='" + context + '\'' +
               ", preserveFormatting=" + preserveFormatting +
               ", splitSentences=" + splitSentences +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TranslationOptions that = (TranslationOptions) o;
        
        if (preserveFormatting != that.preserveFormatting) return false;
        if (splitSentences != that.splitSentences) return false;
        if (formality != that.formality) return false;
        return context != null ? context.equals(that.context) : that.context == null;
    }
    
    @Override
    public int hashCode() {
        int result = formality != null ? formality.hashCode() : 0;
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (preserveFormatting ? 1 : 0);
        result = 31 * result + (splitSentences ? 1 : 0);
        return result;
    }
}
