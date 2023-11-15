FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn verify --fail-never
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /app
COPY --from=build /build/target/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT java -jar app.jar
