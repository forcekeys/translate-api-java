# TranslateAPI Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.forcekeys/translate-api.svg)](https://search.maven.org/artifact/com.forcekeys/translate-api)
[![Java Version](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/technologies/downloads/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Documentation](https://img.shields.io/badge/docs-deeptranslate.online-blue.svg)](https://deeptranslate.online/docs)

Official Java client library for the TranslateAPI translation service. Translate text, documents, and images between 70+ languages with a simple, intuitive interface.

## Features

- **Text Translation**: Translate text between 70+ languages
- **Document Translation**: Support for PDF, DOCX, TXT files
- **Image OCR**: Extract and translate text from images
- **Language Detection**: Automatically detect language of text
- **Batch Translation**: Translate multiple texts in a single request
- **Account Management**: Check usage, credits, and account info
- **Android Compatible**: Works on Android and JVM
- **Async Support**: Both synchronous and asynchronous APIs
- **HTTP/2 Ready**: Built on modern Java HTTP client

## Installation

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.forcekeys</groupId>
    <artifactId>translate-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.forcekeys:translate-api:1.0.0'
```

### Manual Installation

```bash
git clone https://github.com/forcekeys/translate-api-java.git
cd translate-api-java
mvn install
```

## Quick Start

### 1. Get Your API Key

First, sign up at [deeptranslate.online](https://deeptranslate.online) to get your free API key.

### 2. Basic Usage

```java
import com.forcekeys.translateapi.TranslateAPI;
import com.forcekeys.translateapi.TranslationResult;

public class Example {
    public static void main(String[] args) {
        // Initialize with your API key
        TranslateAPI api = new TranslateAPI("your_api_key_here");
        
        // Translate text
        TranslationResult result = api.translate("Hello, world!", "en", "fr");
        System.out.println("Translated: " + result.getTranslatedText());
        System.out.println("Characters used: " + result.getCharactersUsed());
        System.out.println("Processing time: " + result.getProcessingTimeMs() + "ms");
        
        // Auto-detect source language
        TranslationResult autoResult = api.translate("Bonjour le monde", null, "en");
        System.out.println("Detected language: " + autoResult.getSourceLang());
        System.out.println("Translated: " + autoResult.getTranslatedText());
    }
}
```

## Comprehensive Examples

### Text Translation

```java
import com.forcekeys.translateapi.TranslateAPI;
import com.forcekeys.translateapi.TranslationResult;
import com.forcekeys.translateapi.TranslationOptions;

public class Example {
    public static void main(String[] args) {
        TranslateAPI api = new TranslateAPI("your_api_key_here");
        
        // Basic translation
        TranslationResult result = api.translate(
            "Hello, how are you?",
            "en",
            "es"
        );
        
        // With formality control
        TranslationOptions options = new TranslationOptions()
            .setFormality("formal");  // or "informal"
        
        TranslationResult formalResult = api.translate(
            "Hello, how are you?",
            "en",
            "de",
            options
        );
        
        // Translation with context
        TranslationOptions contextOptions = new TranslationOptions()
            .setContext("financial");  // Helps with ambiguous words
        
        TranslationResult contextResult = api.translate(
            "The bank is closed on Sunday.",
            "en",
            "fr",
            contextOptions
        );
    }
}
```

### Document Translation

```java
import com.forcekeys.translateapi.DocumentTranslationResult;
import java.nio.file.Paths;

// Translate a document file
DocumentTranslationResult result = api.translateDocument(
    Paths.get("document.pdf"),
    "en",
    "es"
);

// Save translated text to file
Files.write(Paths.get("translated_document.txt"), 
    result.getTranslatedText().getBytes());

System.out.println("Translated " + result.getPages() + " pages");
System.out.println("Used " + result.getCharactersUsed() + " characters");
```

### Image OCR and Translation

```java
import com.forcekeys.translateapi.OCRResult;

// Extract text from image and translate
OCRResult result = api.ocrAndTranslate(
    Paths.get("receipt.png"),
    "en",
    "fr"
);

System.out.println("Extracted text: " + result.getExtractedText());
System.out.println("Translated text: " + result.getTranslatedText());
System.out.println("Confidence: " + result.getConfidence() + "%");
```

### Language Detection

```java
import com.forcekeys.translateapi.LanguageDetectionResult;

// Detect language of text
LanguageDetectionResult detection = api.detect("Bonjour le monde");

System.out.println("Detected language: " + detection.getLanguage());
System.out.println("Language name: " + detection.getLanguageName());
System.out.println("Confidence: " + detection.getConfidence() + "%");

// Show alternative possibilities
for (LanguageAlternative alt : detection.getAlternatives()) {
    System.out.println("  - " + alt.getLanguage() + ": " + alt.getConfidence() + "%");
}
```

### Batch Translation

```java
import com.forcekeys.translateapi.BatchTranslationResult;
import java.util.Arrays;
import java.util.List;

// Translate multiple texts at once
List<String> texts = Arrays.asList(
    "Hello",
    "Goodbye",
    "Thank you",
    "Please"
);

BatchTranslationResult results = api.batchTranslate(
    texts,
    "en",
    "de"
);

for (BatchTranslationItem item : results.getTranslations()) {
    System.out.println(item.getOriginal() + " => " + item.getTranslated());
}
```

### Account Information

```java
import com.forcekeys.translateapi.AccountInfo;

// Get account details
AccountInfo account = api.account();

System.out.println("Email: " + account.getEmail());
System.out.println("Plan: " + account.getPlan());
System.out.println("Status: " + account.getStatus());

// Usage statistics
PlanLimits limits = account.getPlanLimits();
System.out.println("Daily translations: " + limits.getTodayUsed() + 
    "/" + limits.getDailyTranslations());
System.out.println("Remaining today: " + limits.getRemainingToday());

// Balance information
BalanceInfo balance = account.getBalance();
System.out.println("Available balance: $" + 
    String.format("%.2f", balance.getAvailable()));
System.out.println("Total spent: $" + 
    String.format("%.2f", balance.getTotalSpent()));
```

### Supported Languages

```java
import com.forcekeys.translateapi.LanguageList;

// Get all supported languages
LanguageList languages = api.languages();

System.out.println("Total languages: " + languages.getCount());
for (Language lang : languages.getLanguages()) {
    System.out.println(lang.getFlag() + " " + lang.getCode() + 
        ": " + lang.getName());
}
```

## Advanced Configuration

### Custom HTTP Client

```java
import java.net.http.HttpClient;
import java.time.Duration;

// Create custom HTTP client
HttpClient httpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(30))
    .version(HttpClient.Version.HTTP_2)
    .build();

TranslateAPI api = new TranslateAPI.Builder("your_api_key")
    .httpClient(httpClient)
    .baseUrl("https://api.deeptranslate.online/api/v1")
    .timeout(Duration.ofSeconds(30))
    .retries(3)
    .build();
```

### Environment Variables

```java
// Read API key from environment variable
String apiKey = System.getenv("FORCEKEYS_API_KEY");
TranslateAPI api = new TranslateAPI(apiKey);
```

### Error Handling

```java
import com.forcekeys.translateapi.APIException;

try {
    TranslationResult result = api.translate("Hello", "en", "fr");
} catch (APIException e) {
    System.out.println("API Error: " + e.getCode() + " - " + e.getMessage());
    System.out.println("Status Code: " + e.getStatusCode());
    
    if ("rate_limit_exceeded".equals(e.getCode())) {
        System.out.println("Retry after: " + e.getRetryAfter() + " seconds");
    } else if ("insufficient_credits".equals(e.getCode())) {
        System.out.println("Please add credits to your account");
    }
} catch (Exception e) {
    System.out.println("Unexpected error: " + e.getMessage());
}
```

### Asynchronous API

```java
import java.util.concurrent.CompletableFuture;

// Async translation
CompletableFuture<TranslationResult> future = api.translateAsync(
    "Hello, world!", "en", "fr");

future.thenAccept(result -> {
    System.out.println("Translated: " + result.getTranslatedText());
}).exceptionally(e -> {
    System.out.println("Error: " + e.getMessage());
    return null;
});

// Wait for completion
future.join();
```

## API Reference

### TranslateAPI Class

```java
// Builder pattern
TranslateAPI api = new TranslateAPI.Builder(apiKey)
    .baseUrl("https://api.deeptranslate.online/api/v1")
    .timeout(Duration.ofSeconds(30))
    .retries(3)
    .build();

// Simple constructor
TranslateAPI api = new TranslateAPI(apiKey);
```

#### Methods

| Method | Description | Parameters |
|--------|-------------|------------|
| `translate(String text, String source, String target)` | Translate text | `text`: Text to translate<br>`source`: Source language code (null for auto-detect)<br>`target`: Target language code |
| `translate(String text, String source, String target, TranslationOptions options)` | Translate text with options | `text`: Text to translate<br>`source`: Source language code<br>`target`: Target language code<br>`options`: Translation options |
| `translateDocument(Path filePath, String source, String target)` | Translate document file | `filePath`: Path to document (PDF, DOCX, TXT)<br>`source`: Source language code<br>`target`: Target language code |
| `ocrAndTranslate(Path imagePath, String source, String target)` | Extract text from image and translate | `imagePath`: Path to image file<br>`source`: Source language code<br>`target`: Target language code |
| `detect(String text)` | Detect language of text | `text`: Text to analyze |
| `batchTranslate(List<String> texts, String source, String target)` | Translate multiple texts | `texts`: List of texts to translate<br>`source`: Source language code<br>`target`: Target language code |
| `languages()` | Get supported languages | |
| `account()` | Get account information | |

#### Async Methods

All methods have async counterparts with `Async` suffix (e.g., `translateAsync()`).

### Response Objects

All methods return typed response objects with the following common properties:

- `status`: "success" or "error"
- `processingTimeMs`: Processing time in milliseconds
- `charactersUsed`: Number of characters used

#### TranslationResponse
- `translatedText`: Translated text
- `sourceLang`: Source language code
- `targetLang`: Target language code

#### DocumentTranslationResponse
- `translatedText`: Translated text
- `pages`: Number of pages processed
- `charactersUsed`: Characters used

#### OCRResponse
- `extractedText`: Text extracted from image
- `translatedText`: Translated text (if translation requested)
- `confidence`: OCR confidence percentage
- `languageDetected`: Detected language in image

#### DetectionResponse
- `language`: Detected language code
- `languageName`: Full language name
- `confidence`: Detection confidence percentage
- `alternatives`: List of alternative possibilities

#### AccountResponse
- `email`: User email
- `plan`: Subscription plan
- `status`: Account status
- `planLimits`: Plan limits object
- `balance`: Balance information object
- `statistics`: Usage statistics object

## Error Codes

The SDK throws `APIException` for API errors:

| Code | Description | HTTP Status |
|------|-------------|-------------|
| `invalid_request` | Missing or malformed parameters | 400 |
| `unauthorized` | Invalid or missing API key | 401 |
| `forbidden` | Feature not available on your plan | 403 |
| `payload_too_large` | File or text exceeds size limit | 413 |
| `unsupported_language` | Language code not supported | 422 |
| `rate_limit_exceeded` | Too many requests | 429 |
| `insufficient_credits` | Not enough credits | 402 |
| `internal_error` | Server error | 500 |

## Rate Limits

Rate limits vary by plan:

| Plan | Requests/Minute | Monthly Requests | Max Characters/Request |
|------|----------------|------------------|------------------------|
| Free | 10 | 500/day | 2,000 |
| Starter | 60 | 50,000 | 5,000 |
| Professional | 300 | 1,000,000 | 10,000 |
| Enterprise | Unlimited | Unlimited | Unlimited |

## Android Integration

### Add to Android Project

```gradle
// build.gradle (app level)
dependencies {
    implementation 'com.forcekeys:translate-api:1.0.0'
    
    // Required for Android
    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
}
```

### Android Usage

```java
import android.os.AsyncTask;
import com.forcekeys.translateapi.TranslateAPI;

public class TranslationTask extends AsyncTask<String, Void, String> {
    private TranslateAPI api;
    
    public TranslationTask() {
        this.api = new TranslateAPI("your_api_key");
    }
    
    @Override
    protected String doInBackground(String... params) {
        try {
            TranslationResult result = api.translate(
                params[0], "en", "fr");
            return result.getTranslatedText();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @Override
    protected void onPostExecute(String result) {
        // Update UI with translation
    }
}
```

## Spring Boot Integration

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.forcekeys.translateapi.TranslateAPI;

@Configuration
public class TranslateConfig {
    
    @Bean
    public TranslateAPI translateAPI() {
        return new TranslateAPI.Builder(System.getenv("FORCEKEYS_API_KEY"))
            .baseUrl("https://api.deeptranslate.online/api/v1")
            .build();
    }
}

// Usage in service
@Service
public class TranslationService {
    
    private final TranslateAPI api;
    
    public TranslationService(TranslateAPI api) {
        this.api = api;
    }
    
    public String translate(String text, String targetLang) {
        TranslationResult result = api.translate(text, null, targetLang);
        return result.getTranslatedText();
    }
}
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

- **Documentation**: [deeptranslate.online/docs](https://deeptranslate.online/docs)
- **Issues**: [GitHub Issues](https://github.com/forcekeys/translate-api-java/issues)
- **Email**: support@deeptranslate.online
- **Discord**: [Join our Discord](https://discord.gg/forcekeys)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Related Projects

