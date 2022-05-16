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

package forms

import forms.mappings.Mappings
import models.PreviousMarriageOrPartnershipDetails
import play.api.data.Form
import play.api.data.Forms._

import java.time.LocalDate
import javax.inject.Inject

class PreviousMarriageOrPartnershipDetailsFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousMarriageOrPartnershipDetails] = Form(
     mapping(
       "startDate" -> localDate(
         invalidKey     = "previousMarriageOrPartnershipDetails.error.startDate.invalid",
         allRequiredKey = "previousMarriageOrPartnershipDetails.error.startDate.required.all",
         twoRequiredKey = "previousMarriageOrPartnershipDetails.error.startDate.required.two",
         requiredKey    = "previousMarriageOrPartnershipDetails.error.startDate.required"
       ).verifying(maxDate(LocalDate.now, "previousMarriageOrPartnershipDetails.error.startDate.past")),
       "endDate" -> localDate(
          invalidKey     = "previousMarriageOrPartnershipDetails.error.endDate.invalid",
          allRequiredKey = "previousMarriageOrPartnershipDetails.error.endDate.required.all",
          twoRequiredKey = "previousMarriageOrPartnershipDetails.error.endDate.required.two",
          requiredKey    = "previousMarriageOrPartnershipDetails.error.endDate.required"
       ).verifying(maxDate(LocalDate.now, "previousMarriageOrPartnershipDetails.error.endDate.past")),
      "endReason" -> text("previousMarriageOrPartnershipDetails.error.endReason.required")
        .verifying(maxLength(100, "previousMarriageOrPartnershipDetails.error.endReason.length"))
    )(PreviousMarriageOrPartnershipDetails.apply)(PreviousMarriageOrPartnershipDetails.unapply)
       .verifying("previousMarriageOrPartnershipDetails.error.datesOutOfOrder", x => {
         x.startDate isBefore x.endDate
       })
   )
 }
