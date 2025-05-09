FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-jammy

COPY --from=builder /app/target/*.jar ./app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar ./app.jar" ]
