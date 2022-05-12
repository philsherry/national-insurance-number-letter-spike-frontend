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

import audit.AuditService
import com.dmanchester.playfop.sapi.PlayFop
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import org.apache.fop.apps.FOUserAgent
import org.apache.xmlgraphics.util.MimeConstants
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.PrintModel
import views.xml.xml.PrintTemplate

import javax.inject.Inject

class PrintController @Inject()(
                                 val controllerComponents: MessagesControllerComponents,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 fop: PlayFop,
                                 template: PrintTemplate,
                                 view: views.html.PrintView,
                                 auditService: AuditService
                               ) extends FrontendBaseController with I18nSupport {

  val userAgentBlock: FOUserAgent => Unit = { foUserAgent: FOUserAgent =>
    foUserAgent.setAccessibility(true)
    foUserAgent.setPdfUAEnabled(true)
    foUserAgent.setAuthor("HMRC forms service")
    foUserAgent.setProducer("HMRC forms services")
    foUserAgent.setCreator("HMRC forms services")
    foUserAgent.setSubject("Get your National Insurance number by post form")
    foUserAgent.setTitle("Get your National Insurance number by post form")
  }

  def onDownload: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    PrintModel.from(request.userAnswers).map { model =>
      val pdf = fop.processTwirlXml(template.render(model, implicitly), MimeConstants.MIME_PDF, foUserAgentBlock = userAgentBlock)
      auditService.auditDownload(request.userAnswers)
      
      Ok(pdf).as(MimeConstants.MIME_PDF).withHeaders(CONTENT_DISPOSITION -> "filename=get-your-national-insurance-number-by-post.pdf")
    }.getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    PrintModel.from(request.userAnswers).map { model =>
      Ok(view(model))
    }.getOrElse(Redirect(routes.JourneyRecoveryController.onPageLoad()))
  }
}
