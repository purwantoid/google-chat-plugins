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
        
        val service = com.purwantoid.googlechat.service.GoogleChatService.instance
        settingsComponent?.setAuthenticated(service.isAuthenticated())

        settingsComponent?.getLoginButton()?.addActionListener {
            try {
                // Apply settings first
                apply()
                service.login()
                settingsComponent?.setAuthenticated(service.isAuthenticated())
                if (service.isAuthenticated()) {
                    com.intellij.openapi.ui.Messages.showInfoMessage("Logged in successfully.", "Google Chat")
                }
            } catch (e: Exception) {
                com.intellij.openapi.ui.Messages.showErrorDialog("Login failed: ${e.message}", "Google Chat")
            }
        }

        settingsComponent?.getLogoutButton()?.addActionListener {
            service.logout()
            settingsComponent?.setAuthenticated(false)
            com.intellij.openapi.ui.Messages.showInfoMessage("Logged out successfully.", "Google Chat")
        }
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
