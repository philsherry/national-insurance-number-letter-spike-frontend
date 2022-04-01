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

package pages

import models.{PreviousMarriageOrPartnershipDetails, UserAnswers}
import pages.behaviours.PageBehaviours

import java.time.LocalDate

class HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPageSpec extends PageBehaviours {

  "HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage" - {

    beRetrievable[Boolean](HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage)

    beSettable[Boolean](HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage)

    beRemovable[Boolean](HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage)

    "must previous name when false" in {
      val previous = PreviousMarriageOrPartnershipDetails(LocalDate.of(2000,1,1), LocalDate.of(2001, 1, 1), "reason")

      val answers = UserAnswers("id")
        .set(PreviousMarriageOrPartnershipDetailsPage, previous).get

      val result = answers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, false).success.value

      result.get(PreviousMarriageOrPartnershipDetailsPage) must not be defined
    }

    "must not remove previous name when true" in {
      val previous = PreviousMarriageOrPartnershipDetails(LocalDate.of(2000,1,1), LocalDate.of(2001, 1, 1), "reason")

      val answers = UserAnswers("id")
        .set(PreviousMarriageOrPartnershipDetailsPage, previous).get

      val result = answers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value

      result.get(PreviousMarriageOrPartnershipDetailsPage).value mustEqual previous
    }
  }
}
