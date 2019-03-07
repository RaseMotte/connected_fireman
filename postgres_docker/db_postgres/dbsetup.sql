CREATE TABLE measurement(
  udid serial,
  longitude FLOAT NOT NULL,
  latitude FLOAT NOT NULL,
  temperatureIn FLOAT NOT NULL,
  temperatureOut FLOAT NOT NULL,
  mtime TIMESTAMP NOT NULL
);
