# Build Java app
FROM maven:3.8.3-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package

# Run Java app
FROM openjdk:17
VOLUME /tmp
EXPOSE 8098
ARG JAR_FILE=target/calculate-regulation-disarrangement-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

LABEL maintainer="Damián Blanc <damianblanc@gmail.com>"
LABEL version="1.0.0"
LABEL description="Calculate FCI Regulation Biases"