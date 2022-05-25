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
import com.dmanchester.playfop.sapi.PlayFop
import models._
import pages._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.domain.Nino
import viewmodels.PrintModel
import views.html.PrintView
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import audit.AuditService
import models.PreviousRelationshipType.CivilPartnership

import java.nio.charset.Charset
import java.time.LocalDate

class PrintControllerSpec extends SpecBase with MockitoSugar {

  private val now = LocalDate.now

  private val completeUserAnswers: UserAnswers =
    UserAnswers("id")
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

  "Print Controller" - {

    "onPageLoad" - {

      "must return OK and the correct view for a GET when user answers is complete" in {

        val application = applicationBuilder(userAnswers = Some(completeUserAnswers)).build()
        val printModel = PrintModel.from(JourneyModel.from(completeUserAnswers).toOption.value)

        running(application) {
          val request = FakeRequest(GET, routes.PrintController.onPageLoad.url)

          val result = route(application, request).value

          val view = application.injector.instanceOf[PrintView]

          status(result) mustEqual OK
          contentAsString(result) mustEqual view(printModel)(request, messages(application)).toString
        }
      }

      "must return redirect to recovery controller for a GET when user answers is incomplete" in {
        val incompleteUserAnswers = completeUserAnswers.remove(WhatIsYourNamePage).get

        val application = applicationBuilder(userAnswers = Some(incompleteUserAnswers)).build()

        running(application) {
          val request = FakeRequest(GET, routes.PrintController.onPageLoad.url)

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }

    "onDownload" - {

      "must return OK for a GET when user answers is complete" in {
        val mockAuditService = mock[AuditService]
        val mockFop = mock[PlayFop]
        when(mockFop.processTwirlXml(any(), any(), any(), any())).thenReturn("hello".getBytes)

        val application = applicationBuilder(userAnswers = Some(completeUserAnswers))
          .overrides(
            bind[PlayFop].toInstance(mockFop),
            bind[AuditService].toInstance(mockAuditService)
          )
          .build()

        running(application) {
          val request = FakeRequest(GET, routes.PrintController.onDownload.url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsBytes(result).decodeString(Charset.defaultCharset()) mustEqual "hello"

          verify(mockAuditService, times(1)).auditDownload(any())(any())
        }
      }

      "must return redirect to recovery controller for a GET when user answers is incomplete" in {
        val incompleteUserAnswers = completeUserAnswers.remove(WhatIsYourNamePage).get

        val application = applicationBuilder(userAnswers = Some(incompleteUserAnswers)).build()

        running(application) {
          val request = FakeRequest(GET, routes.PrintController.onDownload.url)

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }
  }


}
