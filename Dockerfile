FROM openjdk:8-alpine
LABEL maintainer="acarbenay@adhuc.fr"

WORKDIR /srv/
COPY ./liquibaserefactoringcolumnname.jar liquibaserefactoringcolumnname.jar
CMD java -jar liquibaserefactoringcolumnname.jar
