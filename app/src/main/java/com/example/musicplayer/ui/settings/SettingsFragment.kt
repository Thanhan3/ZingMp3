package com.example.musicplayer.ui.settings

import android.app.UiModeManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.musicplayer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPreference()
    }

    private fun setupPreference() {
        val nightModePref = findPreference<SwitchPreferenceCompat>(KEY_PREF_DARK_MODE)
        nightModePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                changeNightMode(preference, newValue)
                true
            }
        val languagePref = findPreference<ListPreference>(KEY_PREF_LANGUAGE)
        languagePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                changeLanguage(preference, newValue)
                true
            }
    }

    private fun changeNightMode(preference: Preference, newValue: Any) {
        var oldNightMode = false
        val sharedPreferences = preference.sharedPreferences
        val uiModeManager = requireActivity().getSystemService(UiModeManager::class.java)
        if (sharedPreferences != null) {
            oldNightMode = sharedPreferences.getBoolean(KEY_PREF_DARK_MODE, false)
        }
        val newNightMode = newValue.toString().toBoolean()
        if (newNightMode != oldNightMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val modeUi = if (newNightMode) {
                    UiModeManager.MODE_NIGHT_YES
                } else {
                    UiModeManager.MODE_NIGHT_NO
                }
                uiModeManager.setApplicationNightMode(modeUi)
            } else {
                val modeUi = if (newNightMode) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(modeUi)
            }
        }
    }

    private fun changeLanguage(preference: Preference, newValue: Any) {
        var language = "en"
        val sharedPreferences = preference.sharedPreferences
        if (sharedPreferences != null) {
            language = sharedPreferences.getString(KEY_PREF_LANGUAGE, "en") ?: "en"
        }
        val newLanguage = newValue.toString()
        if(language != newLanguage) {
            val localListCompat = LocaleListCompat.forLanguageTags(newLanguage)
            AppCompatDelegate.setApplicationLocales(localListCompat)
        }
    }

    companion object {
        const val KEY_PREF_DARK_MODE = "dark_mode"
        const val KEY_PREF_LANGUAGE = "language"
    }
}