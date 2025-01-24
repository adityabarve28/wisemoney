#FROM openjdk:17-jdk
#
#COPY target/wisemoney.jar .
#
#EXPOSE 8080
#
#ENTRYPOINT [ "java", "-jar", "wisemoney.jar"]
FROM maven:3.8.5-openjdk-17 AS build
copy . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/wisemoney.jar wisemoney.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jdk", "wisemoney.jar" ]