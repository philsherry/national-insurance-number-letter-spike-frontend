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

import forms.behaviours.StringFieldBehaviours
import models.CurrentAddressUk
import play.api.data.FormError

class WhatIsYourCurrentAddressUkFormProviderSpec extends StringFieldBehaviours {

  val form = new WhatIsYourCurrentAddressUkFormProvider()()

  ".addressLine1" - {

    val fieldName = "addressLine1"
    val requiredKey = "whatIsYourCurrentAddressUk.error.addressLine1.required"
    val lengthKey = "whatIsYourCurrentAddressUk.error.addressLine1.length"
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
    val lengthKey = "whatIsYourCurrentAddressUk.error.addressLine2.length"
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
    val requiredKey = "whatIsYourCurrentAddressUk.error.addressLine3.required"
    val lengthKey = "whatIsYourCurrentAddressUk.error.addressLine3.length"
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

  ".postcode" - {

    val fieldName = "postcode"
    val requiredKey = "whatIsYourCurrentAddressUk.error.postcode.required"
    val lengthKey = "whatIsYourCurrentAddressUk.error.postcode.length"
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

  "unapply" - {

    "must leave line 2 and line 3 unchanged if line 3 is present" in {
      val address = CurrentAddressUk("line 1", Some("line 2"), Some("line 3"), "postcode")

      val filled = form.fill(address)

      filled("addressLine2").value mustBe Some("line 2")
      filled("addressLine3").value mustBe Some("line 3")
    }

    "must swap line 2 to line 3 if line 3 is not present" in {
      val address = CurrentAddressUk("line 1", Some("line 2"), None, "postcode")

      val filled = form.fill(address)

      filled("addressLine2").value mustBe None
      filled("addressLine3").value mustBe Some("line 2")
    }

  }
}
