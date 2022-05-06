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
import uk.gov.hmrc.govukfrontend.views.Aliases.{HtmlContent, Text}
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}
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
        .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourPreviousNamePage(Index(1)), WhatIsYourPreviousName(firstName = "first2", None, lastName = "last2")).success.value
        .set(WhatIsYourDateOfBirthPage, LocalDate.now).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, "country")).success.value
        .set(DoYouHaveAnyPreviousAddressesPage, true).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), PreviousAddressUk(addressLine1 = "line 1", None, None, "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))).success.value
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).success.value
        .set(AreYouMarriedPage, true).success.value
        .set(WhenDidYouGetMarriedPage, LocalDate.now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), PreviousMarriageOrPartnershipDetails(LocalDate.of(2000, 2, 1), LocalDate.of(2001, 3, 2), "nunya")).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage(Index(1)), PreviousMarriageOrPartnershipDetails(LocalDate.of(2002, 2, 1), LocalDate.of(2003, 3, 2), "nunya 2")).success.value
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
        .set(AreYouStillEmployedPage(Index(0)), true).success.value
        .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), LocalDate.of(2001, 3, 2)).success.value
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckYourAnswersView]

        val previousNames = List(
          ListWithActionsItem(
            name = HtmlContent("first middle last"),
            actions = List(
              ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changePreviousNameHidden", "first middle last")), href = routes.WhatIsYourPreviousNameController.onPageLoad(Index(0), CheckMode).url),
              ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removePreviousNameHidden", "first middle last")), href = routes.AreYouSureYouWantToRemovePreviousNameController.onPageLoad(Index(0), CheckMode).url)
            )
          ),
          ListWithActionsItem(
            name = HtmlContent("first2 last2"),
            actions = List(
              ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changePreviousNameHidden", "first2 last2")), href = routes.WhatIsYourPreviousNameController.onPageLoad(Index(1), CheckMode).url),
              ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removePreviousNameHidden", "first2 last2")), href = routes.AreYouSureYouWantToRemovePreviousNameController.onPageLoad(Index(1), CheckMode).url)
            )
          )
        )

        val personalDetails = SummaryListViewModel(Seq(
          WhatIsYourNameSummary.row(answers)(messages(application)),
          DoYouHaveAPreviousNameSummary.row(answers)(messages(application)),
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
        ).flatten)

        val previousAddresses = List(ListWithActionsItem(
          name = HtmlContent("line 1, postcode<br/>1 February 2000 to 2 March 2001"),
          actions = List(
            ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changePreviousAddressHidden", "line 1, postcode")), href = routes.IsYourPreviousAddressInUkController.onPageLoad(Index(0), CheckMode).url),
            ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removePreviousAddressHidden", "line 1, postcode")), href = routes.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(Index(0), CheckMode).url)
          )
        ))

        val currentRelationship = SummaryListViewModel(Seq(
          AreYouMarriedSummary.row(answers)(messages(application)),
          WhenDidYouGetMarriedSummary.row(answers)(messages(application)),
        ).flatten)

        val previousRelationships = List(
          ListWithActionsItem(
            name = HtmlContent(messages(application)("From 1 February 2000 to 2 March 2001")),
            actions = List(
              ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changePreviousRelationshipHidden", "1 February 2000", "2 March 2001")), href = routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(0), CheckMode).url),
              ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removePreviousRelationshipHidden", "1 February 2000", "2 March 2001")), href = routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(0), CheckMode).url) // TODO change to remove controller
            )
          ),
          ListWithActionsItem(
            name = HtmlContent(messages(application)("From 1 February 2002 to 2 March 2003")),
            actions = List(
              ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changePreviousRelationshipHidden", "1 February 2002", "2 March 2003")), href = routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(1), CheckMode).url),
              ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removePreviousRelationshipHidden", "1 February 2002", "2 March 2003")), href = routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(1), CheckMode).url) // TODO change to remove controller
            )
          )
        )

        val benefitHistory = SummaryListViewModel(Seq(
          HaveYouEverClaimedChildBenefitSummary.row(answers)(messages(application)),
          DoYouKnowYourChildBenefitNumberSummary.row(answers)(messages(application)),
          WhatIsYourChildBenefitNumberSummary.row(answers)(messages(application)),
          HaveYouEverReceivedOtherUkBenefitsSummary.row(answers)(messages(application)),
          WhatOtherUkBenefitsHaveYouReceivedSummary.row(answers)(messages(application))
        ).flatten)

        val employmentHistory = SummaryListViewModel(Seq(
          HaveYouEverWorkedInUkSummary.row(answers)(messages(application))
        ).flatten)

        val employers = List(ListWithActionsItem(
          name = HtmlContent("previous employers name<br/>line 1, postcode<br/>Employed from 1 February 2000 to 2 March 2001"),
          actions = List(
            ListWithActionsAction(content = Text(messages(application)("site.change")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.changeEmployerHidden", "previous employers name")), href = routes.WhatIsYourEmployersNameController.onPageLoad(Index(0), CheckMode).url),
            ListWithActionsAction(content = Text(messages(application)("site.remove")), visuallyHiddenText = Some(messages(application)("checkYourAnswers.removeEmployerHidden", "previous employers name")), href = routes.AreYouSureYouWantToRemoveEmployerController.onPageLoad(Index(0), CheckMode).url)
          )
        ))

        val supportingDocuments = SummaryListViewModel(Seq(
          DoYouHavePrimaryDocumentSummary.row(answers)(messages(application)),
          WhichPrimaryDocumentSummary.row(answers)(messages(application)),
          DoYouHaveTwoSecondaryDocumentsSummary.row(answers)(messages(application)),
          WhichAlternativeDocumentsSummary.row(answers)(messages(application))
        ).flatten)

        val renderedView = view(personalDetails, previousNames, addressHistory, previousAddresses, currentRelationship, previousRelationships, benefitHistory, employers, supportingDocuments)(request, messages(application))

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
