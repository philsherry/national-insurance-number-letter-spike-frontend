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
import forms.DoYouHaveAPreviousNameFormProvider

import javax.inject.Inject
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.{DoYouHaveAPreviousNamePage, PreviousNamesQuery}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.listwithactions.ListWithActionsItem
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.PreviousNameSummary
import views.html.DoYouHaveAPreviousNameView

import scala.concurrent.{ExecutionContext, Future}

class DoYouHaveAPreviousNameController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: DoYouHaveAPreviousNameFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: DoYouHaveAPreviousNameView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def listItems(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListWithActionsItem] =
    answers.get(PreviousNamesQuery).getOrElse(Seq.empty)
      .indices.map(PreviousNameSummary.item(answers, mode, _))

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      Ok(view(form, listItems(request.userAnswers, mode), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, listItems(request.userAnswers, mode), mode))),
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DoYouHaveAPreviousNamePage, value))
          } yield Redirect(navigator.nextPage(DoYouHaveAPreviousNamePage, mode, updatedAnswers))
      )
  }
}
