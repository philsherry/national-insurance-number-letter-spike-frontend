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

import models.AlternativeDocuments.HomeOfficeOrTravelDocument
import models.PrimaryDocument.BirthCertificate
import models.{AlternativeDocuments, UserAnswers}
import org.scalatest.TryValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class DoYouHavePrimaryDocumentPageSpec extends AnyFreeSpec with Matchers with TryValues {

  "DoYouHavePrimaryDocumentPage" - {

    "must remove which primary document when false" in {

      val answers =
        UserAnswers("id")
          .set(WhichPrimaryDocumentPage, BirthCertificate).success.value

      val result = answers.set(DoYouHavePrimaryDocumentPage, false).success.value

      result.get(WhichPrimaryDocumentPage) must not be defined
    }

    "must remove alternative documents when true" in {

      val answers =
        UserAnswers("id")
          .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
          .set(WhichAlternativeDocumentsPage, Set[AlternativeDocuments](HomeOfficeOrTravelDocument)).success.value

      val result = answers.set(DoYouHavePrimaryDocumentPage, true).success.value

      result.get(DoYouHaveTwoSecondaryDocumentsPage) must not be defined
      result.get(WhichAlternativeDocumentsPage) must not be defined
    }

  }
}
