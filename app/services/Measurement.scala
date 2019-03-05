package services

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.Logger

/**
  * Represent gps coordinates following the DD format.
  *
  * @param longitude: Double
  * @param latitude : Double
  */
case class GpsDD(longitude: Double, latitude: Double) {
}

/**
  * Represent a measure as sent from the device.
  *
  * @param udid: Srting - user device id
  * @param gpsDD: GpsDD - coordinates of the fireman at a given time
  * @param temperatureIn: Float - temperature inside the fireman's suit
  * @param temperatureOut: float - temperature outside the fireman's suit
  * @param time: Timestamp - time at which the measurement was done, follows ISO 8601
  */
case class Measurement (udid: String, gpsDD: GpsDD, temperatureIn: Float, temperatureOut: Float, time: String) {

  def save() = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
    val parsedDate = dateFormat.parse(time)
    val timestamp = new Timestamp(parsedDate.getTime)
    Logger.debug(s"Successfully created $timestamp")
  }

}
