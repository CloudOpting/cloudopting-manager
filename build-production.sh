mvn clean install -Dmaven.test.skip=true -Dspring.config.name=prod -U
mv ./rest-component/target/cloudopting.war ./Dockerfile/cloudopting.war