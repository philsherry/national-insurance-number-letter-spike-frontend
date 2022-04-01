package pages

import models.Index
import play.api.libs.json.{JsPath, JsValue}
import queries.Settable

final case class PreviousEmployerQuery(index: Index) extends Settable[JsValue] {

  override def path: JsPath = JsPath \ "previousEmployer" \ index.position
}
