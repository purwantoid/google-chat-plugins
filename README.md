# Google Chat Integration Plugin

A JetBrains IDE plugin that brings Google Chat functionality directly into your workspace.

## Features
- **Integrated Tool Window**: View your Google Chat spaces within the IDE.
- **Secure Authentication**: Uses OAuth2 with Google's official API client.
- **Customizable Settings**: Configure your own Google API credentials for private or organizational use.

## Prerequisites
Before using this plugin, you need:
1. A **Google Cloud Project** with the **Google Chat API** enabled.
2. **OAuth 2.0 Client IDs** (Desktop application type).
3. The `client_id` and `client_secret` from your Google Cloud Console.

## Setup & Configuration
1. Open your JetBrains IDE (IntelliJ IDEA, etc.).
2. Go to **Settings/Preferences > Tools > Google Chat**.
3. Enter your **Client ID** and **Client Secret**.
4. Click **Apply** or **OK**.
5. Open the **Google Chat** tool window (usually on the right sidebar).
6. Click **Login with Google** and follow the authentication flow in your browser.

## Building the Plugin

### Using Docker (Recommended)
You can build the plugin without having JDK 17 installed locally:
```bash
docker-compose up
```
The built plugin will be available in the `build/distributions/` directory.

### Local Build
If you have JDK 17+ installed:
```bash
./gradlew buildPlugin
```

## Project Structure
- `src/main/kotlin`: Core implementation in Kotlin.
  - `service/`: OAuth2 handling and Google Chat API interaction.
  - `settings/`: IDE-integrated settings management.
  - `ui/`: Swing-based UI components (Tool Window, Panels).
- `src/main/resources/META-INF/plugin.xml`: Plugin registration and extension points.
- `Dockerfile` & `docker-compose.yml`: Containerized build environment.

## Authentication Details
- **Scopes**: `chat.spaces.readonly`, `chat.messages.readonly`.
- **Callback**: Uses a local receiver on `http://localhost:8888`.
- **Token Storage**: Credentials are securely cached in `~/.google-chat-jetbrains-plugin`.

## License
[Add License Information Here]
