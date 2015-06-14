FROM ubuntu:12.04
MAINTAINER Jose Fuentes <jfuentes@wtelecom.es>

RUN apt-get -y update && apt-get -y install ruby
RUN apt-get install -y puppet

VOLUME containerLogs /tmp/logs

ADD manifest.pp /tmp/manifest.pp

ADD puppet apply -l /tmp/logs/puppet.log /tmp/manifest.pp


