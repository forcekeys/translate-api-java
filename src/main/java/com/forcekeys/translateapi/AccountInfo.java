package com.forcekeys.translateapi;

import java.util.Map;

/**
 * Account information
 * 
 * Represents user account information including plan details, usage statistics,
 * and API key information.
 * 
 * Example:
 * ```java
 * Map<String, Object> accountInfo = api.getAccountInfo();
 * AccountInfo account = new AccountInfo(accountInfo);
 * System.out.println("Email: " + account.getEmail());
 * System.out.println("Plan: " + account.getPlan());
 * System.out.println("Daily translations: " + account.getTodayUsed() + "/" + account.getDailyTranslations());
 * ```
 */
public class AccountInfo {
    private final String email;
    private final String name;
    private final String status;
    private final String plan;
    private final Map<String, Object> planLimits;
    private final Map<String, Object> balance;
    private final Map<String, Object> apiKey;
    private final Map<String, Object> statistics;
    
    /**
     * Constructor from raw account data
     * 
     * @param accountData Raw account data from API
     */
    @SuppressWarnings("unchecked")
    public AccountInfo(Map<String, Object> accountData) {
        this.email = (String) accountData.getOrDefault("email", "");
        this.name = (String) accountData.getOrDefault("name", "");
        this.status = (String) accountData.getOrDefault("status", "");
        this.plan = (String) accountData.getOrDefault("plan", "");
        this.planLimits = (Map<String, Object>) accountData.getOrDefault("plan_limits", new java.util.HashMap<>());
        this.balance = (Map<String, Object>) accountData.getOrDefault("balance", new java.util.HashMap<>());
        this.apiKey = (Map<String, Object>) accountData.getOrDefault("api_key", new java.util.HashMap<>());
        this.statistics = (Map<String, Object>) accountData.getOrDefault("statistics", new java.util.HashMap<>());
    }
    
    /**
     * Get user email
     * 
     * @return User email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Get user name
     * 
     * @return User name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get account status
     * 
     * @return Account status (active, suspended, pending)
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Check if account is active
     * 
     * @return True if account status is "active"
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    /**
     * Get subscription plan
     * 
     * @return Subscription plan (free, starter, professional, enterprise)
     */
    public String getPlan() {
        return plan;
    }
    
    /**
     * Check if plan is free
     * 
     * @return True if plan is "free"
     */
    public boolean isFreePlan() {
        return "free".equalsIgnoreCase(plan);
    }
    
    /**
     * Check if plan is paid
     * 
     * @return True if plan is not free
     */
    public boolean isPaidPlan() {
        return !isFreePlan();
    }
    
    /**
     * Get daily translation limit
     * 
     * @return Daily translation limit for current plan
     */
    public int getDailyTranslations() {
        Object limit = planLimits.get("daily_translations");
        return limit instanceof Number ? ((Number) limit).intValue() : 0;
    }
    
    /**
     * Get translations used today
     * 
     * @return Number of translations used today
     */
    public int getTodayUsed() {
        Object used = planLimits.get("today_used");
        return used instanceof Number ? ((Number) used).intValue() : 0;
    }
    
    /**
     * Get remaining translations for today
     * 
     * @return Remaining translations available today
     */
    public int getRemainingToday() {
        Object remaining = planLimits.get("remaining_today");
        return remaining instanceof Number ? ((Number) remaining).intValue() : 0;
    }
    
    /**
     * Get percentage of daily limit used
     * 
     * @return Percentage of daily limit used (0-100)
     */
    public double getPercentageUsed() {
        Object percentage = planLimits.get("percentage_used");
        return percentage instanceof Number ? ((Number) percentage).doubleValue() : 0.0;
    }
    
    /**
     * Check if daily limit is reached
     * 
     * @return True if daily translation limit is reached
     */
    public boolean isDailyLimitReached() {
        return getRemainingToday() <= 0;
    }
    
    /**
     * Get available balance
     * 
     * @return Available balance for pay-as-you-go usage
     */
    public double getAvailableBalance() {
        Object balance = this.balance.get("available");
        return balance instanceof Number ? ((Number) balance).doubleValue() : 0.0;
    }
    
    /**
     * Get total amount spent
     * 
     * @return Total amount spent on translations
     */
    public double getTotalSpent() {
        Object spent = balance.get("total_spent");
        return spent instanceof Number ? ((Number) spent).doubleValue() : 0.0;
    }
    
    /**
     * Get API key name
     * 
     * @return API key name
     */
    public String getApiKeyName() {
        return (String) apiKey.getOrDefault("name", "");
    }
    
    /**
     * Get API key status
     * 
     * @return API key status (active, revoked)
     */
    public String getApiKeyStatus() {
        return (String) apiKey.getOrDefault("status", "");
    }
    
    /**
     * Check if API key is active
     * 
     * @return True if API key status is "active"
     */
    public boolean isApiKeyActive() {
        return "active".equalsIgnoreCase(getApiKeyStatus());
    }
    
    /**
     * Get API key creation date
     * 
     * @return API key creation date as string
     */
    public String getApiKeyCreatedAt() {
        return (String) apiKey.getOrDefault("created_at", "");
    }
    
    /**
     * Get API key last used date
     * 
     * @return API key last used date as string
     */
    public String getApiKeyLastUsed() {
        return (String) apiKey.getOrDefault("last_used", "");
    }
    
    /**
     * Get total translations made
     * 
     * @return Total number of translations made
     */
    public int getTotalTranslations() {
        Object translations = statistics.get("total_translations");
        return translations instanceof Number ? ((Number) translations).intValue() : 0;
    }
    
    /**
     * Get total characters translated
     * 
     * @return Total number of characters translated
     */
    public int getTotalCharacters() {
        Object characters = statistics.get("total_characters");
        return characters instanceof Number ? ((Number) characters).intValue() : 0;
    }
    
    /**
     * Get average characters per translation
     * 
     * @return Average characters per translation or 0 if no translations
     */
    public double getAverageCharactersPerTranslation() {
        int totalTranslations = getTotalTranslations();
        if (totalTranslations == 0) {
            return 0.0;
        }
        return (double) getTotalCharacters() / totalTranslations;
    }
    
    /**
     * Get raw plan limits data
     * 
     * @return Raw plan limits data
     */
    public Map<String, Object> getPlanLimits() {
        return planLimits;
    }
    
    /**
     * Get raw balance data
     * 
     * @return Raw balance data
     */
    public Map<String, Object> getBalance() {
        return balance;
    }
    
    /**
     * Get raw API key data
     * 
     * @return Raw API key data
     */
    public Map<String, Object> getApiKey() {
        return apiKey;
    }
    
    /**
     * Get raw statistics data
     * 
     * @return Raw statistics data
     */
    public Map<String, Object> getStatistics() {
        return statistics;
    }
    
    /**
     * Get all account data as map
     * 
     * @return All account data as map
     */
    public Map<String, Object> toMap() {
        java.util.HashMap<String, Object> map = new java.util.HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("status", status);
        map.put("plan", plan);
        map.put("plan_limits", planLimits);
        map.put("balance", balance);
        map.put("api_key", apiKey);
        map.put("statistics", statistics);
        return map;
    }
    
    @Override
    public String toString() {
        return "AccountInfo{" +
               "email='" + email + '\'' +
               ", name='" + name + '\'' +
               ", status='" + status + '\'' +
               ", plan='" + plan + '\'' +
               ", dailyTranslations=" + getDailyTranslations() +
               ", todayUsed=" + getTodayUsed() +
               ", remainingToday=" + getRemainingToday() +
               '}';
    }
}