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

import com.dmanchester.playfop.sapi.PlayFop
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import org.apache.xmlgraphics.util.MimeConstants
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.PrintModel
import views.xml.xml.TestTemplate

import javax.inject.Inject

class PrintController @Inject()(
                                 val controllerComponents: MessagesControllerComponents,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 fop: PlayFop,
                                 template: TestTemplate
                               ) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    PrintModel.from(request.userAnswers).map { model =>
      val pdf = fop.processTwirlXml(template.render(model, implicitly), MimeConstants.MIME_PDF)

      Ok(pdf).as(MimeConstants.MIME_PDF).withHeaders(CONTENT_DISPOSITION -> "filename=HMRCForm")
    }.getOrElse(BadRequest("Missing user answers items"))
  }
}
