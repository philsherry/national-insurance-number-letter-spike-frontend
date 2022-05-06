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
import forms.AreYouSureYouWantToRemoveEmployerFormProvider

import javax.inject.Inject
import models.{Index, Mode, UserAnswers}
import navigation.Navigator
import pages.{AreYouSureYouWantToRemovePreviousEmployerPage, PreviousAddressQuery, PreviousEmployerQuery, WhatIsYourEmployersNamePage, WhenDidYouStartWorkingForEmployerPage, WhenDidYouStopWorkingForEmployerPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.AreYouSureYouWantToRemoveEmployerView

import scala.concurrent.{ExecutionContext, Future}

class AreYouSureYouWantToRemoveEmployerController @Inject()(
                                                             override val messagesApi: MessagesApi,
                                                             sessionRepository: SessionRepository,
                                                             navigator: Navigator,
                                                             identify: IdentifierAction,
                                                             getData: DataRetrievalAction,
                                                             requireData: DataRequiredAction,
                                                             formProvider: AreYouSureYouWantToRemoveEmployerFormProvider,
                                                             val controllerComponents: MessagesControllerComponents,
                                                             view: AreYouSureYouWantToRemoveEmployerView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def removeEmployer(answers: UserAnswers, index: Index): Future[Unit] = for {
    updatedAnswers <- Future.fromTry(answers.remove(PreviousEmployerQuery(index)))
    _              <- sessionRepository.set(updatedAnswers)
  } yield ()

  def onPageLoad(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val employerName = request.userAnswers.get(WhatIsYourEmployersNamePage(index))
      val from = request.userAnswers.get(WhenDidYouStartWorkingForEmployerPage(index))
      val to = request.userAnswers.get(WhenDidYouStopWorkingForEmployerPage(index))
      Ok(view(form, employerName, from, to, mode, index))
  }

  def onSubmit(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val employerName = request.userAnswers.get(WhatIsYourEmployersNamePage(index))
          val from = request.userAnswers.get(WhenDidYouStartWorkingForEmployerPage(index))
          val to = request.userAnswers.get(WhenDidYouStopWorkingForEmployerPage(index))
          Future.successful(BadRequest(view(formWithErrors, employerName, from, to, mode, index)))
        },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AreYouSureYouWantToRemovePreviousEmployerPage(index), value))
            _              <- if (value) removeEmployer(updatedAnswers, index: Index) else Future.unit
          } yield Redirect(navigator.nextPage(AreYouSureYouWantToRemovePreviousEmployerPage(index), mode, updatedAnswers))
      )
  }
}
