package common

import play.api.mvc.Cookie

object ClientSideSessionFactory {
  final val TrackingIdCookieName = "tracking_id"
}

trait ClientSideSessionFactory {
  def getSession(request: Traversable[Cookie]): ClientSideSession

  def newSessionCookiesIfNeeded(request: Traversable[Cookie]): Option[Seq[Cookie]]
}

