# Connected fireman

This project is part of an assignment for SCALA/SPARK course at EPITAi (SCIA 2020).

Our project is an API for fireman suits. Every suits will send some information about the fireman (position, temperature, time) so it can be easier for them during the interventions.

The project is divided in two parts: the client and the server.

## Build and Set up the project

First, you must launch the server. To do so, run
```
./server.sh
```
Then, you can simulate a client doing
```
./client.sh
```

## Client

The client takes a directory as input. This directory contains different json file. Each line of those json files represent a measurement from the fireman suit.
For each json file in this directory, it will read it line by line and send the request to the server.

## Server

The server handles POST and GET requests, then connects to a Postgres Docker database to store the data. You can GET the data at

 - localhost:9000/measurements
 - localhost:9000/emergency
 - localhost:9000/

### Request examples:

```
curl -v localhost:9000/measurements -X POST -H "Content-length application/json" -d '{"udid": 1, "longitude": 43534.5345, "latitude": -352.4634, "temperatureIn" : 38, "temperatureOut": 57.3, "mtime": "2018-01-05T00:00:00Z"}'
curl -v localhost:9000/emergency -X POST -H "Content-Type: application/json" -d '{"udid": 1, "metric": "this is a metric", "message": "this is a message", "mtime": "2018-01-05T00:00:00Z"}'

```

# Documentation
## Kafka docker image info

Used the spotify/kafka image as it includes an image for zookeeper.
link to doc: https://github.com/spotify/docker-kafka


