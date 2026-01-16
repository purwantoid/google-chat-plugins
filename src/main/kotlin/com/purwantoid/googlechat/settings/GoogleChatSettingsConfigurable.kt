package com.purwantoid.googlechat.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class GoogleChatSettingsConfigurable : Configurable {
    private var settingsComponent: GoogleChatSettingsComponent? = null

    override fun getDisplayName(): String {
        return "Google Chat"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return settingsComponent?.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent? {
        settingsComponent = GoogleChatSettingsComponent()
        return settingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = GoogleChatSettingsState.instance
        return settingsComponent!!.clientId != settings.clientId ||
                settingsComponent!!.clientSecret != settings.clientSecret
    }

    override fun apply() {
        val settings = GoogleChatSettingsState.instance
        settings.clientId = settingsComponent!!.clientId
        settings.clientSecret = settingsComponent!!.clientSecret
    }

    override fun reset() {
        val settings = GoogleChatSettingsState.instance
        settingsComponent!!.clientId = settings.clientId
        settingsComponent!!.clientSecret = settings.clientSecret
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
