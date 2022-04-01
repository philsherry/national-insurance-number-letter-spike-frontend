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

import models.{UserAnswers, WhatIsYourPreviousName}
import pages.behaviours.PageBehaviours

class HaveYouEverClaimedChildBenefitPageSpec extends PageBehaviours {

  "HaveYouEverClaimedChildBenefitPage" - {

    beRetrievable[Boolean](HaveYouEverClaimedChildBenefitPage)

    beSettable[Boolean](HaveYouEverClaimedChildBenefitPage)

    beRemovable[Boolean](HaveYouEverClaimedChildBenefitPage)

    "must remove child benefit number when false" in {
      val answers = UserAnswers("id")
        .set(DoYouKnowYourChildBenefitNumberPage, true).get
        .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get

      val result = answers.set(HaveYouEverClaimedChildBenefitPage, false).success.value

      result.get(DoYouKnowYourChildBenefitNumberPage) must not be defined
      result.get(WhatIsYourChildBenefitNumberPage) must not be defined
    }

    "must not remove child benefit number when true" in {
      val answers = UserAnswers("id")
        .set(DoYouKnowYourChildBenefitNumberPage, true).get
        .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get

      val result = answers.set(DoYouHaveAPreviousNamePage, true).success.value

      result.get(DoYouKnowYourChildBenefitNumberPage).value mustEqual true
      result.get(WhatIsYourChildBenefitNumberPage).value mustEqual "CHB12345678"
    }
  }
}
