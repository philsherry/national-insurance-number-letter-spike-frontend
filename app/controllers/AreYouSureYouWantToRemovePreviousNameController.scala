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
import forms.AreYouSureYouWantToRemovePreviousNameFormProvider

import javax.inject.Inject
import models.{Index, Mode, UserAnswers}
import navigation.Navigator
import pages.{AreYouSureYouWantToRemovePreviousNamePage, PreviousNameQuery, WhatIsYourPreviousNamePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.AreYouSureYouWantToRemovePreviousNameView

import scala.concurrent.{ExecutionContext, Future}

class AreYouSureYouWantToRemovePreviousNameController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AreYouSureYouWantToRemovePreviousNameFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AreYouSureYouWantToRemovePreviousNameView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def getName(index: Index, userAnswers: UserAnswers): String =
    userAnswers.get(WhatIsYourPreviousNamePage(index))
      .map(n => Seq(
        Some(n.firstName),
        n.middleNames,
        Some(n.lastName)).flatten).getOrElse(Seq.empty).mkString(" ")

  private def removeName(answers: UserAnswers, index: Index): Future[Unit] = for {
    updatedAnswers <- Future.fromTry(answers.remove(PreviousNameQuery(index)))
    _ <- sessionRepository.set(updatedAnswers)
  } yield ()

  def onPageLoad(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val name = getName(index, request.userAnswers)

      val preparedForm = request.userAnswers.get(AreYouSureYouWantToRemovePreviousNamePage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, name, mode, index))
  }

  def onSubmit(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val name = getName(index, request.userAnswers)
          Future.successful(BadRequest(view(formWithErrors, name, mode, index)))
        },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AreYouSureYouWantToRemovePreviousNamePage(index), value))
            _              <- if (value) removeName(updatedAnswers, index) else Future.unit
          } yield Redirect(navigator.nextPage(AreYouSureYouWantToRemovePreviousNamePage(index), mode, updatedAnswers))
      )
  }
}
