package com.purwantoid.googlechat

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.purwantoid.googlechat.service.GoogleChatService
import com.purwantoid.googlechat.ui.ChatListPanel
import com.purwantoid.googlechat.ui.LoginPanel
import javax.swing.JPanel

class GoogleChatToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val googleChatToolWindow = GoogleChatToolWindow(toolWindow)
        googleChatToolWindow.updateContent()
    }

    private class GoogleChatToolWindow(private val toolWindow: ToolWindow) {
        
        fun updateContent() {
            val contentFactory = ContentFactory.getInstance()
            val panel = if (GoogleChatService.instance.isAuthenticated()) {
                ChatListPanel()
            } else {
                LoginPanel {
                    // On login success, re-run updateContent on EDT
                    com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater {
                        updateContent()
                    }
                }
            }
            
            val content = contentFactory.createContent(panel, "", false)
            toolWindow.contentManager.removeAllContents(true)
            toolWindow.contentManager.addContent(content)
        }
    }
}
