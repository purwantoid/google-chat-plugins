package com.purwantoid.googlechat.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.purwantoid.googlechat.service.GoogleChatService
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JPanel
import javax.swing.ListSelectionModel

class ChatListPanel : JPanel() {
    private val listModel = DefaultListModel<String>()
    private val chatList = JBList(listModel)

    init {
        layout = BorderLayout()
        chatList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        chatList.setEmptyText("Loading chats...")
        
        add(JBScrollPane(chatList), BorderLayout.CENTER)
        
        refreshChats()
    }

    private fun refreshChats() {
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val spaces = GoogleChatService.instance.listSpaces()
                
                ApplicationManager.getApplication().invokeLater {
                    listModel.clear()
                    if (spaces.isEmpty()) {
                        chatList.setEmptyText("No chats found.")
                    } else {
                        spaces.forEach { space ->
                            listModel.addElement(space.displayName ?: "Unknown Chat")
                        }
                    }
                }
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    listModel.clear()
                    chatList.setEmptyText("Error: ${e.message}")
                }
            }
        }
    }
}
