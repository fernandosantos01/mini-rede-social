# ====== build ======
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# cache de dependências
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# build da aplicação
COPY src ./src
RUN mvn -q -DskipTests package

# ====== runtime ======
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


