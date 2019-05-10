package services

import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject._

import play.api.Logger

/**
  * Represent a measure as sent from the device.
  *
  * @param udid: Srting - user device id
  * @param gpsDD: GpsDD - coordinates of the fireman at a given time
  * @param temperatureIn: Float - temperature inside the fireman's suit
  * @param temperatureOut: float - temperature outside the fireman's suit
  * @param time: Timestamp - time at which the measurement was done, follows ISO 8601
  */
case class Emergency (udid: String, metric: String, message: String, mtime: String) {


  def sqlFormat = s"($udid, $metric, $message, TIMESTAMP'${this.getTime()}')"

  def getTime(): Timestamp = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
    val parsedDate = dateFormat.parse(mtime)
    val timestamp = new Timestamp(parsedDate.getTime)
    Logger.debug(s"Successfully converted $timestamp")
    timestamp
  }

}
