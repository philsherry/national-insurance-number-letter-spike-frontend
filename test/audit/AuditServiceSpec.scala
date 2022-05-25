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

import audit.DownloadAuditEvent._
import models.JourneyModel.PreviousRelationship
import models._
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global

class AuditServiceSpec extends AnyFreeSpec with Matchers with OptionValues with TryValues with MockitoSugar {

  val mockAuditConnector: AuditConnector = mock[AuditConnector]
  val configuration: Configuration = Configuration(
    "auditing.downloadEventName" -> "downloadAuditEvent"
  )
  val service = new AuditService(mockAuditConnector, configuration)

  "auditDownload" - {

    "must call the audit connector with the correct payload" in {

      val now = LocalDate.now

      val model: JourneyModel = JourneyModel(
        currentName = WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last"),
        previousNames = List(
          WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last"),
          WhatIsYourPreviousName(firstName = "first2", middleNames = None, lastName = "last2")
        ),
        dateOfBirth = now,
        gender = WhatIsYourGender.PreferNotToSay,
        telephoneNumber = "tel",
        nationalInsuranceNumber = Some("AA123456A"),
        returningFromLivingAbroad = true,
        currentAddress = CurrentAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "postcode"),
        previousAddresses = List(
          PreviousAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))
        ),
        currentRelationship = Some(JourneyModel.CurrentRelationship(relationshipType = CurrentRelationshipType.Marriage, from = now)),
        previousRelationships = List(
          PreviousRelationship(relationshipType = PreviousRelationshipType.CivilPartnership, now, now, "nunya")
        ),
        claimedChildBenefit = true,
        childBenefitNumber = Some("cbn"),
        otherBenefits = Some("other benefits"),
        employers = List(
          JourneyModel.Employer("previous employers name", EmployersAddress("line 1", None, None, "postcode"), LocalDate.of(2000, 2, 1), Some(LocalDate.of(2001, 3, 2)))
        ),
        primaryDocument = Some("passport"),
        alternativeDocuments = List.empty
      )

      val expected: DownloadAuditEvent = DownloadAuditEvent(
        names = Names(
          currentName = Name(Some("title"), "first", Some("middle"), "last"),
          previousNames = List(
            Name(None, "first", Some("middle"), "last"),
            Name(None, "first2", None, "last2")
          )
        ),
        dateOfBirth = now,
        gender = WhatIsYourGender.PreferNotToSay,
        addresses = Addresses(
          currentAddress = Address("line 1", None, None, Some("postcode"), None),
          previousAddresses = List(PreviousAddress("line 1", None, None, Some("postcode"), None, LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2)))
        ),
        returningFromLivingAbroad = true,
        telephoneNumber = "tel",
        nationalInsuranceNumber = Some("AA123456A"),
        relationships = Relationships(
          currentRelationship = Some(Relationship("marriage", now)),
          previousRelationships = List(
            DownloadAuditEvent.PreviousRelationship("civilPartnership", now, now, "nunya")
          )
        ),
        benefits = Benefits(
          claimedChildBenefit = true,
          childBenefitNumber = Some("cbn"),
          otherBenefits = Some("other benefits")
        ),
        employers = List(
          Employer("previous employers name", EmployerAddress("line 1", None, None, "postcode"), LocalDate.of(2000, 2, 1), Some(LocalDate.of(2001, 3, 2)))
        ),
        documents = List("passport")
      )

      val hc = HeaderCarrier()
      service.auditDownload(model)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("downloadAuditEvent"), eqTo(expected))(eqTo(hc), any(), any())
    }
  }
}
