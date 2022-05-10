package audit

import com.google.inject.Inject
import models.UserAnswers
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

@Singleton
class AuditService @Inject() (connector: AuditConnector){

  def auditDownload(userAnswers: UserAnswers): Unit = {

  }

}
