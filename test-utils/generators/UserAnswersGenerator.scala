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

package generators

import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersGenerator extends TryValues {
  self: Generators =>

  val generators: Seq[Gen[(QuestionPage[_], JsValue)]] =
    arbitrary[(AreYouSureYouWantToRemovePreviousRelationshipPage, JsValue)] ::
    arbitrary[(AreYouSureYouWantToRemovePreviousNamePage, JsValue)] ::
    arbitrary[(AreYouSureYouWantToRemovePreviousEmployerPage, JsValue)] ::
    arbitrary[(AreYouSureYouWantToRemovePreviousAddressPage, JsValue)] ::
    arbitrary[(WhichAlternativeDocumentsPage.type, JsValue)] ::
    arbitrary[(AreYouStillEmployedPage, JsValue)] ::
    arbitrary[(DoYouKnowYourNationalInsuranceNumberPage.type, JsValue)] ::
    arbitrary[(WhichPrimaryDocumentPage.type, JsValue)] ::
    arbitrary[(DoYouHaveTwoSecondaryDocumentsPage.type, JsValue)] ::
    arbitrary[(DoYouHavePrimaryDocumentPage.type, JsValue)] ::
    arbitrary[(WhenDidYouStopWorkingForEmployerPage, JsValue)] ::
    arbitrary[(WhenDidYouStartWorkingForEmployerPage, JsValue)] ::
    arbitrary[(WhatIsYourEmployersNamePage, JsValue)] ::
    arbitrary[(WhatIsYourEmployersAddressPage, JsValue)] ::
    arbitrary[(HaveYouEverWorkedInUkPage.type, JsValue)] ::
    arbitrary[(EmploymentHistoryPage.type, JsValue)] ::
    arbitrary[(WhatOtherUkBenefitsHaveYouReceivedPage.type, JsValue)] ::
    arbitrary[(HaveYouEverReceivedOtherUkBenefitsPage.type, JsValue)] ::
    arbitrary[(WhatIsYourChildBenefitNumberPage.type, JsValue)] ::
    arbitrary[(HaveYouEverClaimedChildBenefitPage.type, JsValue)] ::
    arbitrary[(DoYouKnowYourChildBenefitNumberPage.type, JsValue)] ::
    arbitrary[(PreviousMarriageOrPartnershipDetailsPage, JsValue)] ::
    arbitrary[(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage.type, JsValue)] ::
    arbitrary[(WhenDidYouGetMarriedPage.type, JsValue)] ::
    arbitrary[(AreYouMarriedPage.type, JsValue)] ::
    arbitrary[(WhatIsYourTelephoneNumberPage.type, JsValue)] ::
    arbitrary[(WhatIsYourNationalInsuranceNumberPage.type, JsValue)] ::
    arbitrary[(AreYouReturningFromLivingAbroadPage.type, JsValue)] ::
    arbitrary[(WhatIsYourPreviousAddressUkPage, JsValue)] ::
    arbitrary[(WhatIsYourPreviousAddressInternationalPage, JsValue)] ::
    arbitrary[(IsYourPreviousAddressInUkPage, JsValue)] ::
    arbitrary[(DoYouHaveAnyPreviousAddressesPage.type, JsValue)] ::
    arbitrary[(WhatIsYourCurrentAddressUkPage.type, JsValue)] ::
    arbitrary[(WhatIsYourCurrentAddressInternationalPage.type, JsValue)] ::
    arbitrary[(IsYourCurrentAddressInUkPage.type, JsValue)] ::
    arbitrary[(WhatIsYourDateOfBirthPage.type, JsValue)] ::
    arbitrary[(WhatIsYourPreviousNamePage, JsValue)] ::
    arbitrary[(DoYouHaveAPreviousNamePage.type, JsValue)] ::
    arbitrary[(WhatIsYourNamePage.type, JsValue)] ::
    Nil

  implicit lazy val arbitraryUserData: Arbitrary[UserAnswers] = {

    import models._

    Arbitrary {
      for {
        id      <- nonEmptyString
        data    <- generators match {
          case Nil => Gen.const(Map[QuestionPage[_], JsValue]())
          case _   => Gen.mapOf(oneOf(generators))
        }
      } yield UserAnswers (
        id = id,
        data = data.foldLeft(Json.obj()) {
          case (obj, (path, value)) =>
            obj.setObject(path.path, value).get
        }
      )
    }
  }
}
