package services

import java.sql.ResultSet
import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.Logger

import scala.annotation.tailrec

/**
 * Represent a measure as sent from the device.
 *
 * @param udid: Srting - user device id
 * @param longitude: Double -
 * @param latitude: Double -
 * @param temperatureIn: Float - temperature inside the fireman's suit
 * @param temperatureOut: float - temperature outside the fireman's suit
 * @param mtime: Timestamp - time at which the measurement was done, follows ISO 8601
 */
case class Measurement (udid: Int, longitude: Double, latitude: Double,
  temperatureIn: Float, temperatureOut: Float, mtime: String) {

    def sqlFormat = s"($udid, $longitude, $latitude, $temperatureIn, $temperatureOut, TIMESTAMP'${this.getTime()}')"

    def getTime(): Timestamp = {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
      val parsedDate = dateFormat.parse(mtime)
      val timestamp = new Timestamp(parsedDate.getTime)
      Logger.debug(s"Successfully converted $timestamp")
      timestamp
    }

  }

  object Measurement {
    @tailrec
    final
    def parseStream(res: ResultSet,
      accumulator: List[Measurement]): List[Measurement] = {
        if (!res.next)
          accumulator.reverse
        else {
          val value = Measurement(res.getInt("udid"),
            res.getDouble("longitude"),
            res.getDouble("latitude"),
            res.getFloat("temperaturein"),
            res.getFloat("temperatureout"),
            res.getString("mtime"))
          parseStream(res, value +: accumulator)
        }
    }
  }
