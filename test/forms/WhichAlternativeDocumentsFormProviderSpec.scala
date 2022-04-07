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

import forms.behaviours.CheckboxFieldBehaviours
import models.AlternativeDocuments
import org.scalacheck.Gen
import play.api.data.FormError

class WhichAlternativeDocumentsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new WhichAlternativeDocumentsFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "whichAlternativeDocuments.error.exactlyTwo"
    val invalidError = FormError(s"$fieldName[0]", "whichAlternativeDocuments.error.exactlyTwo")

    "bind for two valid values" in {
      for {
        gen <- Gen.pick(2, AlternativeDocuments.values)
      } yield {
        val data = gen.zipWithIndex.map{case (value, i) => s"$fieldName[$i]" -> value.toString}.toMap
        val result = form.bind(data)
        result.get mustEqual Set(gen)
        result.errors mustBe empty
      }
    }

    "fail to bind for one valid value" in {
      for {
        gen <- Gen.pick(1, AlternativeDocuments.values)
      } yield {
        val data = gen.zipWithIndex.map{case (value, i) => s"$fieldName[$i]" -> value.toString}.toMap
        form.bind(data).errors must contain(invalidError)
      }
    }

    "fail to bind for three or more valid values" in {
      for {
        num <- Gen.pick(1, 3 to 7)
        gen <- Gen.pick(num.head, AlternativeDocuments.values)
      } yield {
        val data = gen.zipWithIndex.map{case (value, i) => s"$fieldName[$i]" -> value.toString}.toMap
        form.bind(data).errors must contain(invalidError)
      }
    }

    "fail to bind when the answer is invalid" in {
      val data = Map(
        s"$fieldName[0]" -> "invalid value"
      )
      form.bind(data).errors must contain(invalidError)
    }

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
