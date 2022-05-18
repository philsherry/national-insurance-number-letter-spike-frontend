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

import controllers.actions._
import forms.WhenDidYouStartWorkingForEmployerFormProvider

import javax.inject.Inject
import models.{Index, Mode}
import navigation.Navigator
import pages.{WhatIsYourEmployersNamePage, WhenDidYouStartWorkingForEmployerPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.twirl.api.HtmlFormat
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.WhenDidYouStartWorkingForEmployerView

import scala.concurrent.{ExecutionContext, Future}

class WhenDidYouStartWorkingForEmployerController @Inject()(
                                                             override val messagesApi: MessagesApi,
                                                             sessionRepository: SessionRepository,
                                                             navigator: Navigator,
                                                             identify: IdentifierAction,
                                                             getData: DataRetrievalAction,
                                                             requireData: DataRequiredAction,
                                                             formProvider: WhenDidYouStartWorkingForEmployerFormProvider,
                                                             val controllerComponents: MessagesControllerComponents,
                                                             view: WhenDidYouStartWorkingForEmployerView
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  def form = formProvider()

  def onPageLoad(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(WhatIsYourEmployersNamePage(index)) {
        employerName =>

          val preparedForm = request.userAnswers.get(WhenDidYouStartWorkingForEmployerPage(index)) match {
            case None => form
            case Some(value) => form.fill(value)
          }

          Ok(view(preparedForm, index, mode, HtmlFormat.escape(employerName).toString))
        }
  }

  def onSubmit(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(WhatIsYourEmployersNamePage(index)) {
        employerName =>

          form.bindFromRequest().fold(
            formWithErrors =>
              Future.successful(BadRequest(view(formWithErrors, index, mode, HtmlFormat.escape(employerName).toString))),

            value =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(WhenDidYouStartWorkingForEmployerPage(index), value))
                _ <- sessionRepository.set(updatedAnswers)
              } yield Redirect(navigator.nextPage(WhenDidYouStartWorkingForEmployerPage(index), mode, updatedAnswers))
          )
        }
  }
}