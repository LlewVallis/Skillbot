FROM maven:3-jdk-11

COPY . /server
WORKDIR /server
RUN mvn package
ENTRYPOINT java -jar target/skillbot*.jar
