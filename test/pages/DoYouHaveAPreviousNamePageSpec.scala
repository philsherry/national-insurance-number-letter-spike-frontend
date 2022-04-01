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

class DoYouHaveAPreviousNamePageSpec extends PageBehaviours {

  "DoYouHaveAPreviousNamePage" - {

    beRetrievable[Boolean](DoYouHaveAPreviousNamePage)

    beSettable[Boolean](DoYouHaveAPreviousNamePage)

    beRemovable[Boolean](DoYouHaveAPreviousNamePage)

    "must previous name when false" in {
      val answers = UserAnswers("id")
        .set(WhatIsYourPreviousNamePage, WhatIsYourPreviousName("first", None, "last")).get

      val result = answers.set(DoYouHaveAPreviousNamePage, false).success.value

      result.get(WhatIsYourPreviousNamePage) must not be defined
    }

    "must not remove previous name when true" in {
      val answers = UserAnswers("id")
        .set(WhatIsYourPreviousNamePage, WhatIsYourPreviousName("first", None, "last")).get

      val result = answers.set(DoYouHaveAPreviousNamePage, true).success.value

      result.get(WhatIsYourPreviousNamePage).value mustEqual WhatIsYourPreviousName("first", None, "last")
    }
  }
}
