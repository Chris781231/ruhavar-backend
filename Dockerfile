FROM adoptopenjdk:16-jre-hotspot as builder
WORKDIR application
COPY target/ruhavar-backend-0.0.1-SNAPSHOT.jar ruhavar-backend.jar
RUN java -Djarmode=layertools -jar ruhavar-backend.jar extract
FROM adoptopenjdk:16-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
CMD ["java", \
"org.springframework.boot.loader.JarLauncher"]