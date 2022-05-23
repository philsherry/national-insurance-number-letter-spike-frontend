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

import models.{EmployersAddress, Index, UserAnswers}
import org.scalatest.{OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

import java.time.LocalDate

class HaveYouEverWorkedInUkPageSpec extends AnyFreeSpec with Matchers with OptionValues with TryValues {

  "HaveYouEverWorkedInUkPage" - {

    "must remove all employer details when false" in {

      val address = EmployersAddress("line 1", None, None, "postcode")
      val date    = LocalDate.now

      val answers =
        UserAnswers("id")
          .set(WhatIsYourEmployersNamePage(Index(0)), "name").success.value
          .set(WhatIsYourEmployersAddressPage(Index(0)), address).success.value
          .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), date).success.value
          .set(AreYouStillEmployedPage(Index(0)), false).success.value
          .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), date).success.value

      val result = answers.set(HaveYouEverWorkedInUkPage, false).success.value

      result.get(WhatIsYourEmployersNamePage(Index(0)))           must not be defined
      result.get(WhatIsYourEmployersAddressPage(Index(0)))        must not be defined
      result.get(WhenDidYouStartWorkingForEmployerPage(Index(0))) must not be defined
      result.get(AreYouStillEmployedPage(Index(0)))               must not be defined
      result.get(WhenDidYouStopWorkingForEmployerPage(Index(0)))  must not be defined
    }

    "must not remove any employer details when true" in {

      val address = EmployersAddress("line 1", None, None, "postcode")
      val date    = LocalDate.now

      val answers =
        UserAnswers("id")
          .set(WhatIsYourEmployersNamePage(Index(0)), "name").success.value
          .set(WhatIsYourEmployersAddressPage(Index(0)), address).success.value
          .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), date).success.value
          .set(AreYouStillEmployedPage(Index(0)), false).success.value
          .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), date).success.value

      val result = answers.set(HaveYouEverWorkedInUkPage, true).success.value

      result.get(WhatIsYourEmployersNamePage(Index(0))).value           mustEqual "name"
      result.get(WhatIsYourEmployersAddressPage(Index(0))).value        mustEqual address
      result.get(WhenDidYouStartWorkingForEmployerPage(Index(0))).value mustEqual date
      result.get(AreYouStillEmployedPage(Index(0))).value               mustEqual false
      result.get(WhenDidYouStopWorkingForEmployerPage(Index(0))).value  mustEqual date
    }
  }
}
