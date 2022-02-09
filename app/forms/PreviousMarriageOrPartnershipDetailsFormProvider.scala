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

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms._
import models.PreviousMarriageOrPartnershipDetails

class PreviousMarriageOrPartnershipDetailsFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousMarriageOrPartnershipDetails] = Form(
     mapping(
      "startDate" -> text("previousMarriageOrPartnershipDetails.error.startDate.required")
        .verifying(maxLength(100, "previousMarriageOrPartnershipDetails.error.startDate.length")),
      "endDate" -> text("previousMarriageOrPartnershipDetails.error.endDate.required")
        .verifying(maxLength(100, "previousMarriageOrPartnershipDetails.error.endDate.length"))
    )(PreviousMarriageOrPartnershipDetails.apply)(PreviousMarriageOrPartnershipDetails.unapply)
   )
 }
