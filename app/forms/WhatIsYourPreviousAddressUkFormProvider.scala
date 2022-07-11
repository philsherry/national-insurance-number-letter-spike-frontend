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

import java.time.{LocalDate, YearMonth}

class WhatIsYourPreviousAddressUkFormProvider @Inject() extends Mappings {

   def apply(): Form[PreviousAddressUk] = Form(
     mapping(
       "addressLine1" -> text("whatIsYourPreviousAddressUk.error.addressLine1.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine1.length")),
       "addressLine2" -> optional(text("whatIsYourPreviousAddressUk.error.addressLine2.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine2.length"))),
       "addressLine3" -> text("whatIsYourPreviousAddressUk.error.addressLine3.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.addressLine3.length")),
       "postcode" -> text("whatIsYourPreviousAddressUk.error.postcode.required")
         .verifying(maxLength(100, "whatIsYourPreviousAddressUk.error.postcode.length")),
       "from.month" -> int(
         "whatIsYourPreviousAddressUk.error.from.month.required",
         "whatIsYourPreviousAddressUk.error.from.month.invalid.numeric",
         "whatIsYourPreviousAddressUk.error.from.month.invalid.nonNumeric"
       ).verifying(inRange(1, 12, "whatIsYourPreviousAddressUk.error.from.month.range")),
       "from.year" -> int(
         "whatIsYourPreviousAddressUk.error.from.year.required",
         "whatIsYourPreviousAddressUk.error.from.year.invalid.numeric",
         "whatIsYourPreviousAddressUk.error.from.year.invalid.nonNumeric"
       ).verifying(inRange(1900, LocalDate.now().getYear, "whatIsYourPreviousAddressUk.error.from.year.range")),
       "to.month" -> int(
         "whatIsYourPreviousAddressUk.error.to.month.required",
         "whatIsYourPreviousAddressUk.error.to.month.invalid.numeric",
         "whatIsYourPreviousAddressUk.error.to.month.invalid.nonNumeric"
       ).verifying(inRange(1, 12, "whatIsYourPreviousAddressUk.error.to.month.range")),
       "to.year" -> int(
         "whatIsYourPreviousAddressUk.error.to.year.required",
         "whatIsYourPreviousAddressUk.error.to.year.invalid.numeric",
         "whatIsYourPreviousAddressUk.error.to.year.invalid.nonNumeric"
       ).verifying(inRange(1900, LocalDate.now().getYear, "whatIsYourPreviousAddressUk.error.to.year.range")),
    ){ (line1, line2, line3, postcode, fromMonth, fromYear, toMonth, toYear) =>
       PreviousAddressUk(
         line1, line2, Some(line3), postcode,
         from = YearMonth.of(fromYear, fromMonth),
         to = YearMonth.of(toYear, toMonth)
       )
     }{a =>
       // Temporary until model can be updated to enforce town or city as mandatory
       val (line2, line3) = a.addressLine3 match {
         case Some(_) => (a.addressLine2, a.addressLine3)
         case None => (None, a.addressLine2)
       }

       Some((
         a.addressLine1, line2, line3.getOrElse(""), a.postcode,
         a.from.getMonthValue, a.from.getYear,
         a.to.getMonthValue, a.to.getYear
       ))
     }
       .verifying("whatIsYourPreviousAddressUk.error.dateInFuture", x => {
         !x.from.isAfter(YearMonth.now()) && !x.to.isAfter(YearMonth.now())
       })
       .verifying("whatIsYourPreviousAddressUk.error.datesOutOfOrder", x => {
       (x.from isBefore x.to) || (x.from == x.to)
     })
   )
 }
