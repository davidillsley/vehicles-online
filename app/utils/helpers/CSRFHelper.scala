package utils.helpers

import app.ConfigProperties._
import play.api.templates.{Html, HtmlFormat}

object CsrfHelper {

  val csrfPrevention = getProperty("csrf.prevention", default = true)

  def hiddenFormField(implicit token: services.csrf_prevention.CsrfPreventionAction.CsrfPreventionToken): Html = {
    if (csrfPrevention) {
      Html(s"""<input type="hidden" name="${services.csrf_prevention.CsrfPreventionAction.tokenName}" value="${HtmlFormat.escape(token.value)}"/>""")
    } else {
      Html("")
    }
  }
}