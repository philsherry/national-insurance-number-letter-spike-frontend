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

import forms.behaviours.{DateBehaviours, IntFieldBehaviours, StringFieldBehaviours}
import models.PreviousAddressUk
import play.api.data.FormError

import java.time.{LocalDate, YearMonth, ZoneOffset}

class WhatIsYourPreviousAddressUkFormProviderSpec extends StringFieldBehaviours with DateBehaviours with IntFieldBehaviours {

  val form = new WhatIsYourPreviousAddressUkFormProvider()()

  ".addressLine1" - {

    val fieldName = "addressLine1"
    val requiredKey = "whatIsYourPreviousAddressUk.error.addressLine1.required"
    val lengthKey = "whatIsYourPreviousAddressUk.error.addressLine1.length"
    val maxLength = 100

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".addressLine2" - {

    val fieldName = "addressLine2"
    val lengthKey = "whatIsYourPreviousAddressUk.error.addressLine2.length"
    val maxLength = 100

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )
  }

  ".addressLine3" - {

    val fieldName = "addressLine3"
    val lengthKey = "whatIsYourPreviousAddressUk.error.addressLine3.length"
    val maxLength = 100

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )
  }

  ".postcode" - {

    val fieldName = "postcode"
    val requiredKey = "whatIsYourPreviousAddressUk.error.postcode.required"
    val lengthKey = "whatIsYourPreviousAddressUk.error.postcode.length"
    val maxLength = 100

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".from" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    ).map(YearMonth.from(_))

    behave like yearMonthField(form, "from", validData)

    ".month" - {
      behave like mandatoryField(form, "from.month", FormError("from.month", "whatIsYourPreviousAddressUk.error.from.month.required"))

      behave like intFieldWithRange(form, "from.month", 1, 12, FormError("from.month", "whatIsYourPreviousAddressUk.error.from.month.range", List(1, 12)))
    }

    ".year" - {
      behave like mandatoryField(form, "from.year", FormError("from.year", "whatIsYourPreviousAddressUk.error.from.year.required"))

      behave like intFieldWithRange(form, "from.year", 1900, LocalDate.now().getYear, FormError("from.year", "whatIsYourPreviousAddressUk.error.from.year.range", List(1900, LocalDate.now().getYear)))
    }

  }

  ".to" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    ).map(YearMonth.from(_))

    behave like yearMonthField(form, "to", validData)

    ".month" - {
      behave like mandatoryField(form, "to.month", FormError("to.month", "whatIsYourPreviousAddressUk.error.to.month.required"))

      behave like intFieldWithRange(form, "to.month", 1, 12, FormError("to.month", "whatIsYourPreviousAddressUk.error.to.month.range", List(1, 12)))
    }

    ".year" - {
      behave like mandatoryField(form, "to.year", FormError("to.year", "whatIsYourPreviousAddressUk.error.to.year.required"))

      behave like intFieldWithRange(form, "to.year", 1, LocalDate.now().getYear, FormError("to.year", "whatIsYourPreviousAddressUk.error.to.year.range", List(1900, LocalDate.now().getYear)))
    }
  }

  "form" - {

    "must bind if start date and end date match" in {
      val date = LocalDate.now

      val data = Map(
        "from.month"   -> date.getMonthValue.toString,
        "from.year"    -> date.getYear.toString,
        "to.month"     -> date.getMonthValue.toString,
        "to.year"      -> date.getYear.toString,
        "addressLine1" -> "line 1",
        "postcode"     -> "postcode"
      )

      form.bind(data).value.value mustBe PreviousAddressUk("line 1", None, None, "postcode", YearMonth.from(date), YearMonth.from(date))
    }

    "must give an error if start date is not before end date" in {

      val startDate = LocalDate.now
      val endDate   = startDate.minusMonths(1)

      val data = Map(
        "from.month"   -> startDate.getMonthValue.toString,
        "from.year"    -> startDate.getYear.toString,
        "to.month"     -> endDate.getMonthValue.toString,
        "to.year"      -> endDate.getYear.toString,
        "addressLine1" -> "line 1",
        "postcode"     -> "postcode"
      )

      val result = form.bind(data)
      result.errors must contain only FormError("", "whatIsYourPreviousAddressUk.error.datesOutOfOrder")
    }

    "must give an error if start date is in the future" in {

      val startDate = YearMonth.now().plusMonths(1)
      val endDate = YearMonth.now()

      val data = Map(
        "from.month"   -> startDate.getMonthValue.toString,
        "from.year"    -> startDate.getYear.toString,
        "to.month"     -> endDate.getMonthValue.toString,
        "to.year"      -> endDate.getYear.toString,
        "addressLine1" -> "line 1",
        "postcode"     -> "postcode"
      )

      val result = form.bind(data)
      result.errors must contain(FormError("", "whatIsYourPreviousAddressUk.error.dateInFuture"))

    }

    "must give an error if end date is in the future" in {

      val startDate = YearMonth.now()
      val endDate = YearMonth.now().plusMonths(1)

      val data = Map(
        "from.month"   -> startDate.getMonthValue.toString,
        "from.year"    -> startDate.getYear.toString,
        "to.month"     -> endDate.getMonthValue.toString,
        "to.year"      -> endDate.getYear.toString,
        "addressLine1" -> "line 1",
        "postcode"     -> "postcode"
      )

      val result = form.bind(data)
      result.errors must contain(FormError("", "whatIsYourPreviousAddressUk.error.dateInFuture"))

    }
  }
}
