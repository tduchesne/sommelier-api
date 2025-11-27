# Étape 1 : On utilise une image Maven pour construire l'application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# On compile le projet (en sautant les tests pour aller plus vite)
RUN mvn clean package -DskipTests

# Étape 2 : On utilise une image Java légère pour lancer l'application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# On récupère le fichier .jar créé à l'étape 1
COPY --from=build /app/target/*.jar app.jar
# On expose le port 8080
EXPOSE 8080
# La commande de démarrage
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]