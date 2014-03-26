package models

import org.scalatest.{Matchers, WordSpec}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.util.Try
import scala.language.postfixOps

class DayMonthYearSpec extends WordSpec with Matchers {
  "DayMonthYear" should {
    "return the correct 'yyyy-MM-dd' date format" in {
      val dmy = DayMonthYear(1, 1, 1963)
      dmy.`yyyy-MM-dd` shouldEqual "1963-01-01"
    }

    "subtract 1 day from 26-6-2010 to give 25-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 1 day) shouldEqual DayMonthYear(25, 6, 2010)
    }

    "subtract 6 days from 26-6-2010 to give 20-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 days) shouldEqual DayMonthYear(20, 6, 2010)
      (dmy - 6 day) shouldEqual DayMonthYear(20, 6, 2010)
    }

    "subtract 2 weeks from 26-6-2010 to give 12-6-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 2 weeks) shouldEqual DayMonthYear(12, 6, 2010)
    }

    "subtract 5 months from 26-6-2010 to give 26-1-2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 5 months) shouldEqual DayMonthYear(26, 1, 2010)
    }

    "subtract 6 months from 26-6-2010 to give 26-12-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 6 months) shouldEqual DayMonthYear(26, 12, 2009)
    }

    "subtract 14 years from 26-6-2010 to give 26-6-1996" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (dmy - 14 years) shouldEqual DayMonthYear(26, 6, 1996)
    }

    "subtract 2 days, 4 months and 1 year from 26-6-2010 to give 24-2-2009" in {
      val dmy = DayMonthYear(26, 6, 2010)
      (((dmy - 2 days) - 4 months) - 1 year) shouldEqual DayMonthYear(24, 2, 2009)
    }

    "Format to dd/MM/yyyy of 26-6-2010 should give 26/06/2010" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.`dd/MM/yyyy` shouldEqual "26/06/2010"
    }

    "Format to yyyy-MM-dd of 26-6-2010 should give 2010-06-26" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.`yyyy-MM-dd` shouldEqual "2010-06-26"
    }

    "Format to yyyy-MM-dd of empty DayMonthYear should give empty" in {
      val dmy = DayMonthYear(0, 0, 0)
      dmy.`yyyy-MM-dd` should equal("")
    }

    "Format to yyyy-MM-dd'T'HH:mm:00" in {
      val dmy = DayMonthYear(30, 5, 2002, Some(9), Some(45))
      dmy.`yyyy-MM-dd'T'HH:mm:00` should equal("2002-05-30T09:45:00")
    }

    "have 7 digits given" in {
      val dmy = DayMonthYear(26, 6, 2010)
      dmy.numberOfCharactersInput shouldEqual 7
    }

    "accept a Joda DateTime" in {
      val dmy = DayMonthYear.from(new DateTime(2013, 2, 23, 0, 0))

      dmy.day should equal(23)
      dmy.month should equal(2)
      dmy.year should equal(2013)
    }

    "format as '23 September, 2013' " in {
      val dmy = DayMonthYear(23, 9, 2013)
      dmy.`dd month, yyyy` shouldEqual "23 September, 2013"
    }

    "include time" in {
      val dmyWithTime = DayMonthYear(23, 9, 2013).withTime(hour = 14, minutes = 55)
      dmyWithTime shouldEqual DayMonthYear(23, 9, 2013, Some(14), Some(55))
    }

    """accept format "01 September, 2001" """ in {
      Try(DateTimeFormat.forPattern("dd MMMM, yyyy").parseDateTime("01 September, 2001")).isSuccess should equal(true)
    }

    """accept format "01 September 2001" """ in {
      Try(DateTimeFormat.forPattern("dd MMMM yyyy").parseDateTime("01 September 2001")).isSuccess should equal(true)
    }

    """reject format "31 February 2001" """ in {
      Try(DateTimeFormat.forPattern("dd MMMM yyyy").parseDateTime("31 February 2001")).isFailure should equal(true)
    }

    "convert to a valid date time" in {
      val year = 2000
      val month = 11
      val day = 25
      val dayMonthYear = DayMonthYear(day = day, month = month, year = year)
      dayMonthYear.toDateTime.isEmpty should equal(false) // Indicates we get a Some[T] back from the Option[T]
      dayMonthYear.toDateTime.get should equal(new DateTime(year, month, day, 0, 0))
    }

    "not convert to a valid date time when DayMonthYear contains invalid day" in {
      val dayMonthYear = DayMonthYear(day = 99, month = 11, year = 2000)
      dayMonthYear.toDateTime.isEmpty should equal(true) // Indicates we get a None back from the Option[T]
    }

    "not convert to a valid date time when DayMonthYear contains invalid month" in {
      val dayMonthYear = DayMonthYear(day = 25, month = 99, year =2000)
      dayMonthYear.toDateTime.isEmpty should equal(true)
    }

    "not convert to a valid date time when DayMonthYear contains invalid year" in {
      val dayMonthYear = DayMonthYear(day = 25, month = 99, year =20001)
      dayMonthYear.toDateTime.isEmpty should equal(true)
    }

  }
}