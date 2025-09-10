package com.example.login_page

// LocaleHelper.kt
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

object LocaleHelper {

    private const val PREFS_NAME = "LanguagePrefs"
    private const val SELECTED_LANGUAGE = "SelectedLanguage"
    private const val DEFAULT_LANGUAGE = "en" // Default to English

    /**
     * Sets the application's locale to the specified language code and saves it.
     */
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    /**
     * Retrieves the saved locale and updates the context.
     */
    fun getPersistedLocale(context: Context): Context {
        val language = getPersistedData(context, DEFAULT_LANGUAGE)
        return updateResources(context, language)
    }

    // Reads the saved language from SharedPreferences
    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Returns the saved language or the default language if not found
        return sharedPreferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
    }

    // Saves the selected language to SharedPreferences
    private fun persist(context: Context, language: String) {
        // Uses androidx.core.content.edit extension for simpler editing
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString(SELECTED_LANGUAGE, language)
        }
    }

    // Updates the application resources configuration for the new locale
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        // Set the default locale for the JVM
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        // Set the locale in the configuration
        configuration.setLocale(locale)

        // Create a new context with the updated configuration
        return context.createConfigurationContext(configuration)
    }
}