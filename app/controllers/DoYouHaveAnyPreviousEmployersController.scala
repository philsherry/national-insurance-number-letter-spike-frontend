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
import forms.DoYouHaveAnyPreviousEmployersFormProvider
import models.{Index, Mode, UserAnswers}
import navigation.Navigator
import pages._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DoYouHaveAnyPreviousEmployersView

import java.time.format.DateTimeFormatter
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DoYouHaveAnyPreviousEmployersController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: DoYouHaveAnyPreviousEmployersFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: DoYouHaveAnyPreviousEmployersView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  private def listItems(answers: UserAnswers, mode: Mode)(implicit messages: Messages): Seq[ListWithActionsItem] = {

    val previousEmployers = answers.get(PreviousEmployersQuery).getOrElse(Seq.empty)

    previousEmployers.indices.map { i =>

      val name = answers.get(WhatIsYourPreviousEmployersNamePage(Index(i)))
      val range = for {
        from <- answers.get(WhenDidYouStartWorkingForPreviousEmployerPage(Index(i))).map(_.format(dateFormatter))
        to <- answers.get(WhenDidYouStopWorkingForPreviousEmployerPage(Index(i))).map(_.format(dateFormatter))
      } yield s"${messages("doYouHaveAnyPreviousEmployers.from")} $from ${messages("doYouHaveAnyPreviousEmployers.to")} $to"

      ListWithActionsItem(
        name = Text(List(name, range).flatten.mkString(", ")),
        actions = List(
          ListWithActionsAction(content = Text(messages("site.change")), href = routes.WhatIsYourPreviousEmployersNameController.onPageLoad(Index(i), mode).url),
          ListWithActionsAction(content = Text(messages("site.remove")), href = routes.AreYouSureYouWantToRemovePreviousEmployerController.onPageLoad(Index(i), mode).url)
        )
      )
    }
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(DoYouHaveAnyPreviousEmployersPage) match {
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
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DoYouHaveAnyPreviousEmployersPage, value))
          } yield Redirect(navigator.nextPage(DoYouHaveAnyPreviousEmployersPage, mode, updatedAnswers))
      )
  }
}
