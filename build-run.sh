#!/bin/bash

mvn clean install -Dmaven.test.skip=true -U
cd rest-component
mvn spring-boot:run
