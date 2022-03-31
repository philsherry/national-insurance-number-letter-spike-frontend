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

package viewmodels

import base.SpecBase
import models._
import pages._
import viewmodels.PrintModel._

import java.time.LocalDate

class PrintModelSpec extends SpecBase {

  "PrintModel" - {

    "get current address" - {
      val ukAddress = CurrentAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA")
      val intAddress = CurrentAddressInternational(addressLine1 = "line 1", None, None, country = "France")

      "must extract a UK address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressUkPage, ukAddress).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "AA1 1AA"))
      }

      "must extract an international address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressInternationalPage, intAddress).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "France"))
      }

      "must extract a UK address if both present" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressUkPage, ukAddress).get
          .set(WhatIsYourCurrentAddressInternationalPage, intAddress).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "AA1 1AA"))
      }

      "must return None if both addresses are not set" in {
        val userAnswers = UserAnswers("id")

        getCurrentAddress(userAnswers) mustBe None
      }

    }

    "get previous addresses" - {
      val ukAddress = PreviousAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA",
        LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1))
      val intAddress = PreviousAddressInternational(addressLine1 = "line 1", None, None, country = "France",
        LocalDate.of(2002, 1, 1), LocalDate.of(2003, 1, 1))

      "must extract no addresses" in {
        val userAnswers = UserAnswers("id")

        getPreviousAddresses(userAnswers) mustBe List.empty
      }

      "must extract a UK address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), ukAddress).get

        val expected = List(
          PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001")
        )
        getPreviousAddresses(userAnswers) mustBe expected
      }

      "must extract an international address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressInternationalPage(Index(0)), intAddress).get

        val expected = List(
          PreviousAddressPrintModel(List("line 1", "France"), "1 January 2002", "1 January 2003")
        )
        getPreviousAddresses(userAnswers) mustBe expected
      }

      "must extract multiple UK and international addresses" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), ukAddress).get
          .set(WhatIsYourPreviousAddressInternationalPage(Index(1)), intAddress).get
          .set(WhatIsYourPreviousAddressInternationalPage(Index(2)), intAddress).get
          .set(WhatIsYourPreviousAddressUkPage(Index(3)), ukAddress).get

        val expected = List(
          PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001"),
          PreviousAddressPrintModel(List("line 1", "France"), "1 January 2002", "1 January 2003"),
          PreviousAddressPrintModel(List("line 1", "France"), "1 January 2002", "1 January 2003"),
          PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001")
        )
        getPreviousAddresses(userAnswers) mustBe expected
      }

    }

    "get most recent employer" - {
      val employerAddress = WhatIsYourEmployersAddress(addressLine1 = "line 1", None, None, postcode = "AA1 1AA")

      "must extract employer with a start and end" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourEmployersNamePage, "employer name").get
          .set(WhatIsYourEmployersAddressPage, employerAddress).get
          .set(WhenDidYouStartWorkingForEmployerPage, LocalDate.of(2010, 5, 10)).get
          .set(WhenDidYouFinishYourEmploymentPage, LocalDate.of(2015, 10, 20)).get

        val expected = MostRecentUkEmployerPrintModel(
          "employer name",
          List("line 1", "AA1 1AA"),
          "10 May 2010",
          Some("20 October 2015")
        )

        getMostRecentUkEmployer(userAnswers) mustBe Some(expected)
      }

      "must extract employer with start" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourEmployersNamePage, "employer name").get
          .set(WhatIsYourEmployersAddressPage, employerAddress).get
          .set(WhenDidYouStartWorkingForEmployerPage, LocalDate.of(2010, 5, 10)).get

        val expected = MostRecentUkEmployerPrintModel(
          "employer name",
          List("line 1", "AA1 1AA"),
          "10 May 2010",
          None
        )

        getMostRecentUkEmployer(userAnswers) mustBe Some(expected)
      }

      "must return None if answers are not set" in {
        val userAnswers = UserAnswers("id")

        getMostRecentUkEmployer(userAnswers) mustBe None
      }

    }

    "get previous employers" - {
      val previousEmployerAddress = PreviousEmployersAddress("line 1", None, None)

      "must extract no previous employers" in {
        val userAnswers = UserAnswers("id")

        getPreviousEmployers(userAnswers) mustBe List.empty
      }

      "must extract one previous employer" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "emp 1").get
          .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), previousEmployerAddress).get
          .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 2)).get
          .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 3)).get

        val expected = List(
          PreviousEmployerPrintModel("emp 1", List("line 1"), "2 March 2013", "3 March 2013")
        )

        getPreviousEmployers(userAnswers) mustBe expected
      }

      "must extract multiple previous employers" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "emp 1").get
          .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), previousEmployerAddress).get
          .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 2)).get
          .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 3)).get
          .set(WhatIsYourPreviousEmployersNamePage(Index(1)), "emp 2").get
          .set(WhatIsYourPreviousEmployersAddressPage(Index(1)), previousEmployerAddress).get
          .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(1)), LocalDate.of(2015, 3, 2)).get
          .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(1)), LocalDate.of(2016, 3, 3)).get

        val expected = List(
          PreviousEmployerPrintModel("emp 1", List("line 1"), "2 March 2013", "3 March 2013"),
          PreviousEmployerPrintModel("emp 2", List("line 1"), "2 March 2015", "3 March 2016")
        )

        getPreviousEmployers(userAnswers) mustBe expected
      }

    }

    // TODO: Add case for full model
  }
}