/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait SecondaryDocument

object SecondaryDocument extends Enumerable.Implicits {

  case object AdoptionCertificate extends WithName("adoption-certificate") with SecondaryDocument
  case object HomeOfficeOrTravelDocument extends WithName("home-office-or-travel-document") with SecondaryDocument
  case object WorkPermit extends WithName("work-permit") with SecondaryDocument
  case object CertificateOfNaturalisation extends WithName("certificate-of-naturalisation") with SecondaryDocument
  case object MarriageOrCivilPartnershipCertificate extends WithName("marriage-or-civil-partnership-certificate") with SecondaryDocument
  case object ArmyOrNavyCertificate extends WithName("army-or-navy-certificate") with SecondaryDocument
  case object IdentityOrMedicalCard extends WithName("identity-or-medical-card") with SecondaryDocument

  val values: Seq[SecondaryDocument] = Seq(
    AdoptionCertificate, HomeOfficeOrTravelDocument, WorkPermit, CertificateOfNaturalisation,
    MarriageOrCivilPartnershipCertificate, ArmyOrNavyCertificate, IdentityOrMedicalCard
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      RadioItem(
        content = Text(messages(s"whichSecondaryDocuments.${value.toString}")),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  implicit val enumerable: Enumerable[SecondaryDocument] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
