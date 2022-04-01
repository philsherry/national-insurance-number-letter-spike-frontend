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

import models.UserAnswers
import pages.behaviours.PageBehaviours

import java.time.LocalDate

class AreYouInACivilPartnershipPageSpec extends PageBehaviours {

  "AreYouInACivilPartnershipPage" - {

    beRetrievable[Boolean](AreYouInACivilPartnershipPage)

    beSettable[Boolean](AreYouInACivilPartnershipPage)

    beRemovable[Boolean](AreYouInACivilPartnershipPage)

    "must remove civil partnership date when false" in {
      val answers = UserAnswers("id")
        .set(WhenDidYouEnterACivilPartnershipPage, LocalDate.of(2000, 1, 1)).get

      val result = answers.set(AreYouInACivilPartnershipPage, false).success.value

      result.get(WhenDidYouEnterACivilPartnershipPage) must not be defined
    }

    "must not remove civil partnership date when true" in {
      val answers = UserAnswers("id")
        .set(WhenDidYouEnterACivilPartnershipPage, LocalDate.of(2000, 1, 1)).get

      val result = answers.set(AreYouInACivilPartnershipPage, true).success.value

      result.get(WhenDidYouEnterACivilPartnershipPage).value must equal (LocalDate.of(2000, 1, 1))
    }
  }
}
