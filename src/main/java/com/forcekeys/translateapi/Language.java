package com.forcekeys.translateapi;

/**
 * Language information
 * 
 * Represents information about a supported language.
 * 
 * Example:
 * ```java
 * List<Language> languages = api.getSupportedLanguages();
 * for (Language lang : languages) {
 *     System.out.println(lang.getCode() + ": " + lang.getName() + " " + lang.getFlag());
 * }
 * ```
 */
public class Language {
    private final String code;
    private final String name;
    private final String nativeName;
    private final String flag;
    private final boolean supportsFormality;
    
    /**
     * Constructor
     * 
     * @param code Language code (e.g., "en", "fr", "es")
     * @param name Language name (e.g., "English", "French", "Spanish")
     * @param nativeName Native language name (e.g., "English", "Français", "Español")
     * @param flag Language flag emoji (e.g., "🇺🇸", "🇫🇷", "🇪🇸")
     * @param supportsFormality Whether the language supports formality levels
     */
    public Language(String code, String name, String nativeName, String flag, boolean supportsFormality) {
        this.code = code;
        this.name = name;
        this.nativeName = nativeName;
        this.flag = flag;
        this.supportsFormality = supportsFormality;
    }
    
    /**
     * Constructor with minimal parameters
     * 
     * @param code Language code
     * @param name Language name
     * @param nativeName Native language name
     */
    public Language(String code, String name, String nativeName) {
        this(code, name, nativeName, "", false);
    }
    
    /**
     * Constructor without native name
     * 
     * @param code Language code
     * @param name Language name
     */
    public Language(String code, String name) {
        this(code, name, name, "", false);
    }
    
    /**
     * Get language code
     * 
     * @return ISO 639-1 language code (e.g., "en", "fr", "es")
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get language name
     * 
     * @return Language name in English
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get native language name
     * 
     * @return Language name in its native script
     */
    public String getNativeName() {
        return nativeName;
    }
    
    /**
     * Get language flag
     * 
     * @return Flag emoji for the language
     */
    public String getFlag() {
        return flag;
    }
    
    /**
     * Check if language has a flag
     * 
     * @return True if language has a flag emoji
     */
    public boolean hasFlag() {
        return flag != null && !flag.trim().isEmpty();
    }
    
    /**
     * Check if language supports formality levels
     * 
     * @return True if language supports formal/informal translation
     */
    public boolean supportsFormality() {
        return supportsFormality;
    }
    
    /**
     * Get display name with flag (if available)
     * 
     * @return Display name with flag prefix
     */
    public String getDisplayName() {
        if (hasFlag()) {
            return flag + " " + name;
        }
        return name;
    }
    
    /**
     * Get full display including code
     * 
     * @return Full display string with code, flag and name
     */
    public String getFullDisplay() {
        if (hasFlag()) {
            return code + " " + flag + " " + name;
        }
        return code + " " + name;
    }
    
    /**
     * Check if this is English language
     * 
     * @return True if language code is "en"
     */
    public boolean isEnglish() {
        return "en".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is French language
     * 
     * @return True if language code is "fr"
     */
    public boolean isFrench() {
        return "fr".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is Spanish language
     * 
     * @return True if language code is "es"
     */
    public boolean isSpanish() {
        return "es".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is German language
     * 
     * @return True if language code is "de"
     */
    public boolean isGerman() {
        return "de".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is Chinese language
     * 
     * @return True if language code is "zh"
     */
    public boolean isChinese() {
        return "zh".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is Arabic language
     * 
     * @return True if language code is "ar"
     */
    public boolean isArabic() {
        return "ar".equalsIgnoreCase(code);
    }
    
    /**
     * Check if this is a Latin script language
     * 
     * @return True if language uses Latin script
     */
    public boolean isLatinScript() {
        // Common Latin script languages
        String[] latinLanguages = {"en", "fr", "es", "de", "it", "pt", "nl", "sv", "no", "da", "fi", "pl", "ro", "hu", "cs", "sk", "sl", "hr", "bs", "sr", "mk", "bg", "ru", "uk", "be"};
        for (String lang : latinLanguages) {
            if (lang.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if this is a Cyrillic script language
     * 
     * @return True if language uses Cyrillic script
     */
    public boolean isCyrillicScript() {
        String[] cyrillicLanguages = {"ru", "uk", "be", "bg", "mk", "sr", "kk", "ky", "mn"};
        for (String lang : cyrillicLanguages) {
            if (lang.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if this is an Arabic script language
     * 
     * @return True if language uses Arabic script
     */
    public boolean isArabicScript() {
        String[] arabicScriptLanguages = {"ar", "fa", "ur", "ps", "sd", "ku"};
        for (String lang : arabicScriptLanguages) {
            if (lang.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if this is a CJK (Chinese, Japanese, Korean) language
     * 
     * @return True if language is Chinese, Japanese, or Korean
     */
    public boolean isCJK() {
        String[] cjkLanguages = {"zh", "ja", "ko"};
        for (String lang : cjkLanguages) {
            if (lang.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get language family
     * 
     * @return Language family (e.g., "Germanic", "Romance", "Slavic")
     */
    public String getLanguageFamily() {
        // Germanic languages
        String[] germanic = {"en", "de", "nl", "sv", "no", "da", "is", "fo", "af"};
        for (String lang : germanic) {
            if (lang.equalsIgnoreCase(code)) {
                return "Germanic";
            }
        }
        
        // Romance languages
        String[] romance = {"fr", "es", "it", "pt", "ro", "ca", "gl", "oc", "rm"};
        for (String lang : romance) {
            if (lang.equalsIgnoreCase(code)) {
                return "Romance";
            }
        }
        
        // Slavic languages
        String[] slavic = {"ru", "uk", "pl", "cs", "sk", "sl", "hr", "bs", "sr", "bg", "mk"};
        for (String lang : slavic) {
            if (lang.equalsIgnoreCase(code)) {
                return "Slavic";
            }
        }
        
        // Turkic languages
        String[] turkic = {"tr", "az", "kk", "ky", "uz", "tk", "ug"};
        for (String lang : turkic) {
            if (lang.equalsIgnoreCase(code)) {
                return "Turkic";
            }
        }
        
        // Semitic languages
        String[] semitic = {"ar", "he", "am", "ti"};
        for (String lang : semitic) {
            if (lang.equalsIgnoreCase(code)) {
                return "Semitic";
            }
        }
        
        return "Other";
    }
    
    @Override
    public String toString() {
        return "Language{" +
               "code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", nativeName='" + nativeName + '\'' +
               ", flag='" + flag + '\'' +
               ", supportsFormality=" + supportsFormality +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Language language = (Language) o;
        
        if (supportsFormality != language.supportsFormality) return false;
        if (code != null ? !code.equals(language.code) : language.code != null) return false;
        if (name != null ? !name.equals(language.name) : language.name != null) return false;
        if (nativeName != null ? !nativeName.equals(language.nativeName) : language.nativeName != null) return false;
        return flag != null ? flag.equals(language.flag) : language.flag == null;
    }
    
    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nativeName != null ? nativeName.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        result = 31 * result + (supportsFormality ? 1 : 0);
        return result;
    }
}
