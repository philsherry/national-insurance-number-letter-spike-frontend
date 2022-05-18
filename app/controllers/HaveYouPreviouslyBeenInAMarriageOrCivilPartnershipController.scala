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
import forms.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipFormProvider

import javax.inject.Inject
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.{HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, PreviousRelationshipsQuery}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.hmrcfrontend.views.Aliases.ListWithActionsItem
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.PreviousRelationshipSummary
import views.html.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipView

import scala.concurrent.{ExecutionContext, Future}

class HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def listItems(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListWithActionsItem] =
    answers.get(PreviousRelationshipsQuery).getOrElse(Seq.empty)
      .indices.flatMap(PreviousRelationshipSummary.item(answers, mode, _))

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, listItems(request.userAnswers, mode), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, listItems(request.userAnswers, mode), mode))),
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, value))
          } yield Redirect(navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, mode, updatedAnswers))
      )
  }
}
