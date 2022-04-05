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
import forms.AreYouSureYouWantToRemovePreviousAddressFormProvider

import javax.inject.Inject
import models.{Index, Mode, UserAnswers}
import navigation.Navigator
import pages.{AreYouSureYouWantToRemovePreviousAddressPage, PreviousAddressQuery, WhatIsYourPreviousAddressInternationalPage, WhatIsYourPreviousAddressUkPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.AreYouSureYouWantToRemovePreviousAddressView

import scala.concurrent.{ExecutionContext, Future}

class AreYouSureYouWantToRemovePreviousAddressController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AreYouSureYouWantToRemovePreviousAddressFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AreYouSureYouWantToRemovePreviousAddressView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  private def getLines(answers: UserAnswers, index: Index): Option[List[String]] = {
    val ukLines = answers.get(WhatIsYourPreviousAddressUkPage(index)).map(_.lines)
    val internationalLines = answers.get(WhatIsYourPreviousAddressInternationalPage(index)).map(_.lines)
    ukLines orElse internationalLines
  }

  private def removeAddress(answers: UserAnswers, index: Index): Future[Unit] = for {
    updatedAnswers <- Future.fromTry(answers.remove(PreviousAddressQuery(index)))
    _              <- sessionRepository.set(updatedAnswers)
  } yield ()

  def onPageLoad(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getLines(request.userAnswers, index)
        .map(lines => Ok(view(form, lines, index, mode)))
        .getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }

  def onSubmit(index: Index, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => Future.successful {
          val lines = getLines(request.userAnswers, index).getOrElse(List.empty)
          BadRequest(view(formWithErrors, lines, index, mode))
        },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AreYouSureYouWantToRemovePreviousAddressPage(index), value))
            _              <- if (value) removeAddress(request.userAnswers, index) else Future.successful(())
          } yield Redirect(navigator.nextPage(AreYouSureYouWantToRemovePreviousAddressPage(index), mode, updatedAnswers))
      )
  }
}
