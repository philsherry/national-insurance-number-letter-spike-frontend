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

import forms.behaviours.{DateBehaviours, StringFieldBehaviours}
import models.Country
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import play.api.data.FormError
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}

import java.time.{LocalDate, ZoneOffset}

class WhatIsYourPreviousAddressInternationalFormProviderSpec extends StringFieldBehaviours with DateBehaviours {

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
    )

    behave like dateField(form, "from", validData)

    behave like mandatoryDateField(form, "from", "whatIsYourPreviousAddressInternational.error.from.required.all")

    behave like dateFieldWithMax(form, "from", LocalDate.now, FormError("from", "whatIsYourPreviousAddressInternational.error.from.past"))
  }

  ".to" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "from", validData)

    behave like mandatoryDateField(form, "from", "whatIsYourPreviousAddressInternational.error.from.required.all")

    behave like dateFieldWithMax(form, "from", LocalDate.now, FormError("from", "whatIsYourPreviousAddressInternational.error.from.past"))
  }

  "form" - {

    "must give an error if start date is not before end date" in {

      val startDate = LocalDate.now
      val endDate   = startDate.minusDays(1)

      val data = Map(
        "from.day"     -> startDate.getDayOfMonth.toString,
        "from.month"   -> startDate.getMonthValue.toString,
        "from.year"    -> startDate.getYear.toString,
        "to.day"       -> endDate.getDayOfMonth.toString,
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
