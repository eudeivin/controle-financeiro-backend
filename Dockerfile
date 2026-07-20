# 1. Fase de Build (Compilação) com Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Configura a variável de ambiente do sistema para forçar UTF-8 em todas as operações do Maven
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8"
RUN mvn clean package -DskipTests

# 2. Fase de Execução com a imagem correta do Eclipse Temurin 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]