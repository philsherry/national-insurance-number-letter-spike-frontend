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
import forms.WhatIsYourPreviousAddressInternationalFormProvider
import models.{Index, NormalMode, PreviousAddressInternational, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.WhatIsYourPreviousAddressInternationalPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.WhatIsYourPreviousAddressInternationalView

import java.time.LocalDate
import scala.concurrent.Future

class WhatIsYourPreviousAddressInternationalControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new WhatIsYourPreviousAddressInternationalFormProvider()
  val form = formProvider()

  lazy val whatIsYourPreviousAddressInternationalRoute = routes.WhatIsYourPreviousAddressInternationalController.onPageLoad(Index(0), NormalMode).url

  val validData = PreviousAddressInternational(
    addressLine1 = "value 1", addressLine2 = None, addressLine3 = None, postcode = None, country = "country", from = LocalDate.now, to = LocalDate.now
  )

  val userAnswers = UserAnswers(userAnswersId)
    .set(WhatIsYourPreviousAddressInternationalPage(Index(0)), validData)
    .success.value

  "WhatIsYourPreviousAddressInternational Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourPreviousAddressInternationalRoute)

        val view = application.injector.instanceOf[WhatIsYourPreviousAddressInternationalView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, Index(0), NormalMode)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourPreviousAddressInternationalRoute)

        val view = application.injector.instanceOf[WhatIsYourPreviousAddressInternationalView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validData), Index(0), NormalMode)(request, messages(application)).toString
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
          FakeRequest(POST, whatIsYourPreviousAddressInternationalRoute)
            .withFormUrlEncodedBody(
              "addressLine1" -> "value 1",
              "country" -> "value 2",
              "from.day" -> LocalDate.now.getDayOfMonth.toString, "from.month" -> LocalDate.now.getMonthValue.toString, "from.year" -> LocalDate.now.getYear.toString,
              "to.day" -> LocalDate.now.getDayOfMonth.toString, "to.month" -> LocalDate.now.getMonthValue.toString, "to.year" -> LocalDate.now.getYear.toString
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
          FakeRequest(POST, whatIsYourPreviousAddressInternationalRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[WhatIsYourPreviousAddressInternationalView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, Index(0), NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourPreviousAddressInternationalRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, whatIsYourPreviousAddressInternationalRoute)
            .withFormUrlEncodedBody(("addressLine1", "value 1"), ("adressLine2", "value 2"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
