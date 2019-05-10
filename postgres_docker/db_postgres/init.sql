CREATE TABLE mesurement(
  udid serial,
  longitude FLOAT NOT NULL,
  latitude FLOAT NOT NULL,
  temperatureIn FLOAT NOT NULL,
  temperatureOut FLOAT NOT NULL,
  ctime TIME NOT NULL
);

CREATE TABLE emergency(
  udid serial,
  metric VARCHAR NOT NULL,
  message VARCHAR NOT NULL,
  ctime TIME NOT NULL
);
