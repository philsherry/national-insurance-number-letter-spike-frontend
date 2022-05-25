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

import audit.{AuditService, DownloadAuditEvent}
import com.dmanchester.playfop.sapi.PlayFop
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import org.apache.fop.apps.FOUserAgent
import org.apache.xmlgraphics.util.MimeConstants
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import models.{JourneyModel, UserAnswers}
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
                               ) extends FrontendBaseController with I18nSupport with Logging {

  private val userAgentBlock: FOUserAgent => Unit = { foUserAgent: FOUserAgent =>
    foUserAgent.setAccessibility(true)
    foUserAgent.setPdfUAEnabled(true)
    foUserAgent.setAuthor("HMRC forms service")
    foUserAgent.setProducer("HMRC forms services")
    foUserAgent.setCreator("HMRC forms services")
    foUserAgent.setSubject("Get your National Insurance number by post form")
    foUserAgent.setTitle("Get your National Insurance number by post form")
  }

  private def withPrintModel(answers: UserAnswers)(fn: PrintModel => Result): Result = {

    val (maybeFailures, maybeModel) = JourneyModel.from(answers).pad

    val errors = maybeFailures.map { failures =>
      val message = failures.toChain.toList.map(_.path).mkString(", ")
      s" at: $message"
    }.getOrElse("")

    lazy val backup = PrintModel.from(answers).map { model =>
      logger.warn(s"Journey model creation failed but was recovered$errors")
      model
    }

    (maybeModel.map(PrintModel.from) orElse backup).map { printModel =>
      if (errors.nonEmpty) {
        logger.warn(s"Journey model creation successful with warnings$errors")
      }
      fn(printModel)
    }.getOrElse {
      logger.warn(s"Journey model creation failed and could not be recovered$errors")
      Redirect(routes.JourneyRecoveryController.onPageLoad())
    }
  }

  def onDownload: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    JourneyModel.from(request.userAnswers).foreach(auditService.auditDownload(_))

    withPrintModel(request.userAnswers) { printModel =>
      val pdf = fop.processTwirlXml(template.render(printModel, implicitly), MimeConstants.MIME_PDF, foUserAgentBlock = userAgentBlock)
      Ok(pdf).as("application/octet-stream").withHeaders(CONTENT_DISPOSITION -> "attachment; filename=get-your-national-insurance-number-by-post.pdf")
    }
  }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    withPrintModel(request.userAnswers) { printModel =>
      Ok(view(printModel))
    }
  }
}
