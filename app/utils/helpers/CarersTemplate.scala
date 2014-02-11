package utils.helpers

import views.html.helper.FieldConstructor

object CarersTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.valtechTemplate.f)
}

object EmptyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.emptyTemplate.f)
}


object ShortFieldTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldTemplate.f)
}

object CurrencyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldCurrencyTemplate.f)
}

object DatepickerTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.datepickerTemplate.f)
}

object DeclareCheckTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.declareCheckTemplate.f)
}