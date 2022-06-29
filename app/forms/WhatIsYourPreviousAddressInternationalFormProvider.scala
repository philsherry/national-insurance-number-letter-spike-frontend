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
import models.{Country, PreviousAddressInternational}
import play.api.i18n.Messages

import java.time.{LocalDate, YearMonth}

class WhatIsYourPreviousAddressInternationalFormProvider @Inject() extends Mappings {

   def apply()(implicit messages: Messages): Form[PreviousAddressInternational] = Form(
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
         .verifying("whatIsYourPreviousAddressInternational.error.country.required", c => Country.internationalCountries.exists(_.code == c))
         .transform[Country](value => Country.internationalCountries.find(_.code == value).get, _.code),
       "from.month" -> int(
         "whatIsYourPreviousAddressInternational.error.from.month.required",
         "whatIsYourPreviousAddressInternational.error.from.month.invalid.numeric",
         "whatIsYourPreviousAddressInternational.error.from.month.invalid.nonNumeric"
       ).verifying(inRange(1, 12, "whatIsYourPreviousAddressInternational.error.from.month.range")),
       "from.year" -> int(
         "whatIsYourPreviousAddressInternational.error.from.year.required",
         "whatIsYourPreviousAddressInternational.error.from.year.invalid.numeric",
         "whatIsYourPreviousAddressInternational.error.from.year.invalid.nonNumeric"
       ).verifying(inRange(1900, LocalDate.now().getYear, "whatIsYourPreviousAddressInternational.error.from.year.range")),
       "to.month" -> int(
         "whatIsYourPreviousAddressInternational.error.to.month.required",
         "whatIsYourPreviousAddressInternational.error.to.month.invalid.numeric",
         "whatIsYourPreviousAddressInternational.error.to.month.invalid.nonNumeric"
       ).verifying(inRange(1, 12, "whatIsYourPreviousAddressInternational.error.to.month.range")),
       "to.year" -> int(
         "whatIsYourPreviousAddressInternational.error.to.year.required",
         "whatIsYourPreviousAddressInternational.error.to.year.invalid.numeric",
         "whatIsYourPreviousAddressInternational.error.to.year.invalid.nonNumeric"
       ).verifying(inRange(1900, LocalDate.now().getYear, "whatIsYourPreviousAddressInternational.error.to.year.range")),
     ){ (line1, line2, line3, postcode, country, fromMonth, fromYear, toMonth, toYear) =>
       PreviousAddressInternational(
         line1, line2, line3, postcode, country,
         from = YearMonth.of(fromYear, fromMonth),
         to = YearMonth.of(toYear, toMonth)
       )
     }(a => Some((
       a.addressLine1, a.addressLine2, a.addressLine3, a.postcode, a.country,
       a.from.getYear, a.from.getMonthValue,
       a.to.getYear, a.to.getMonthValue
     )))
       .verifying("whatIsYourPreviousAddressInternational.error.dateInFuture", x => {
         !x.from.isAfter(YearMonth.now()) && !x.to.isAfter(YearMonth.now())
       })
       .verifying("whatIsYourPreviousAddressInternational.error.datesOutOfOrder", x => {
       (x.from isBefore x.to) || (x.from == x.to)
     })
   )
 }
