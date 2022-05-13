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
import models.PreviousAddressInternational

import java.time.LocalDate

class WhatIsYourPreviousAddressInternationalFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousAddressInternational] = Form(
     mapping(
      "addressLine1" -> text("whatIsYourPreviousAddressInternational.error.addressLine1.required")
        .verifying(maxLength(100, "whatIsYourPreviousAddressInternational.error.addressLine1.length")),
      "addressLine2" -> optional(text("whatIsYourPreviousAddressInternational.error.addressLine2.required")
        .verifying(maxLength(100, "whatIsYourPreviousAddressInternational.error.addressLine2.length"))),
       "addressLine3" -> optional(text("whatIsYourPreviousAddressInternational.error.addressLine3.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressInternational.error.addressLine3.length"))),
       "postcode" -> optional(text("whatIsYourPreviousAddressInternational.error.postcode.required")
         .verifying(maxLength(12, "whatIsYourPreviousAddressInternational.error.postcode.length"))),
       "country" -> text("whatIsYourPreviousAddressInternational.error.country.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressInternational.error.country.length")),
       "from" -> localDate(
         invalidKey = "whatIsYourPreviousAddressInternational.error.from.invalid",
         allRequiredKey = "whatIsYourPreviousAddressInternational.error.from.required.all",
         twoRequiredKey = "whatIsYourPreviousAddressInternational.error.from.required.two",
         requiredKey    = "whatIsYourPreviousAddressInternational.error.from.required"
       ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousAddressInternational.error.from.past")),
       "to" -> localDate(
         invalidKey = "whatIsYourPreviousAddressInternational.error.to.invalid",
         allRequiredKey = "whatIsYourPreviousAddressInternational.error.to.required.all",
         twoRequiredKey = "whatIsYourPreviousAddressInternational.error.to.required.two",
         requiredKey    = "whatIsYourPreviousAddressInternational.error.to.required"
       ).verifying(maxDate(LocalDate.now, "whatIsYourPreviousAddressInternational.error.to.past"))
     )(PreviousAddressInternational.apply)(PreviousAddressInternational.unapply)
   )
 }
