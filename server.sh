#! /bin/bash


sudo docker-compose -f postgres_docker/docker-compose.yml up -d
sleep 10s;
sbt run;

