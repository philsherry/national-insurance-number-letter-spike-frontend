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
import models.{Country, CurrentAddressInternational}
import play.api.i18n.Messages

class WhatIsYourCurrentAddressInternationalFormProvider @Inject() extends Mappings {

   def apply()(implicit messages: Messages): Form[CurrentAddressInternational] = Form(
     mapping(
      "addressLine1" -> text("whatIsYourCurrentAddressInternational.error.addressLine1.required")
        .verifying(maxLength(100, "whatIsYourCurrentAddressInternational.error.addressLine1.length")),
      "addressLine2" -> optional(text("whatIsYourCurrentAddressInternational.error.addressLine2.required")
        .verifying(maxLength(100, "whatIsYourCurrentAddressInternational.error.addressLine2.length"))),
       "addressLine3" -> optional(text("whatIsYourCurrentAddressInternational.error.addressLine3.required")
         .verifying(maxLength(100, "whatIsYourCurrentAddressInternational.error.addressLine3.length"))),
       "postcode" -> optional(text("whatIsYourCurrentAddressInternational.error.postcode.required")
         .verifying(maxLength(12, "whatIsYourCurrentAddressInternational.error.postcode.length"))),
       "country" -> text("whatIsYourCurrentAddressInternational.error.country.required")
         .verifying("whatIsYourCurrentAddressInternational.error.country.required", c => Country.internationalCountries.exists(_.code == c))
         .transform[Country](value => Country.internationalCountries.find(_.code == value).get, _.code)
     )(CurrentAddressInternational.apply)(CurrentAddressInternational.unapply)
   )
 }
