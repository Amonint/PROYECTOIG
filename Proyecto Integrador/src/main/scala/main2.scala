import play.api.libs.json._
import play.api.libs.functional.syntax._


object main2 {
  case class Language(iso: String, name: String)

  implicit val languageFormat: OFormat[Language] = (
    (__ \ "iso_639_1").format[String] and
      (__ \ "name").format[String]
    )(Language.apply, unlift(Language.unapply))

  val jsonList: JsValue = Json.parse("[{\"iso_639_1\": \"fr\", \"name\": \"Fran\u00e7ais\"}, {\"iso_639_1\": " +
    "\"en\", \"name\": \"English\"}, {\"iso_639_1\": \"es\", \"name\": \"Espa\u00f1ol\"}, {\"iso_639_1\": \"it\", \"name\":" +
    " \"Italiano\"}, {\"iso_639_1\": \"de\", \"name\": \"Deutsch\"}]")

  val names: Seq[String] = jsonList.as[List[Language]].map(_.name)
  println(s"Los nombres son: $names")

}
