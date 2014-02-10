package models

import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

case class DayMonthYear(day: Option[Int], month: Option[Int], year: Option[Int],
                        hour: Option[Int] = None, minutes: Option[Int] = None) {
  def withTime(hour: Int, minutes: Int) = copy(hour = Some(hour), minutes = Some(minutes))

  def `yyyy-MM-dd`: String = format("yyyy-MM-dd")

  def `dd month, yyyy`: String = format("dd MMMM, yyyy")

  def `yyyy-MM-dd'T'HH:mm:00`: String = format("yyyy-MM-dd'T'HH:mm:00")

  def `dd/MM/yyyy`: String = pad(day) + "/" + pad(month) + "/" + year.fold("")(_.toString)

  def -(amount: Int) = new Period {
    override def days = adjust { _.minusDays(amount) }

    override def weeks = adjust { _.minusWeeks(amount) }

    override def months = adjust { _.minusMonths(amount) }

    override def years = adjust { _.minusYears(amount) }
  }

  def numberOfCharactersInput = List(day, month, year, hour, minutes).foldLeft(0) { (x, i) => x + i.fold(0)(_.toString.length) }

  private def pad(i: Option[Int]): String = i.fold("")(i => if (i < 10) s"0$i" else s"$i")

  private def adjust(f: DateTime => DateTime) = Try(new DateTime(year.get, month.get, day.get, 0, 0)) match {
    case Success(dt: DateTime) => {
      val newDateTime = f(dt)
      DayMonthYear(Some(newDateTime.dayOfMonth().get), Some(newDateTime.monthOfYear().get), Some(newDateTime.year().get), hour, minutes)
    }
    case Failure(_) => this
  }

  private def format(pattern: String): String = Try(new DateTime(year.getOrElse(0), month.getOrElse(0), day.getOrElse(0), hour.getOrElse(0), minutes.getOrElse(0))) match {
    case Success(dt: DateTime) => DateTimeFormat.forPattern(pattern).print(dt)
    case Failure(_) => ""
  }
}

object DayMonthYearComparator extends Ordering[Option[DayMonthYear]]{
  def compare(a:Option[DayMonthYear],b:Option[DayMonthYear]): Int = {
    (Try(new DateTime(a.get.year.get, a.get.month.get, a.get.day.get, a.get.hour.get, a.get.minutes.get)) match {
      case Success(dt: DateTime) => dt
      case Failure(_) => new DateTime()
    }).compareTo(
        Try(new DateTime(b.get.year.get, b.get.month.get, b.get.day.get, b.get.hour.get, b.get.minutes.get)) match {
          case Success(dt: DateTime) => dt
          case Failure(_) => new DateTime()
        })
  }
}

object DayMonthYear {
  import scala.language.implicitConversions

  implicit def dateTimeToDayMonthYear(dt: DateTime) = apply(dt)

  def apply() = new DayMonthYear(Some(1), Some(1), Some(1970))

  def apply(day: Int, month: Int, year: Int) = new DayMonthYear(Some(day), Some(month), Some(year))

  def apply(dt: DateTime) = {
    new DayMonthYear(Some(dt.dayOfMonth().get), Some(dt.monthOfYear().get), Some(dt.year().get), Some(dt.hourOfDay().get()), Some(dt.minuteOfHour().get))
  }

  def today = {
    val now = DateTime.now()
    new DayMonthYear(Some(now.dayOfMonth().get), Some(now.monthOfYear().get), Some(now.year().get))
  }
}

sealed trait Period {
  def day: DayMonthYear = days

  def days: DayMonthYear

  def week: DayMonthYear = weeks

  def weeks: DayMonthYear

  def month: DayMonthYear = months

  def months: DayMonthYear

  def year: DayMonthYear = years

  def years: DayMonthYear
}
