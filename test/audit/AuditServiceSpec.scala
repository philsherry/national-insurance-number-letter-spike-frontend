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
import models._
import org.scalatest.{OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.mockito.Mockito.{times, verify}
import org.mockito.ArgumentMatchers.{eq => eqTo, any}
import org.scalatestplus.mockito.MockitoSugar
import pages._
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import play.api.Configuration

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

      val answers = UserAnswers("id")
        .set(WhatIsYourNamePage, WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(DoYouHaveAPreviousNamePage, true).success.value
        .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName(firstName = "first", middleNames = Some("middle"), lastName = "last")).success.value
        .set(WhatIsYourPreviousNamePage(Index(1)), WhatIsYourPreviousName(firstName = "first2", None, lastName = "last2")).success.value
        .set(WhatIsYourDateOfBirthPage, now).success.value
        .set(IsYourCurrentAddressInUkPage, true).success.value
        .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk(addressLine1 = "line 1", None, None, "postcode")).success.value
        .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational(addressLine1 = "line 1", None, None, "country")).success.value
        .set(DoYouHaveAnyPreviousAddressesPage, true).success.value
        .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), PreviousAddressUk(addressLine1 = "line 1", None, None, "postcode", from = LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2))).success.value
        .set(AreYouReturningFromLivingAbroadPage, true).success.value
        .set(WhatIsYourTelephoneNumberPage, "tel").success.value
        .set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).success.value
        .set(AreYouMarriedPage, true).success.value
        .set(WhenDidYouGetMarriedPage, now).success.value
        .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
        .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), PreviousMarriageOrPartnershipDetails(now, now, "nunya")).success.value
        .set(HaveYouEverClaimedChildBenefitPage, true).success.value
        .set(DoYouKnowYourChildBenefitNumberPage, true).success.value
        .set(WhatIsYourChildBenefitNumberPage, "cbn").success.value
        .set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
        .set(WhatOtherUkBenefitsHaveYouReceivedPage, "other benefits").success.value
        .set(HaveYouEverWorkedInUkPage, true).success.value
        .set(EmploymentHistoryPage, true).success.value
        .set(WhatIsYourEmployersNamePage(Index(0)), "previous employers name").success.value
        .set(WhatIsYourEmployersAddressPage(Index(0)), EmployersAddress("line 1", None, None, "postcode")).success.value
        .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), LocalDate.of(2000, 2, 1)).success.value
        .set(AreYouStillEmployedPage(Index(0)), true).success.value
        .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), LocalDate.of(2001, 3, 2)).success.value
        .set(DoYouHavePrimaryDocumentPage, true).success.value
        .set(WhichPrimaryDocumentPage, PrimaryDocument.Passport).success.value
        .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
        .set(WhichAlternativeDocumentsPage, AlternativeDocuments.values.toSet).success.value

      val expected: DownloadAuditEvent = DownloadAuditEvent(
        names = Names(
          currentName = Name(Some("title"), "first", Some("middle"), "last"),
          previousNames = List(
            Name(None, "first", Some("middle"), "last"),
            Name(None, "first2", None, "last2")
          )
        ),
        dateOfBirth = now,
        addresses = Addresses(
          currentAddress = Address("line 1", None, None, Some("postcode"), None),
          previousAddresses = List(PreviousAddress("line 1", None, None, Some("postcode"), None, LocalDate.of(2000, 2, 1), to = LocalDate.of(2001, 3, 2)))
        ),
        returningFromLivingAbroad = true,
        telephoneNumber = "tel",
        nationalInsuranceNumber = Some("AA123456A"),
        relationships = Relationships(
          currentRelationship = Some(Relationship(now)),
          previousRelationships = List(
            PreviousRelationship(now, now, "nunya")
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
        documents = List(
          "passport"
        )
      )

      val hc = HeaderCarrier()
      service.auditDownload(answers)(hc)

      verify(mockAuditConnector, times(1)).sendExplicitAudit(eqTo("downloadAuditEvent"), eqTo(expected))(eqTo(hc), any(), any())
    }
  }
}
