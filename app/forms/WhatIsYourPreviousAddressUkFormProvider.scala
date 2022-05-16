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
import models.PreviousAddressUk

import java.time.LocalDate

class WhatIsYourPreviousAddressUkFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousAddressUk] = Form(
     mapping(
       "addressLine1" -> text("whatIsYourPreviousAddressUk.error.addressLine1.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine1.length")),
       "addressLine2" -> optional(text("whatIsYourPreviousAddressUk.error.addressLine2.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine2.length"))),
       "addressLine3" -> optional(text("whatIsYourPreviousAddressUk.error.addressLine3.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine3.length"))),
       "postcode" -> text("whatIsYourPreviousAddressUk.error.postcode.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.postcode.length")),
       "from" -> localDate(
         invalidKey = "whatIsYourPreviousAddressUk.error.from.invalid",
         allRequiredKey = "whatIsYourPreviousAddressUk.error.from.required.all",
         twoRequiredKey = "whatIsYourPreviousAddressUk.error.from.required.two",
         requiredKey    = "whatIsYourPreviousAddressUk.error.from.required"
       ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousAddressUk.error.from.past")),
       "to" -> localDate(
         invalidKey = "whatIsYourPreviousAddressUk.error.to.invalid",
         allRequiredKey = "whatIsYourPreviousAddressUk.error.to.required.all",
         twoRequiredKey = "whatIsYourPreviousAddressUk.error.to.required.two",
         requiredKey    = "whatIsYourPreviousAddressUk.error.to.required"
       ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousAddressUk.error.to.past"))
    )(PreviousAddressUk.apply)(PreviousAddressUk.unapply)
       .verifying("whatIsYourPreviousAddressUk.error.datesOutOfOrder", x => {
         x.from isBefore x.to
       })
   )
 }
