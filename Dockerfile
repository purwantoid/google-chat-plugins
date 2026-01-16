# Use JDK 17 as it's the standard for IntelliJ Platform Gradle Plugin 2.x
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy necessary files for gradle wrapper to download gradle
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Copy source code
COPY src/ src/

# Allow execution
RUN chmod +x gradlew

# Run build
CMD ["./gradlew", "buildPlugin", "--no-daemon"]
