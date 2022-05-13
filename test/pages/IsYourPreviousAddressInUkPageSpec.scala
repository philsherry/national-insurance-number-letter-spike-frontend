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

import models.{Index, PreviousAddressInternational, PreviousAddressUk, UserAnswers}
import pages.behaviours.PageBehaviours

import java.time.LocalDate

class IsYourPreviousAddressInUkPageSpec extends PageBehaviours {

  "IsYourPreviousAddressInUkPage" - {

    beRetrievable[Boolean](IsYourPreviousAddressInUkPage(Index(0)))

    beSettable[Boolean](IsYourPreviousAddressInUkPage(Index(0)))

    beRemovable[Boolean](IsYourPreviousAddressInUkPage(Index(0)))

    "must remove what is your previous address uk when set to false" in {
      val answer = PreviousAddressUk(
        "line 1",
        None,
        None,
        "postcode",
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 2, 2)
      )
      val answers = UserAnswers("id")
        .set(WhatIsYourPreviousAddressUkPage(Index(0)), answer)
        .success.value
      val result = answers.set(IsYourPreviousAddressInUkPage(Index(0)), false).success.value
      result.get(WhatIsYourPreviousAddressUkPage(Index(0))) must not be defined
    }

    "must remove what is your previous address international when set to true" in {
      val answer = PreviousAddressInternational(
        "line 1",
        None,
        None,
        None,
        "country",
        LocalDate.of(2000, 1, 1),
        LocalDate.of(2000, 2, 2)
      )
      val answers = UserAnswers("id")
        .set(WhatIsYourPreviousAddressInternationalPage(Index(0)), answer).success.value
      val result = answers.set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
      result.get(WhatIsYourPreviousAddressInternationalPage(Index(0))) must not be defined
    }
  }
}
