FROM eclipse-temurin:21-jdk-jammy
LABEL authors="Vyacheslav"
LABEL description="InternetShop Application"
WORKDIR /app
COPY target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]