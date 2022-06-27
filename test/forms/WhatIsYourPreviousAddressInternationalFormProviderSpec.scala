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
import models.{Country, PreviousAddressInternational}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import play.api.data.FormError
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}

import java.time.{LocalDate, YearMonth, ZoneOffset}

class WhatIsYourPreviousAddressInternationalFormProviderSpec extends StringFieldBehaviours with DateBehaviours with IntFieldBehaviours {

  private implicit val msgs: Messages = stubMessages(stubMessagesApi())
  val form = new WhatIsYourPreviousAddressInternationalFormProvider()()

  ".addressLine1" - {

    val fieldName = "addressLine1"
    val requiredKey = "whatIsYourPreviousAddressInternational.error.addressLine1.required"
    val lengthKey = "whatIsYourPreviousAddressInternational.error.addressLine1.length"
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
    val lengthKey = "whatIsYourPreviousAddressInternational.error.addressLine2.length"
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
    val lengthKey = "whatIsYourPreviousAddressInternational.error.addressLine3.length"
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

  ".country" - {

    val fieldName = "country"
    val requiredKey = "whatIsYourPreviousAddressInternational.error.country.required"


    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf(Country.internationalCountries.map(_.code.toUpperCase))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "must not bind any values other than valid country codes" in {

      val invalidAnswers =
        arbitrary[String] suchThat (x => !Country.countryCodes.map(_.toUpperCase).contains(x))

      forAll(invalidAnswers) {
        answer =>
          val result = form.bind(Map("value" -> answer)).apply(fieldName)
          result.errors must contain only FormError(fieldName, requiredKey)
      }
    }
  }

  ".from" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    ).map(YearMonth.from(_))

    behave like yearMonthField(form, "from", validData)

    ".month" - {
      behave like mandatoryField(form, "from.month", FormError("from.month", "whatIsYourPreviousAddressInternational.error.from.month.required"))

      behave like intFieldWithRange(form, "from.month", 1, 12, FormError("from.month", "whatIsYourPreviousAddressInternational.error.from.month.range", List(1, 12)))
    }

    ".year" - {
      behave like mandatoryField(form, "from.year", FormError("from.year", "whatIsYourPreviousAddressInternational.error.from.year.required"))

      behave like intFieldWithRange(form, "from.year", 1900, LocalDate.now().getYear, FormError("from.year", "whatIsYourPreviousAddressInternational.error.from.year.range", List(1900, LocalDate.now().getYear)))
    }
  }

  ".to" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    ).map(YearMonth.from(_))

    behave like yearMonthField(form, "to", validData)

    ".month" - {
      behave like mandatoryField(form, "to.month", FormError("to.month", "whatIsYourPreviousAddressInternational.error.to.month.required"))

      behave like intFieldWithRange(form, "to.month", 1, 12, FormError("to.month", "whatIsYourPreviousAddressInternational.error.to.month.range", List(1, 12)))
    }

    ".year" - {
      behave like mandatoryField(form, "to.year", FormError("to.year", "whatIsYourPreviousAddressInternational.error.to.year.required"))

      behave like intFieldWithRange(form, "to.year", 1, LocalDate.now().getYear, FormError("to.year", "whatIsYourPreviousAddressInternational.error.to.year.range", List(1900, LocalDate.now().getYear)))
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
        "country"     -> "FR"
      )

      form.bind(data).value.value mustBe PreviousAddressInternational("line 1", None, None, None, Country("FR", "country.fr.text"), YearMonth.from(date), YearMonth.from(date))
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
        "country"      -> "FR"
      )

      val result = form.bind(data)
      result.errors must contain only FormError("", "whatIsYourPreviousAddressInternational.error.datesOutOfOrder")
    }
  }
}
