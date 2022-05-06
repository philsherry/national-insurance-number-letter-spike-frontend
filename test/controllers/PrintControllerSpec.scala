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
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind

import java.nio.charset.Charset
import java.time.LocalDate

class PrintControllerSpec extends SpecBase with MockitoSugar {

  private val completeUserAnswers: UserAnswers = {
    val currentAddressUk = CurrentAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA")
    val previousAddressUk = PreviousAddressUk(addressLine1 = "line 1", None, None, postcode = "AA1 1AA",
      LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1))
    val previousEmployerAddress = PreviousEmployersAddress("line 1", None, None, "AA1 1AA")

    val previousMarriage = PreviousMarriageOrPartnershipDetails(
      LocalDate.of(2005, 2, 1),
      LocalDate.of(2006, 3, 2),
      "reason"
    )

    UserAnswers("id")
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
      .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").get
      .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "emp 1").get
      .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), previousEmployerAddress).get
      .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 2)).get
      .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2013, 3, 3)).get
      .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).get
  }

  "Print Controller" - {

    "onPageLoad" - {

      "must return OK and the correct view for a GET when user answers is complete" in {

        val application = applicationBuilder(userAnswers = Some(completeUserAnswers)).build()
        val printModel = PrintModel.from(completeUserAnswers).value

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
        val mockFop = mock[PlayFop]
        when(mockFop.processTwirlXml(any(), any(), any(), any())).thenReturn("hello".getBytes)

        val application = applicationBuilder(userAnswers = Some(completeUserAnswers))
          .overrides(bind[PlayFop].toInstance(mockFop))
          .build()

        running(application) {
          val request = FakeRequest(GET, routes.PrintController.onDownload.url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsBytes(result).decodeString(Charset.defaultCharset()) mustEqual "hello"
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
