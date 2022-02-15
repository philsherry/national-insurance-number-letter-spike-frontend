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
import forms.DoYouHaveAnyPreviousAddressesFormProvider

import javax.inject.Inject
import models.{Index, Mode, UserAnswers}
import navigation.Navigator
import pages.{DoYouHaveAnyPreviousAddressesPage, PreviousAddressesQuery, WhatIsYourPreviousAddressInternationalPage, WhatIsYourPreviousAddressUkPage}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DoYouHaveAnyPreviousAddressesView

import scala.concurrent.{ExecutionContext, Future}

class DoYouHaveAnyPreviousAddressesController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: DoYouHaveAnyPreviousAddressesFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: DoYouHaveAnyPreviousAddressesView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def listItems(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListWithActionsItem] = {

    val previousAddresses = answers.get(PreviousAddressesQuery).getOrElse(Seq.empty)

    previousAddresses.indices.flatMap { i =>
      answers.get(WhatIsYourPreviousAddressUkPage(Index(i))).map(_.lines) orElse
        answers.get(WhatIsYourPreviousAddressInternationalPage(Index(i))).map(_.lines)
    }.zipWithIndex.map { case (lines, i) =>
      ListWithActionsItem(
        name = Text(lines.mkString(", ")),
        actions = List(
          ListWithActionsAction(content = Text(messages("site.change")), href = routes.IsYourPreviousAddressInUkController.onPageLoad(Index(i), mode).url),
          ListWithActionsAction(content = Text(messages("site.remove")), href = routes.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(Index(i), mode).url)
        )
      )
    }
  }

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
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DoYouHaveAnyPreviousAddressesPage, value))
          } yield Redirect(navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, mode, updatedAnswers))
      )
  }
}
