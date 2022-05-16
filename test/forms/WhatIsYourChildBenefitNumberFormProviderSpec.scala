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
import org.scalacheck.Gen
import play.api.data.FormError

class WhatIsYourChildBenefitNumberFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "whatIsYourChildBenefitNumber.error.required"
  val formatKey = "whatIsYourChildBenefitNumber.error.format"
  val digitCount = 8

  val validGen: Gen[String] = for {
    prefix <- Gen.oneOf("CHB", "")
    digits <- Gen.listOfN(digitCount, Gen.numChar)
    letters <- Gen.listOfN(2, Gen.alphaChar)
  } yield {
    prefix ++ digits ++ letters
  }

  val form = new WhatIsYourChildBenefitNumberFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validGen
    )

    "bind data with spaces" in {
      val result = form.bind(Map(fieldName -> "C H1 23 456 78 A A ")).apply(fieldName)
      result.errors mustBe Seq(FormError(fieldName, formatKey, Seq("(CHB)?\\d{8}[A-Za-z]{2}")))
    }

    "not bind invalid data" - {

      "with too few digits" in {
        val invalidGen: Gen[String] = for {
          prefix <- Gen.oneOf("CHB", "")
          digits <- Gen.listOfN(digitCount - 1, Gen.numChar)
          letters <- Gen.listOfN(2, Gen.alphaChar)
        } yield {
          prefix ++ digits ++ letters
        }

        forAll(invalidGen -> "invalidDataItem") {
          dataItem: String =>
            val result = form.bind(Map(fieldName -> dataItem)).apply(fieldName)
            result.errors mustBe Seq(FormError(fieldName, formatKey, Seq("(CHB)?\\d{8}[A-Za-z]{2}")))
        }
      }

      "with too many digits" in {
        val invalidGen: Gen[String] = for {
          prefix <- Gen.oneOf("CHB", "")
          digits <- Gen.listOfN(digitCount + 1, Gen.numChar)
          letters <- Gen.listOfN(2, Gen.alphaChar)
        } yield {
          prefix ++ digits ++ letters
        }

        forAll(invalidGen -> "invalidDataItem") {
          dataItem: String =>
            val result = form.bind(Map(fieldName -> dataItem)).apply(fieldName)
            result.errors mustBe Seq(FormError(fieldName, formatKey, Seq("(CHB)?\\d{8}[A-Za-z]{2}")))
        }
      }

      "with missing letter suffix" in {
        val invalidGen = for {
          valid <- validGen
          toDrop <- Gen.chooseNum(1, 2)
        } yield {
          valid.dropRight(toDrop)
        }

        forAll(invalidGen -> "invalidDataItem") {
          dataItem: String =>
            val result = form.bind(Map(fieldName -> dataItem)).apply(fieldName)
            result.errors mustBe Seq(FormError(fieldName, formatKey, Seq("(CHB)?\\d{8}[A-Za-z]{2}")))
        }
      }

      "with incomplete prefix" in {
        val result = form.bind(Map(fieldName -> "CH12345678AA")).apply(fieldName)
        result.errors mustBe Seq(FormError(fieldName, formatKey, Seq("(CHB)?\\d{8}[A-Za-z]{2}")))
      }

    }

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
