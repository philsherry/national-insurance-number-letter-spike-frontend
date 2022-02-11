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

class WhatIsYourPreviousEmployersAddressFormProviderSpec extends StringFieldBehaviours with DateBehaviours {

  val form = new WhatIsYourPreviousEmployersAddressFormProvider()()

  ".addressLine1" - {

    val fieldName = "addressLine1"
    val requiredKey = "whatIsYourPreviousEmployersAddress.error.addressLine1.required"
    val lengthKey = "whatIsYourPreviousEmployersAddress.error.addressLine1.length"
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
    val requiredKey = "whatIsYourPreviousEmployersAddress.error.addressLine2.required"
    val lengthKey = "whatIsYourPreviousEmployersAddress.error.addressLine2.length"
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
    val lengthKey = "whatIsYourPreviousEmployersAddress.error.addressLine3.length"
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
}
