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
import models.{Country, Index, NormalMode, PreviousAddressInternational, UserAnswers}
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

import java.time.{LocalDate, YearMonth}
import scala.concurrent.Future

class WhatIsYourPreviousAddressInternationalControllerSpec extends SpecBase with MockitoSugar {

  private def onwardRoute = Call("GET", "/foo")
  private val country = Country("FR", "France")

  val formProvider = new WhatIsYourPreviousAddressInternationalFormProvider()


  lazy val whatIsYourPreviousAddressInternationalRoute = routes.WhatIsYourPreviousAddressInternationalController.onPageLoad(Index(0), NormalMode).url

  val startDate = YearMonth.from(LocalDate.now.minusDays(1))
  val endDate = YearMonth.from(LocalDate.now)

  val validData = PreviousAddressInternational(
    addressLine1 = "value 1", addressLine2 = None, addressLine3 = None, postcode = None, country = country, from = startDate, to = endDate
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
        implicit val msgs = messages(application)
        val form = formProvider()

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, Index(0), NormalMode)(request, implicitly).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, whatIsYourPreviousAddressInternationalRoute)

        val view = application.injector.instanceOf[WhatIsYourPreviousAddressInternationalView]
        implicit val msgs = messages(application)
        val form = formProvider()

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validData), Index(0), NormalMode)(request, implicitly).toString
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
              "country" -> "FR",
              "from.month" -> startDate.getMonthValue.toString, "from.year" -> startDate.getYear.toString,
              "to.month" -> endDate.getMonthValue.toString, "to.year" -> endDate.getYear.toString
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

        implicit val msgs = messages(application)
        val form = formProvider()

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[WhatIsYourPreviousAddressInternationalView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, Index(0), NormalMode)(request, implicitly).toString
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
            .withFormUrlEncodedBody(
              "addressLine1" -> "value 1",
              "country" -> "FR",
              "from.month" -> startDate.getMonthValue.toString, "from.year" -> startDate.getYear.toString,
              "to.month" -> endDate.getMonthValue.toString, "to.year" -> endDate.getYear.toString
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
