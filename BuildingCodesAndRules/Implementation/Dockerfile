FROM tomcat:10.1.26-jdk21-temurin-jammy

LABEL maintainer="beachth@cardiff.ac.uk"

RUN rm -rf /usr/local/webapps/*
ADD target/buildingcodesandrules.war /usr/local/tomcat/webapps/ROOT.war



EXPOSE 8080
CMD ["catalina.sh", "run"]
