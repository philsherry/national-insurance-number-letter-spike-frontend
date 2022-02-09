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
import play.api.data.FormError

import java.time.{LocalDate, ZoneOffset}

class PreviousMarriageOrPartnershipDetailsFormProviderSpec extends StringFieldBehaviours with DateBehaviours {

  val form = new PreviousMarriageOrPartnershipDetailsFormProvider()()

  ".startDate" - {

    val fieldName = "startDate"
    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, fieldName, validData)

    behave like mandatoryDateField(form, fieldName, "previousMarriageOrPartnershipDetails.error.startDate.required.all")

    behave like dateFieldWithMax(form, fieldName, LocalDate.now, FormError(fieldName, "previousMarriageOrPartnershipDetails.error.startDate.past"))
  }

  ".endDate" - {

    val fieldName = "endDate"
    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, fieldName, validData)

    behave like mandatoryDateField(form, fieldName, "previousMarriageOrPartnershipDetails.error.endDate.required.all")

    behave like dateFieldWithMax(form, fieldName, LocalDate.now, FormError(fieldName, "previousMarriageOrPartnershipDetails.error.endDate.past"))
  }

  ".endReason" - {

    val fieldName = "endReason"
    val requiredKey = "previousMarriageOrPartnershipDetails.error.endReason.required"
    val lengthKey = "previousMarriageOrPartnershipDetails.error.endReason.length"
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
}
