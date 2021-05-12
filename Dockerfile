FROM openjdk:11

EXPOSE 8080
EXPOSE 587
ADD target .

ENTRYPOINT ["java", "-jar", "messenger-api.jar"]