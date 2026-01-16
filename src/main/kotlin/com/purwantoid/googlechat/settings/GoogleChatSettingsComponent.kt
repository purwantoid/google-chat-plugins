package com.purwantoid.googlechat.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class GoogleChatSettingsComponent {
    private val mainPanel: JPanel
    private val clientIdText = JBTextField()
    private val clientSecretText = JBTextField()
    private val loginButton = javax.swing.JButton("Login")
    private val logoutButton = javax.swing.JButton("Logout")

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Client ID:"), clientIdText, 1, false)
            .addLabeledComponent(JBLabel("Client Secret:"), clientSecretText, 1, false)
            .addComponent(loginButton)
            .addComponent(logoutButton)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
    
    fun getLoginButton(): javax.swing.JButton {
        return loginButton
    }

    fun getLogoutButton(): javax.swing.JButton {
        return logoutButton
    }

    fun setAuthenticated(authenticated: Boolean) {
        loginButton.isVisible = !authenticated
        logoutButton.isVisible = authenticated
        clientIdText.isEnabled = !authenticated
        clientSecretText.isEnabled = !authenticated
    }

    fun getPanel(): JPanel {
        return mainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return clientIdText
    }

    var clientId: String
        get() = clientIdText.text
        set(newText) {
            clientIdText.text = newText
        }

    var clientSecret: String
        get() = clientSecretText.text
        set(newText) {
            clientSecretText.text = newText
        }
}
