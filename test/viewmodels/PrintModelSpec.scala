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

package viewmodels

import base.SpecBase
import models.JourneyModel.PreviousRelationship
import models._

import java.time.{LocalDate, YearMonth}

class PrintModelSpec extends SpecBase {

  "PrintModel" - {

    "from" in {

      val model = JourneyModel(
        currentName = WhatIsYourName(title = Some("title"), firstName = "first", middleNames = Some("middle"), lastName = "last"),
        previousNames = List(
          WhatIsYourPreviousName(firstName = "prev", middleNames = Some("prev2"), lastName = "prev3")
        ),
        dateOfBirth = LocalDate.of(1990, 12, 1),
        gender = WhatIsYourGender.PreferNotToSay,
        telephoneNumber = "1234567890",
        nationalInsuranceNumber = Some("AA123456A"),
        returningFromLivingAbroad = false,
        currentAddress = CurrentAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "AA1 1AA"),
        previousAddresses = List(
          PreviousAddressUk(addressLine1 = "line 1", addressLine2 = None, addressLine3 = None, postcode = "AA1 1AA", from = YearMonth.of(2000, 1), to = YearMonth.of(2001, 1))
        ),
        currentRelationship = Some(JourneyModel.CurrentRelationship(relationshipType = CurrentRelationshipType.Marriage, from = LocalDate.of(2000, 5, 1))),
        previousRelationships = List(
          PreviousRelationship(relationshipType = PreviousRelationshipType.Marriage, LocalDate.of(2005, 2, 1), LocalDate.of(2006, 3, 2), "reason")
        ),
        claimedChildBenefit = true,
        childBenefitNumber = Some("CHB12345678"),
        otherBenefits = Some("other benefits"),
        employers = List(
          JourneyModel.Employer("emp 1", EmployersAddress("line 1", None, None, "AA1 1AA"), LocalDate.of(2013, 3, 2), None)
        ),
        primaryDocument = None,
        alternativeDocuments = List("adoption-certificate", "work-permit")
      )

      val expected = PrintModel(
        WhatIsYourName(Some("title"), "first", Some("middle"), "last"),
        List(WhatIsYourPreviousName("prev", Some("prev2"), "prev3")),
        "1 December 1990",
        WhatIsYourGender.PreferNotToSay,
        List("line 1", "AA1 1AA"),
        List(PreviousAddressPrintModel(List("line 1", "AA1 1AA"), "January 2000", "January 2001")),
        returningFromLivingAbroad = false,
        "1234567890",
        Some("AA123456A"),
        Some("marriage"),
        Some("1 May 2000"),
        Seq(PreviousMarriageOrPartnershipPrintModel("marriage", "1 February 2005", "2 March 2006", "reason")),
        claimedChildBenefit = true,
        Some("CHB12345678"),
        Some("other benefits"),
        List(EmployerPrintModel("emp 1", List("line 1", "AA1 1AA"), "2 March 2013", None)),
        List.empty,
        None,
        Some(List("whichAlternativeDocuments.adoption-certificate", "whichAlternativeDocuments.work-permit"))
      )

      PrintModel.from(model) mustEqual expected
    }
  }
}