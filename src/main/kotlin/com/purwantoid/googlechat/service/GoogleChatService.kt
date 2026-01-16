package com.purwantoid.googlechat.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.chat.v1.HangoutsChat
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.purwantoid.googlechat.settings.GoogleChatSettingsState
import java.io.File
import java.io.StringReader

@Service(Service.Level.APP)
class GoogleChatService {
    private val logger = Logger.getInstance(GoogleChatService::class.java)
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    
    // Scopes for Google Chat API
    // READ_ONLY allows reading spaces and messages
    private val SCOPES = listOf("https://www.googleapis.com/auth/chat.spaces.readonly", "https://www.googleapis.com/auth/chat.messages.readonly")
    private val CREDENTIALS_FILE_PATH = System.getProperty("user.home") + "/.google-chat-jetbrains-plugin"

    fun getChatClient(): HangoutsChat? {
        val credential = authorize(false) ?: return null
        return HangoutsChat.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName("Google Chat JetBrains Plugin")
            .build()
    }
    
    fun isAuthenticated(): Boolean {
        // Just check if we can get a credential without triggering the UI interaction
        // However, authorize() with Flow executes loading from store. 
        // We need to be careful not to pop up browser if we just want to check status.
        // For simplicity, we assume if we have a stored credential, we are authenticated.
        return try {
            val settings = GoogleChatSettingsState.instance
            if (settings.clientId.isEmpty() || settings.clientSecret.isEmpty()) return false
            
            val flow = buildFlow(settings) ?: return false
            val credential = flow.loadCredential("user")
            credential != null && (credential.refreshToken != null || credential.expiresInSeconds == null || credential.expiresInSeconds > 60L)
        } catch (e: Exception) {
            false
        }
    }

    fun listSpaces(): List<com.google.api.services.chat.v1.model.Space> {
        val client = getChatClient() ?: throw Exception("Not authenticated. Please configure settings.")
        try {
            val response = client.spaces().list().execute()
            return response.spaces ?: emptyList()
        } catch (e: Exception) {
            logger.error("Failed to list spaces", e)
            throw e
        }
    }

    fun login() {
        authorize(true)
    }

    fun logout() {
        try {
            val file = File(CREDENTIALS_FILE_PATH)
            if (file.exists()) {
                file.deleteRecursively()
            }
            logger.info("Logged out successfully")
        } catch (e: Exception) {
            logger.error("Error logging out", e)
        }
    }

    private fun buildFlow(settings: GoogleChatSettingsState): GoogleAuthorizationCodeFlow? {
         val clientSecretsJson = """
            {
              "installed": {
                "client_id": "${settings.clientId}",
                "client_secret": "${settings.clientSecret}"
              }
            }
        """.trimIndent()
        
        return try {
            val clientSecrets = GoogleClientSecrets.load(jsonFactory, StringReader(clientSecretsJson))
             GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES
            )
            .setDataStoreFactory(FileDataStoreFactory(File(CREDENTIALS_FILE_PATH)))
            .setAccessType("offline")
            .build()
        } catch (e: Exception) {
             logger.error("Error building flow", e)
             null
        }
    }

    private fun authorize(interactive: Boolean): Credential? {
        val settings = GoogleChatSettingsState.instance
        if (settings.clientId.isEmpty() || settings.clientSecret.isEmpty()) {
            logger.warn("Client ID or Secret is empty. Please configure in settings.")
            return null
        }

        try {
            val flow = buildFlow(settings) ?: return null
            val receiver = LocalServerReceiver.Builder().setPort(8888).build()
            
            // If not interactive and no credential, don't trigger flow
            if (!interactive) {
                 val cred = flow.loadCredential("user")
                 if (cred == null) return null
            }
            
            return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        } catch (e: Exception) {
            logger.error("Error during authorization", e)
            return null
        }
    }
    
    companion object {
        val instance: GoogleChatService
            get() = ApplicationManager.getApplication().getService(GoogleChatService::class.java)
    }
}
