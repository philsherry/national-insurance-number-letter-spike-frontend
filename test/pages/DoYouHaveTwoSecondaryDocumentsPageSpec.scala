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

import models.AlternativeDocuments.{AdoptionCertificate, MarriageOrCivilPartnershipCertificate}
import models.{AlternativeDocuments, UserAnswers}
import org.scalatest.{OptionValues, TryValues}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class DoYouHaveTwoSecondaryDocumentsPageSpec extends AnyFreeSpec with Matchers with OptionValues with TryValues {

  "DoYouHaveTwoSecondaryDocumentsPage" - {

    "must remove which secondary documents when false" in {
      val answers =
        UserAnswers("id")
          .set(WhichAlternativeDocumentsPage, Set[AlternativeDocuments](MarriageOrCivilPartnershipCertificate, AdoptionCertificate)).success.value

      val result = answers.set(DoYouHaveTwoSecondaryDocumentsPage, false).success.value

      result.get(WhichAlternativeDocumentsPage) must not be defined
    }

    "must not remove which secondary documents when true" in {
      val answers =
        UserAnswers("id")
          .set(WhichAlternativeDocumentsPage, Set[AlternativeDocuments](MarriageOrCivilPartnershipCertificate, AdoptionCertificate)).success.value

      val result = answers.set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value

      result.get(WhichAlternativeDocumentsPage).value mustBe Set[AlternativeDocuments](MarriageOrCivilPartnershipCertificate, AdoptionCertificate)
    }
  }
}
