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
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import viewmodels.govuk.checkbox._

sealed trait AlternativeDocuments

object AlternativeDocuments extends Enumerable.Implicits {

  case object AdoptionCertificate extends WithName("adoption-certificate") with AlternativeDocuments
  case object HomeOfficeOrTravelDocument extends WithName("home-office-or-travel-document") with AlternativeDocuments
  case object WorkPermit extends WithName("work-permit") with AlternativeDocuments
  case object CertificateOfNaturalisation extends WithName("certificate-of-naturalisation") with AlternativeDocuments
  case object MarriageOrCivilPartnershipCertificate extends WithName("marriage-or-civil-partnership-certificate") with AlternativeDocuments
  case object ArmyOrNavyCertificate extends WithName("army-or-navy-certificate") with AlternativeDocuments
  case object IdentityOrMedicalCard extends WithName("identity-or-medical-card") with AlternativeDocuments


  val values: Seq[AlternativeDocuments] = Seq(
    AdoptionCertificate, HomeOfficeOrTravelDocument, WorkPermit, CertificateOfNaturalisation,
    MarriageOrCivilPartnershipCertificate, IdentityOrMedicalCard, ArmyOrNavyCertificate
  )

  def checkboxItems(implicit messages: Messages): Seq[CheckboxItem] =
    values.zipWithIndex.map {
      case (value, index) =>
        CheckboxItemViewModel(
          content = Text(messages(s"whichAlternativeDocuments.${value.toString}")),
          fieldId = "value",
          index   = index,
          value   = value.toString
        )
    }

  implicit val enumerable: Enumerable[AlternativeDocuments] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
