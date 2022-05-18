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
import forms.WhatIsYourCurrentAddressInternationalFormProvider
import models.{Country, CurrentAddressInternational, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.WhatIsYourCurrentAddressInternationalPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.WhatIsYourCurrentAddressInternationalView

import scala.concurrent.Future

class WhatIsYourCurrentAddressInternationalControllerSpec extends SpecBase with MockitoSugar {

  private def onwardRoute = Call("GET", "/foo")
  private val country = Country("FR", "France")

  val formProvider = new WhatIsYourCurrentAddressInternationalFormProvider()

  lazy val whatIsYourCurrentAddressInternationalRoute = routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(NormalMode).url

  val validData = CurrentAddressInternational(
    addressLine1 = "value 1", addressLine2 = None, addressLine3 = None, postcode = None, country = country
  )

  val userAnswers = UserAnswers(userAnswersId)
    .set(WhatIsYourCurrentAddressInternationalPage, validData)
    .success.value

  "WhatIsYourCurrentAddressInternational Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourCurrentAddressInternationalRoute)

        val view = application.injector.instanceOf[WhatIsYourCurrentAddressInternationalView]
        implicit val msgs = messages(application)
        val form = formProvider()

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(request, implicitly).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourCurrentAddressInternationalRoute)

        val view = application.injector.instanceOf[WhatIsYourCurrentAddressInternationalView]
        implicit val msgs = messages(application)
        val form = formProvider()

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validData), NormalMode)(request, implicitly).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

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
          FakeRequest(POST, whatIsYourCurrentAddressInternationalRoute)
            .withFormUrlEncodedBody(
              "addressLine1"->  "value 1",
              "country" -> "FR"
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, whatIsYourCurrentAddressInternationalRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        implicit val msgs = messages(application)
        val form = formProvider()

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[WhatIsYourCurrentAddressInternationalView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode)(request, implicitly).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourCurrentAddressInternationalRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, whatIsYourCurrentAddressInternationalRoute)
            .withFormUrlEncodedBody(("addressLine1", "value 1"), ("country", "FR"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
