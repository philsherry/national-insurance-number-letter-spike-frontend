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
import forms.DoYouHaveAnyPreviousAddressesFormProvider
import models.{Index, NormalMode, PreviousAddressUk}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{IsYourPreviousAddressInUkPage, WhatIsYourPreviousAddressUkPage}
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}
import views.html.DoYouHaveAnyPreviousAddressesView

import java.time.LocalDate
import scala.concurrent.Future

class DoYouHaveAnyPreviousAddressesControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new DoYouHaveAnyPreviousAddressesFormProvider()
  val form = formProvider()

  lazy val doYouHaveAnyPreviousAddressesRoute = routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode).url

  "DoYouHaveAnyPreviousAddresses Controller" - {

    "must return OK and the correct view for a GET when there are no previous addresses" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousAddressesRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousAddressesView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, List.empty, NormalMode)(request, messages(application)).toString
      }
    }

    "must return OK and the correct view for a GET when there are already previous addresses" in {

      val address = PreviousAddressUk("line 1", None, None, "postcode", LocalDate.now, LocalDate.now)

      val answers = emptyUserAnswers
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), address).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      val expectedItems = List(
        ListWithActionsItem(
          name = Text("line 1, postcode"),
          actions = Seq(
            ListWithActionsAction(content = Text(messages(application)("site.change")), href = routes.IsYourPreviousAddressInUkController.onPageLoad(Index(0), NormalMode).url),
            ListWithActionsAction(content = Text(messages(application)("site.remove")), href = routes.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(Index(0), NormalMode).url)
          )
        )
      )

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousAddressesRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousAddressesView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, expectedItems, NormalMode)(request, messages(application)).toString
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
          FakeRequest(POST, doYouHaveAnyPreviousAddressesRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, doYouHaveAnyPreviousAddressesRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousAddressesView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, List.empty, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousAddressesRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, doYouHaveAnyPreviousAddressesRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
