# Connected fireman

```
curl -v localhost:9000/measurements -X POST -H "Content-length application/json" -d '{"udid": 1, "longitude": 43534.5345, "latitude": -352.4634, "temperatureIn" : 38, "temperatureOut": 57.3, "mtime": "2018-01-05T00:00:00Z"}'
curl -v localhost:9000/emergency -X POST -H "Content-length application/json" -d '{"udid": 1, "metric": "this is a metric", "message": "this is a message", "mtime": "2018-01-05T00:00:00Z"}'

```
