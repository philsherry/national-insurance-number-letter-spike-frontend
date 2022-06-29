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

package models

import generators.Generators
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsString, Json}

import java.time.{LocalDate, YearMonth}

class YearMonthFormatSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with Generators with OptionValues with YearMonthFormat {

  "YearMonthFormat" - {

    "must deserialise valid values" in {

      val gen = for {
        date <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
      } yield YearMonth.from(date)

      forAll(gen) {
        date =>
          JsString(date.toString).validate[YearMonth].asOpt.value mustEqual date
      }
    }

    "must deserialise fallback values" in {

      val gen = datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)

      forAll(gen) {
        date =>
          JsString(date.toString).validate[YearMonth].asOpt.value mustEqual YearMonth.from(date)
      }
    }

    "must serialise values" in {
      val gen = for {
        date <- datesBetween(LocalDate.of(1900, 1, 1), LocalDate.now)
      } yield YearMonth.from(date)

      forAll(gen) {
        date =>
          Json.toJson(date) mustEqual JsString(date.toString)
      }
    }
  }
}
