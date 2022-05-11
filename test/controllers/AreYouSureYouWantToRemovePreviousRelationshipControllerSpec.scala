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
import forms.AreYouSureYouWantToRemovePreviousRelationshipFormProvider
import models.{Index, NormalMode, PreviousMarriageOrPartnershipDetails}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when, never}
import org.scalatestplus.mockito.MockitoSugar
import pages.PreviousMarriageOrPartnershipDetailsPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.AreYouSureYouWantToRemovePreviousRelationshipView

import java.time.LocalDate
import scala.concurrent.Future

class AreYouSureYouWantToRemovePreviousRelationshipControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new AreYouSureYouWantToRemovePreviousRelationshipFormProvider()
  val form = formProvider()

  val from = LocalDate.of(2000, 2, 1)
  val to = LocalDate.of(2001, 3, 2)
  val details = PreviousMarriageOrPartnershipDetails(from, to, "nunya")

  val answers = emptyUserAnswers
    .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), details).success.value

  lazy val areYouSureYouWantToRemovePreviousRelationshipRoute = routes.AreYouSureYouWantToRemovePreviousRelationshipController.onPageLoad(Index(0), NormalMode).url

  "AreYouSureYouWantToRemovePreviousRelationship Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, areYouSureYouWantToRemovePreviousRelationshipRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AreYouSureYouWantToRemovePreviousRelationshipView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, details, Index(0), NormalMode)(request, messages(application)).toString
      }
    }

    "must remove data then redirect to the next page when 'yes' is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, areYouSureYouWantToRemovePreviousRelationshipRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url

        verify(mockSessionRepository, times(1)).set(any())
      }
    }

    "must not remove data then redirect to the next page when 'no' is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, areYouSureYouWantToRemovePreviousRelationshipRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url

        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request =
          FakeRequest(POST, areYouSureYouWantToRemovePreviousRelationshipRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AreYouSureYouWantToRemovePreviousRelationshipView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, details, Index(0), NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, areYouSureYouWantToRemovePreviousRelationshipRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, areYouSureYouWantToRemovePreviousRelationshipRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
