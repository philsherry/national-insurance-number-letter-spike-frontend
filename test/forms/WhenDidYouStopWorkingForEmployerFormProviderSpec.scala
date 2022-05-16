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

import java.time.{LocalDate, ZoneOffset}
import forms.behaviours.DateBehaviours
import play.api.data.FormError

import java.time.format.DateTimeFormatter

class WhenDidYouStopWorkingForEmployerFormProviderSpec extends DateBehaviours {

  private val startDate = LocalDate.of(2000, 1, 1)
  private val form = new WhenDidYouStopWorkingForEmployerFormProvider()(startDate)

  ".value" - {

    val validData = datesBetween(
      min = startDate,
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "whenDidYouStopWorkingForEmployer.error.required.all")

    "fail to bind when given a date before the start date" in {

      def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

      val invalidDates = datesBetween(
        min = LocalDate.of(1900, 1, 1),
        max = startDate
      )

      forAll(invalidDates) {
        date =>
          val data = Map(
            "value.day"   -> date.getDayOfMonth.toString,
            "value.month" -> date.getMonthValue.toString,
            "value.year"  -> date.getYear.toString
          )

          val result = form.bind(data)
          result.errors must contain only FormError(
            "value",
            "whenDidYouStopWorkingForEmployer.error.tooEarly",
            Seq(startDate.format(dateFormatter)))
      }
    }
  }
}
