# Étape 1 : On utilise une image Maven pour construire l'application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# On compile le projet (en sautant les tests pour aller plus vite)
RUN mvn clean package -DskipTests

# Étape 2 : On utilise une image Java légère pour lancer l'application
FROM eclipse-temurin:17-jdk-alpine

# Profil Spring configurable (par défaut : prod)
ENV SPRING_PROFILE=prod

# Crée un utilisateur non-root pour exécuter l'application
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# On récupère le fichier .jar créé à l'étape 1
COPY --from=build /app/target/*.jar app.jar
# Copier le script d'entrée qui permet l'expansion de la variable d'environnement
COPY docker-entrypoint.sh /app/docker-entrypoint.sh

# S'assurer que les fichiers appartiennent à l'utilisateur non-root et que le script est exécutable
RUN chown -R appuser:appgroup /app && chmod +x /app/docker-entrypoint.sh

# Passer à l'utilisateur non-root
USER appuser

# On expose le port 8080
EXPOSE 8080

# Utiliser le script d'entrée pour démarrer l'application et permettre l'utilisation de SPRING_PROFILE
CMD ["/app/docker-entrypoint.sh"]
