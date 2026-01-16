package com.purwantoid.googlechat.ui

import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.purwantoid.googlechat.service.GoogleChatService
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingConstants

class LoginPanel(private val onLoginSuccess: () -> Unit) : JPanel() {

    init {
        layout = GridBagLayout()
        val gbc = GridBag()
            .setDefaultWeightX(1.0)
            .setDefaultFill(GridBagConstraints.HORIZONTAL)
            .setDefaultInsets(JBUI.insets(10))

        val titleLabel = JBLabel("Google Chat", SwingConstants.CENTER).apply {
             font = font.deriveFont(20f)
        }
        
        val descLabel = JBLabel("Please login to view your chats.", SwingConstants.CENTER)

        val loginButton = JButton("Login with Google").apply {
            addActionListener { e: ActionEvent ->
                val service = GoogleChatService.instance
                service.login()
                if (service.isAuthenticated()) {
                    onLoginSuccess()
                }
            }
        }

        add(titleLabel, gbc.nextLine())
        add(descLabel, gbc.nextLine())
        add(loginButton, gbc.nextLine().fillCellNone())
    }
}
