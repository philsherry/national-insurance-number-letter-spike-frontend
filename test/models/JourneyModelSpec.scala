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

import models.JourneyModel.PreviousRelationship
import models.PreviousRelationshipType.CivilPartnership
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{EitherValues, OptionValues, TryValues}
import pages._
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

class JourneyModelSpec extends AnyFreeSpec with Matchers with TryValues with EitherValues with OptionValues {

  "from" - {

    val now = LocalDate.now

    "must create a valid journey model when given valid user answers" in {

      val answers = UserAnswers("id")
        .set(WhatIsYourNamePage, WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(DoYouHaveAPreviousNamePage, true).success.value
        .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourPreviousNamePage(Index(1)), WhatIsYourPreviousName(firstName = "first2", None, lastName = "last2")).success.value
        .set(WhatIsYourDateOfBirthPage, now).success.value
        .set(WhatIsYourGenderPage, WhatIsYourGender.PreferNotToSay).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, Some("postcode"), Country("FR", "France"))).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), PreviousAddressUk(addressLine1 = "line 1", None, None, "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))).success.value
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).success.value
        .set(AreYouMarriedPage, true).success.value
        .set(CurrentRelationshipTypePage, CurrentRelationshipType.Marriage).success.value
        .set(WhenDidYouGetMarriedPage, now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), PreviousMarriageOrPartnershipDetails(now, now, "nunya")).success.value
        .set(PreviousRelationshipTypePage(Index(0)), CivilPartnership).success.value
        .set(HaveYouEverClaimedChildBenefitPage, true).success.value
        .set(DoYouKnowYourChildBenefitNumberPage, true).success.value
        .set(WhatIsYourChildBenefitNumberPage, "cbn").success.value
        .set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
        .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").success.value
        .set(HaveYouEverWorkedInUkPage, true).success.value
        .set(EmploymentHistoryPage, true).success.value
        .set(WhatIsYourEmployersNamePage(Index(0)), "previous employers name").success.value
        .set(WhatIsYourEmployersAddressPage(Index(0)), EmployersAddress("line 1", None, None, "postcode")).success.value
        .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), LocalDate.of(2000, 2, 1)).success.value
        .set(AreYouStillEmployedPage(Index(0)), false).success.value
        .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), LocalDate.of(2001, 3, 2)).success.value
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val expected: JourneyModel = JourneyModel(
        currentName = WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last"),
        previousNames = List(
          WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last"),
          WhatIsYourPreviousName(firstName = "first2", middleNames = None, lastName = "last2")
        ),
        dateOfBirth = now,
        gender = WhatIsYourGender.PreferNotToSay,
        telephoneNumber = "tel",
        nationalInsuranceNumber = Some("AA123456A"),
        returningFromLivingAbroad = true,
        currentAddress = CurrentAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "postcode"),
        previousAddresses = List(
          PreviousAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))
        ),
        currentRelationship = Some(JourneyModel.CurrentRelationship(relationshipType = CurrentRelationshipType.Marriage, from = now)),
        previousRelationships = List(
          PreviousRelationship(relationshipType = PreviousRelationshipType.CivilPartnership, now, now, "nunya")
        ),
        claimedChildBenefit = true,
        childBenefitNumber = Some("cbn"),
        otherBenefits = Some("other benefits"),
        employers = List(
          JourneyModel.Employer("previous employers name", EmployersAddress("line 1", None, None, "postcode"), LocalDate.of(2000, 2, 1), Some(LocalDate.of(2001, 3, 2)))
        ),
        documents = List("passport")
      )

      val (errors, data) = JourneyModel.from(answers).pad
      errors mustBe empty
      data.value mustEqual expected
    }

    "must collect non fatal errors when generating model" in {

      val answers = UserAnswers("id")
        .set(WhatIsYourNamePage, WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(DoYouHaveAPreviousNamePage, true).success.value
        .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourPreviousNamePage(Index(1)), WhatIsYourPreviousName(firstName = "first2", None, lastName = "last2")).success.value
        .set(WhatIsYourDateOfBirthPage, now).success.value
        .set(WhatIsYourGenderPage, WhatIsYourGender.PreferNotToSay).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, Some("postcode"), Country("FR", "France"))).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        // omitting previous address details
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).success.value
        .set(AreYouMarriedPage, true).success.value
        .set(CurrentRelationshipTypePage, CurrentRelationshipType.Marriage).success.value
        .set(WhenDidYouGetMarriedPage, now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        // omitting previous marriage or partnership details
        .set(PreviousRelationshipTypePage(Index(0)), CivilPartnership).success.value
        .set(HaveYouEverClaimedChildBenefitPage, true).success.value
        .set(DoYouKnowYourChildBenefitNumberPage, true).success.value
        .set(WhatIsYourChildBenefitNumberPage, "cbn").success.value
        .set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
        .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").success.value
        .set(HaveYouEverWorkedInUkPage, true).success.value
        .set(WhatIsYourEmployersNamePage(Index(0)), "previous employers name").success.value
        .set(WhatIsYourEmployersAddressPage(Index(0)), EmployersAddress("line 1", None, None, "postcode")).success.value
        .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), LocalDate.of(2000, 2, 1)).success.value
        .set(AreYouStillEmployedPage(Index(0)), false).success.value
        // omitting employment end date
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val expected: JourneyModel = JourneyModel(
        currentName = WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last"),
        previousNames = List(
          WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last"),
          WhatIsYourPreviousName(firstName = "first2", middleNames = None, lastName = "last2")
        ),
        dateOfBirth = now,
        gender = WhatIsYourGender.PreferNotToSay,
        telephoneNumber = "tel",
        nationalInsuranceNumber = Some("AA123456A"),
        returningFromLivingAbroad = true,
        currentAddress = CurrentAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "postcode"),
        previousAddresses = List.empty,
        currentRelationship = Some(JourneyModel.CurrentRelationship(relationshipType = CurrentRelationshipType.Marriage, from = now)),
        previousRelationships = List.empty,
        claimedChildBenefit = true,
        childBenefitNumber = Some("cbn"),
        otherBenefits = Some("other benefits"),
        employers = List.empty,
        documents = List("passport")
      )

      val (errors, data) = JourneyModel.from(answers).pad
      errors.value.toChain.toList must contain only (
        WhatIsYourPreviousAddressUkPage(Index(0)),
        PreviousMarriageOrPartnershipDetailsPage(Index(0)),
        WhenDidYouStopWorkingForEmployerPage(Index(0))
      )
      data.value mustEqual expected
    }

    "must collect fatal errors" in {

      val answers = UserAnswers("id")
        // omitting name
        .set(DoYouHaveAPreviousNamePage, true).success.value
        .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourPreviousNamePage(Index(1)), WhatIsYourPreviousName(firstName = "first2", None, lastName = "last2")).success.value
        .set(WhatIsYourDateOfBirthPage, now).success.value
        .set(WhatIsYourGenderPage, WhatIsYourGender.PreferNotToSay).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, Some("postcode"), Country("FR", "France"))).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), PreviousAddressUk(addressLine1 = "line 1", None, None, "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))).success.value
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        // omitting nino
        .set(AreYouMarriedPage, true).success.value
        .set(CurrentRelationshipTypePage, CurrentRelationshipType.Marriage).success.value
        .set(WhenDidYouGetMarriedPage, now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), PreviousMarriageOrPartnershipDetails(now, now, "nunya")).success.value
        .set(PreviousRelationshipTypePage(Index(0)), CivilPartnership).success.value
        .set(HaveYouEverClaimedChildBenefitPage, true).success.value
        .set(DoYouKnowYourChildBenefitNumberPage, true).success.value
        .set(WhatIsYourChildBenefitNumberPage, "cbn").success.value
        .set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
        .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").success.value
        .set(HaveYouEverWorkedInUkPage, true).success.value
        .set(EmploymentHistoryPage, true).success.value
        .set(WhatIsYourEmployersNamePage(Index(0)), "previous employers name").success.value
        .set(WhatIsYourEmployersAddressPage(Index(0)), EmployersAddress("line 1", None, None, "postcode")).success.value
        .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), LocalDate.of(2000, 2, 1)).success.value
        .set(AreYouStillEmployedPage(Index(0)), false).success.value
        .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), LocalDate.of(2001, 3, 2)).success.value
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val (errors, data) = JourneyModel.from(answers).pad
      errors.value.toChain.toList must contain only (
        WhatIsYourNamePage,
        WhatIsYourNationalInsuranceNumberPage
      )
      data mustBe empty
    }
  }
}
