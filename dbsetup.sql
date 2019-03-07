CREATE TABLE mesurement(
  udid serial,
  longitude DOUBLE NOT NULL,
  latitude DOUBLE NOT NULL,
  temperatureIn DOUBLE NOT NULL,
  temperatureOut DOUBLE NOT NULL,
  ctime TIME NOT NULL
);