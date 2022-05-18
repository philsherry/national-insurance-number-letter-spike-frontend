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
import forms.WhenDidYouGetMarriedFormProvider

import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.{CurrentRelationshipTypePage, WhenDidYouGetMarriedPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.WhenDidYouGetMarriedView

import scala.concurrent.{ExecutionContext, Future}

class WhenDidYouGetMarriedController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: WhenDidYouGetMarriedFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: WhenDidYouGetMarriedView
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val relationshipType = request.userAnswers.get(CurrentRelationshipTypePage)

      val preparedForm = request.userAnswers.get(WhenDidYouGetMarriedPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, relationshipType, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val relationshipType = request.userAnswers.get(CurrentRelationshipTypePage)
          Future.successful(BadRequest(view(formWithErrors, relationshipType, mode)))
        },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(WhenDidYouGetMarriedPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(WhenDidYouGetMarriedPage, mode, updatedAnswers))
      )
  }
}
