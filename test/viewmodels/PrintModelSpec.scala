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
import uk.gov.hmrc.domain.Nino
import viewmodels.PrintModel._

import java.time.LocalDate

class PrintModelSpec extends SpecBase {

  "PrintModel" - {

    val currentAddressUk = CurrentAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA")
    val currentAddressInternational = CurrentAddressInternational(addressLine1 = "line 1", None, None, country = "France")
    val previousAddressUk = PreviousAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA",
      LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1))
    val previousAddressInternational = PreviousAddressInternational(addressLine1 = "line 1", None, None, country = "France",
      LocalDate.of(2002, 1, 1), LocalDate.of(2003, 1, 1))
    val employerAddress = WhatIsYourEmployersAddress(addressLine1 = "line 1", None, None, postcode = "AA1 1AA")
    val previousEmployerAddress = PreviousEmployersAddress("line 1", None, None, "AA1 1AA")

    "get current address" - {

      "must extract a UK address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressUkPage, currentAddressUk).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "AA1 1AA"))
      }

      "must extract an international address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressInternationalPage, currentAddressInternational).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "France"))
      }

      "must extract a UK address if both present" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourCurrentAddressUkPage, currentAddressUk).get
          .set(WhatIsYourCurrentAddressInternationalPage, currentAddressInternational).get

        getCurrentAddress(userAnswers) mustBe Some(List("line 1", "AA1 1AA"))
      }

      "must return None if both addresses are not set" in {
        val userAnswers = UserAnswers("id")

        getCurrentAddress(userAnswers) mustBe None
      }

    }

    "get previous addresses" - {

      "must extract no addresses" in {
        val userAnswers = UserAnswers("id")

        getPreviousAddresses(userAnswers) mustBe List.empty
      }

      "must extract a UK address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), previousAddressUk).get

        val expected = List(
          PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001")
        )
        getPreviousAddresses(userAnswers) mustBe expected
      }

      "must extract an international address" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressInternationalPage(Index(0)), previousAddressInternational).get

        val expected = List(
          PreviousAddressPrintModel(List("line 1", "France"), "1 January 2002", "1 January 2003")
        )
        getPreviousAddresses(userAnswers) mustBe expected
      }

      "must extract multiple UK and international addresses" in {
        val userAnswers = UserAnswers("id")
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), previousAddressUk).get
          .set(WhatIsYourPreviousAddressInternationalPage(Index(1)), previousAddressInternational).get
          .set(WhatIsYourPreviousAddressInternationalPage(Index(2)), previousAddressInternational).get
          .set(WhatIsYourPreviousAddressUkPage(Index(3)), previousAddressUk).get

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
          PreviousEmployerPrintModel("emp 1", List("line 1", "AA1 1AA"), "2 March 2013", "3 March 2013")
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
          PreviousEmployerPrintModel("emp 1", List("line 1", "AA1 1AA"), "2 March 2013", "3 March 2013"),
          PreviousEmployerPrintModel("emp 2", List("line 1", "AA1 1AA"), "2 March 2015", "3 March 2016")
        )

        getPreviousEmployers(userAnswers) mustBe expected
      }

    }

    "from user answers" - {

      "must extract a full model for a completed user answers with a primary document" in {
        val previousMarriage = PreviousMarriageOrPartnershipDetails(
          LocalDate.of(2005, 2, 1),
          LocalDate.of(2006, 3, 2),
          "reason"
        )

        val userAnswers = UserAnswers("id")
          .set(WhatIsYourNamePage, WhatIsYourName("first", Some("middle"), "last")).get
          .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName("prev", Some("prev2"), "prev3")).get
          .set(WhatIsYourDateOfBirthPage, LocalDate.of(1990, 12, 1)).get
          .set(WhatIsYourCurrentAddressUkPage, currentAddressUk).get
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), previousAddressUk).get
          .set(AreYouReturningFromLivingAbroadPage, false).get
          .set(WhatIsYourTelephoneNumberPage, "1234567890").get
          .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).get
          .set(WhenDidYouGetMarriedPage, LocalDate.of(2000, 5, 1)).get
          .set(PreviousMarriageOrPartnershipDetailsPage, previousMarriage).get
          .set(HaveYouEverClaimedChildBenefitPage, true).get
          .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get
          .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").get
          .set(WhatIsYourEmployersNamePage, "employer name").get
          .set(WhatIsYourEmployersAddressPage, employerAddress).get
          .set(WhenDidYouStartWorkingForEmployerPage, LocalDate.of(2001, 2, 28)).get
          .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").get
          .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "emp 1").get
          .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), previousEmployerAddress).get
          .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 2)).get
          .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 3)).get
          .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).get

        val expected = Some(PrintModel(
          WhatIsYourName("first", Some("middle"), "last"),
          List(WhatIsYourPreviousName("prev", Some("prev2"), "prev3")),
          "1 December 1990",
          List("line 1", "AA1 1AA"),
          List(PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001")),
          returningFromLivingAbroad = false,
          "1234567890",
          Some("AA123456A"),
          Some("1 May 2000"),
          Some(PreviousMarriageOrPartnershipPrintModel("1 February 2005", "2 March 2006", "reason")),
          claimedChildBenefit = true,
          Some("CHB12345678"),
          Some("other benefits"),
          Some(MostRecentUkEmployerPrintModel("employer name", List("line 1", "AA1 1AA"), "28 February 2001", None)),
          List(PreviousEmployerPrintModel("emp 1", List("line 1", "AA1 1AA"), "2 March 2013", "3 March 2013")),
          Some("whichPrimaryDocument.passport"),
          None
        ))

        from(userAnswers) mustBe expected
      }

      "must extract a full model for a completed user answers with secondary documents" in {
        val previousMarriage = PreviousMarriageOrPartnershipDetails(
          LocalDate.of(2005, 2, 1),
          LocalDate.of(2006, 3, 2),
          "reason"
        )

        val userAnswers = UserAnswers("id")
          .set(WhatIsYourNamePage, WhatIsYourName("first", Some("middle"), "last")).get
          .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName("prev", Some("prev2"), "prev3")).get
          .set(WhatIsYourDateOfBirthPage, LocalDate.of(1990, 12, 1)).get
          .set(WhatIsYourCurrentAddressUkPage, currentAddressUk).get
          .set(WhatIsYourPreviousAddressUkPage(Index(0)), previousAddressUk).get
          .set(AreYouReturningFromLivingAbroadPage, false).get
          .set(WhatIsYourTelephoneNumberPage, "1234567890").get
          .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).get
          .set(WhenDidYouGetMarriedPage, LocalDate.of(2000, 5, 1)).get
          .set(PreviousMarriageOrPartnershipDetailsPage, previousMarriage).get
          .set(HaveYouEverClaimedChildBenefitPage, true).get
          .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get
          .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").get
          .set(WhatIsYourEmployersNamePage, "employer name").get
          .set(WhatIsYourEmployersAddressPage, employerAddress).get
          .set(WhenDidYouStartWorkingForEmployerPage, LocalDate.of(2001, 2, 28)).get
          .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").get
          .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "emp 1").get
          .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), previousEmployerAddress).get
          .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 2)).get
          .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 3)).get
          .set(WhichAlternativeDocumentsPage, Set[AlternativeDocuments](AlternativeDocuments.AdoptionCertificate, AlternativeDocuments.WorkPermit)).get

        val expected = Some(PrintModel(
          WhatIsYourName("first", Some("middle"), "last"),
          List(WhatIsYourPreviousName("prev", Some("prev2"), "prev3")),
          "1 December 1990",
          List("line 1", "AA1 1AA"),
          List(PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "1 January 2000", "1 January 2001")),
          returningFromLivingAbroad = false,
          "1234567890",
          Some("AA123456A"),
          Some("1 May 2000"),
          Some(PreviousMarriageOrPartnershipPrintModel("1 February 2005", "2 March 2006", "reason")),
          claimedChildBenefit = true,
          Some("CHB12345678"),
          Some("other benefits"),
          Some(MostRecentUkEmployerPrintModel("employer name", List("line 1", "AA1 1AA"), "28 February 2001", None)),
          List(PreviousEmployerPrintModel("emp 1", List("line 1", "AA1 1AA"), "2 March 2013", "3 March 2013")),
          None,
          Some(List("whichAlternativeDocuments.adoption-certificate", "whichAlternativeDocuments.work-permit"))
        ))

        from(userAnswers) mustBe expected
      }

    }
  }
}