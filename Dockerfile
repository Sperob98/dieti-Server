# Usa un'immagine base con Java
FROM openjdk:17-jdk-alpine

# Configura una directory di lavoro nel container
WORKDIR /app

# Copia il file .jar generato nel container
ARG JAR_FILE=target/dieti2024-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Espone la porta su cui l'app sarà in ascolto
EXPOSE 8081
