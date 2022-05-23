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
import org.scalatest.{OptionValues, TryValues}
import uk.gov.hmrc.domain.Nino
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class DoYouKnowYourNationalInsuranceNumberPageSpec extends AnyFreeSpec with Matchers with OptionValues with TryValues {

  "DoYouKnowYourNationalInsuranceNumberPage" - {

    "must remove national insurance number when false" in {
      val answers = UserAnswers("id")
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).get

      val result = answers.set(DoYouKnowYourNationalInsuranceNumberPage, false).success.value

      result.get(WhatIsYourNationalInsuranceNumberPage) must not be defined
    }

    "must not remove national insurance number when true" in {
      val answers = UserAnswers("id")
        .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).get

      val result = answers.set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value

      result.get(WhatIsYourNationalInsuranceNumberPage).value mustEqual Nino("AA123456A")
    }
  }
}
