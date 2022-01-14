FROM devops.digital.belgium.be:1443/tooling-base-image:7-a4ca8a9

RUN ./scripts/openjdk-11.sh
RUN ./scripts/java-apm.sh

EXPOSE 8080

COPY target/*.jar ./spring-boot-app.jar

CMD ["java", "-jar", "-javaagent:apm-agent.jar", "-Djava.security.egd=file:/dev/./urandom", "spring-boot-app.jar", "$JAVA_OPTS"]
