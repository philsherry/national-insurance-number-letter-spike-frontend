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
import forms.DoYouHaveAnyPreviousEmployersFormProvider
import models.{Index, NormalMode, PreviousEmployersAddress, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}
import views.html.DoYouHaveAnyPreviousEmployersView

import java.time.LocalDate

class DoYouHaveAnyPreviousEmployersControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new DoYouHaveAnyPreviousEmployersFormProvider()
  val form = formProvider()

  lazy val doYouHaveAnyPreviousEmployersRoute = routes.DoYouHaveAnyPreviousEmployersController.onPageLoad(NormalMode).url

  "DoYouHaveAnyPreviousEmployers Controller" - {

    "must return OK and the correct view for a GET when there are no previous addresses" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousEmployersRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousEmployersView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, List.empty, NormalMode)(request, messages(application)).toString
      }
    }

    "must return OK and the correct view for a GET when there are already previous addresses" in {

      val answers = emptyUserAnswers
        .set(WhatIsYourPreviousEmployersNamePage(Index(0)), "foobar").success.value
        .set(WhatIsYourPreviousEmployersAddressPage(Index(0)), PreviousEmployersAddress("line 1", None, None, "postcode")).success.value
        .set(WhenDidYouStartWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2000, 2, 1)).success.value
        .set(WhenDidYouStopWorkingForPreviousEmployerPage(Index(0)), LocalDate.of(2000, 3, 2)).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      val expectedItems = List(
        ListWithActionsItem(
          name = HtmlContent("foobar<br/>line 1, postcode<br/>1 February 2000 to 2 March 2000"),
          actions = Seq(
            ListWithActionsAction(content = Text(messages(application)("site.change")), href = routes.WhatIsYourPreviousEmployersNameController.onPageLoad(Index(0), NormalMode).url),
            ListWithActionsAction(content = Text(messages(application)("site.remove")), href = routes.AreYouSureYouWantToRemovePreviousEmployerController.onPageLoad(Index(0), NormalMode).url)
          )
        )
      )

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousEmployersRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousEmployersView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, expectedItems, NormalMode)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = UserAnswers(userAnswersId).set(DoYouHaveAnyPreviousEmployersPage, true).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousEmployersRoute)

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousEmployersView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(true), List.empty, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, doYouHaveAnyPreviousEmployersRoute)
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
          FakeRequest(POST, doYouHaveAnyPreviousEmployersRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[DoYouHaveAnyPreviousEmployersView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, List.empty, NormalMode)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, doYouHaveAnyPreviousEmployersRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, doYouHaveAnyPreviousEmployersRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
