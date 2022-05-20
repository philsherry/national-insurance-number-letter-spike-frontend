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

package audit

import com.google.inject.{Inject, Singleton}
import play.api.{Logging, Configuration}
import models.UserAnswers
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.http.HeaderCarrier
import scala.concurrent.ExecutionContext

@Singleton
class AuditService @Inject() (connector: AuditConnector, configuration: Configuration)(implicit ec: ExecutionContext) extends Logging {

  private val downloadEventName = configuration.get[String]("auditing.downloadEventName")

  def auditDownload(answers: UserAnswers)(implicit hc: HeaderCarrier): Unit =
    DownloadAuditEvent(answers).fold(failedQueries => {
      val queries = failedQueries.map(_.path.toString).toNonEmptyList.toList.mkString(", ")
      logger.warn(s"Unable to create download event for: ${answers.id}, with missing values: $queries")
    }, data =>
      connector.sendExplicitAudit(downloadEventName, data)
    )
}
