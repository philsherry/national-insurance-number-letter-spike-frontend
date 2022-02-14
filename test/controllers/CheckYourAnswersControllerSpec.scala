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

package controllers

import base.SpecBase
import models._
import pages._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import viewmodels.checkAnswers._
import viewmodels.govuk.SummaryListFluency
import views.html.CheckYourAnswersView

import java.time.LocalDate

class CheckYourAnswersControllerSpec extends SpecBase with SummaryListFluency {

  "Check Your Answers Controller" - {

    "must return OK and the correct view for a GET" in {

      val answers = emptyUserAnswers
        .set(WhatIsYourNamePage, WhatIsYourName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(DoYouHaveAPreviousNamePage, true).success.value
        .set(WhatIsYourPreviousNamePage, WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.now).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, "country")).success.value
        .set(DoYouHaveAnyPreviousAddressesPage, true).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), PreviousAddressUk(addressLine1 = "line 1", None, None, "postcode", from = LocalDate.now, to = LocalDate.now)).success.value
        .set(WhatIsYourPreviousAddressInternationalPage(Index(0)), PreviousAddressInternational(addressLine1 = "line 1", None, None, "country", from = LocalDate.now, to = LocalDate.now)).success.value
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).success.value
        .set(AreYouMarriedPage, true).success.value
        .set(WhenDidYouGetMarriedPage, LocalDate.now).success.value
        .set(AreYouInACivilPartnershipPage, true).success.value
        .set(WhenDidYouEnterACivilPartnershipPage, LocalDate.now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage, PreviousMarriageOrPartnershipDetails(LocalDate.now, LocalDate.now, "nunya")).success.value
        .set(HaveYouEverClaimedChildBenefitPage, true).success.value
        .set(DoYouKnowYourChildBenefitNumberPage, true).success.value
        .set(WhatIsYourChildBenefitNumberPage, "cbn").success.value
        .set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
        .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").success.value
        .set(HaveYouEverWorkedInUkPage, true).success.value
        .set(WhatIsYourEmployersNamePage, "employer").success.value
        .set(WhatIsYourEmployersAddressPage, WhatIsYourEmployersAddress("line 1", Some("line 2"), Some("line 3"), "postcode")).success.value
        .set(WhenDidYouStartWorkingForEmployerPage, LocalDate.now).success.value
        .set(AreYouStillEmployedPage, false).success.value
        .set(WhenDidYouFinishYourEmploymentPage, LocalDate.now).success.value
        .set(DoYouHaveAnyPreviousEmployersPage, true).success.value
        .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "previous employers name").success.value
        .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), PreviousEmployersAddress("line 1", None, None)).success.value
        .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.now).success.value
        .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.now).success.value
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckYourAnswersView]

        val personalDetails = SummaryListViewModel(Seq(
          WhatIsYourNameSummary.row(answers)(messages(application)),
          DoYouHaveAPreviousNameSummary.row(answers)(messages(application)),
          WhatIsYourPreviousNameSummary.row(answers)(messages(application)),
          WhatIsYourDateOfBirthSummary.row(answers)(messages(application)),
          AreYouReturningFromLivingAbroadSummary.row(answers)(messages(application)),
          WhatIsYourTelephoneNumberSummary.row(answers)(messages(application)),
          DoYouKnowYourNationalInsuranceNumberSummary.row(answers)(messages(application)),
          WhatIsYourNationalInsuranceNumberSummary.row(answers)(messages(application))
        ).flatten)

        val addressHistory = SummaryListViewModel(Seq(
          IsYourCurrentAddressInUkSummary.row(answers)(messages(application)),
          WhatIsYourCurrentAddressUkSummary.row(answers)(messages(application)),
          WhatIsYourCurrentAddressInternationalSummary.row(answers)(messages(application)),
          IsYourPreviousAddressInUkSummary.row(answers, 0)(messages(application)),
          WhatIsYourPreviousAddressUkSummary.row(answers, 0)(messages(application)),
          WhatIsYourPreviousAddressInternationalSummary.row(answers, 0)(messages(application))
        ).flatten)

        val relationshipHistory = SummaryListViewModel(Seq(
          AreYouMarriedSummary.row(answers)(messages(application)),
          WhenDidYouGetMarriedSummary.row(answers)(messages(application)),
          HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipSummary.row(answers)(messages(application)),
          PreviousMarriageOrPartnershipDetailsSummary.row(answers)(messages(application))
        ).flatten)

        val benefitHistory = SummaryListViewModel(Seq(
          HaveYouEverClaimedChildBenefitSummary.row(answers)(messages(application)),
          DoYouKnowYourChildBenefitNumberSummary.row(answers)(messages(application)),
          WhatIsYourChildBenefitNumberSummary.row(answers)(messages(application)),
          HaveYouEverReceivedOtherUkBenefitsSummary.row(answers)(messages(application)),
          WhatOtherUkBenefitsHaveYouReceivedSummary.row(answers)(messages(application))
        ).flatten)

        val employmentHistory = SummaryListViewModel(Seq(
          HaveYouEverWorkedInUkSummary.row(answers)(messages(application)),
          WhatIsYourEmployersNameSummary.row(answers)(messages(application)),
          WhatIsYourEmployersAddressSummary.row(answers)(messages(application)),
          WhenDidYouStartWorkingForEmployerSummary.row(answers)(messages(application)),
          AreYouStillEmployedSummary.row(answers)(messages(application)),
          WhenDidYouFinishYourEmploymentSummary.row(answers)(messages(application)),
          WhatIsYourPreviousEmployersNameSummary.row(answers, 0)(messages(application)),
          WhatIsYourPreviousEmployersAddressSummary.row(answers, 0)(messages(application)),
          WhenDidYouStartWorkingForPreviousEmployerSummary.row(answers, 0)(messages(application)),
          WhenDidYouStopWorkingForPreviousEmployerSummary.row(answers, 0)(messages(application))
        ).flatten)

        val supportingDocuments = SummaryListViewModel(Seq(
          DoYouHavePrimaryDocumentSummary.row(answers)(messages(application)),
          WhichPrimaryDocumentSummary.row(answers)(messages(application)),
          DoYouHaveTwoSecondaryDocumentsSummary.row(answers)(messages(application)),
          WhichAlternativeDocumentsSummary.row(answers)(messages(application))
        ).flatten)

        val renderedView = view(personalDetails, addressHistory, relationshipHistory, benefitHistory, employmentHistory, supportingDocuments)(request, messages(application))

        status(result) mustEqual OK
        contentAsString(result) mustEqual renderedView.toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
