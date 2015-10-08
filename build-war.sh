mvn clean install -Dmaven.test.skip=true -U
mv ./rest-component/target/cloudopting.war ./Dockerfile/cloudopting.war
