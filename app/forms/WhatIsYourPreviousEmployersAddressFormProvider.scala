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
import models.PreviousEmployersAddress

import java.time.LocalDate

class WhatIsYourPreviousEmployersAddressFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousEmployersAddress] = Form(
     mapping(
       "addressLine1" -> text("whatIsYourPreviousEmployersAddress.error.addressLine1.required")
         .verifying(maxLength(100, "whatIsYourPreviousEmployersAddress.error.addressLine1.length")),
       "addressLine2" -> text("whatIsYourPreviousEmployersAddress.error.addressLine2.required")
         .verifying(maxLength(100, "whatIsYourPreviousEmployersAddress.error.addressLine2.length")),
       "addressLine3" -> optional(text("whatIsYourPreviousEmployersAddress.error.addressLine3.required")
         .verifying(maxLength(100, "whatIsYourPreviousEmployersAddress.error.addressLine3.length"))),
       "from" -> localDate(
          invalidKey = "whatIsYourPreviousEmployersAddress.error.from.invalid",
          allRequiredKey = "whatIsYourPreviousEmployersAddress.error.from.required.all",
          twoRequiredKey = "whatIsYourPreviousEmployersAddress.error.from.required.two",
          requiredKey    = "whatIsYourPreviousEmployersAddress.error.from.required"
        ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousEmployersAddress.error.from.past")),
       "to" -> localDate(
         invalidKey = "whatIsYourPreviousEmployersAddress.error.to.invalid",
         allRequiredKey = "whatIsYourPreviousEmployersAddress.error.to.required.all",
         twoRequiredKey = "whatIsYourPreviousEmployersAddress.error.to.required.two",
         requiredKey    = "whatIsYourPreviousEmployersAddress.error.to.required"
       ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousEmployersAddress.error.to.past"))
     )(PreviousEmployersAddress.apply)(PreviousEmployersAddress.unapply)
   )
 }
