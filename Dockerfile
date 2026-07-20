# 1. Fase de Build (Compilação) com Java 17 legítimo
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Adicionamos o comando para forçar a leitura correta dos arquivos de texto
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# 2. Fase de Execução com a imagem correta do Eclipse Temurin 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]