#! /bin/bash

# Start the zookeper and kafka server within the docker
sudo docker-compose -f containers/docker-compose.yml up
sleep 10s;
sbt run;

