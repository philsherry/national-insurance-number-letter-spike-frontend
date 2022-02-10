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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {
        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, emptyUserAnswers) mustBe routes.IndexController.onPageLoad
      }

      "must go from the what is your name page to the do you have a previous name page" in {
        navigator.nextPage(WhatIsYourNamePage, NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
      }

      "must go from the do you have a previous name page" - {

        "to the previous name page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouHaveAPreviousNamePage, true).success.value
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, answers) mustBe routes.WhatIsYourPreviousNameController.onPageLoad(NormalMode)
        }

        "to the date of birth page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHaveAPreviousNamePage, false).success.value
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, answers) mustBe routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the previous name page to the date of birth page" in {
        navigator.nextPage(WhatIsYourPreviousNamePage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
      }

      "must go from the date of birth page to the is current address in the uk page" in {
        navigator.nextPage(WhatIsYourDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.IsYourCurrentAddressInUkController.onPageLoad(NormalMode)
      }

      "must go from the is current address in the uk page" - {

        "to the current address uk page when the user selects yes" in {
          val answers = emptyUserAnswers.set(IsYourCurrentAddressInUkPage, true).success.value
          navigator.nextPage(IsYourCurrentAddressInUkPage, NormalMode, answers) mustBe routes.WhatIsYourCurrentAddressUkController.onPageLoad(NormalMode)
        }

        "to the current address international page when the user selects no" in {
          val answers = emptyUserAnswers.set(IsYourCurrentAddressInUkPage, false).success.value
          navigator.nextPage(IsYourCurrentAddressInUkPage, NormalMode, answers) mustBe routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(IsYourCurrentAddressInUkPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the current address uk page to the do you have any previous addresses page" in {
        navigator.nextPage(WhatIsYourCurrentAddressUkPage, NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
      }

      "must go from the current address international page to the do you have any previous addresses page" in {
        navigator.nextPage(WhatIsYourCurrentAddressInternationalPage, NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
      }

      "must go from the do you have any previous addresses page" - {

        "to ... when the user selects yes" ignore {
          // TODO
          val answers = emptyUserAnswers.set(DoYouHaveAnyPreviousAddressesPage, true).success.value
          navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, answers) mustBe ???
        }

        "to the returning from living abroad page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHaveAnyPreviousAddressesPage, false).success.value
          navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, answers) mustBe routes.AreYouReturningFromLivingAbroadController.onPageLoad(NormalMode)
        }

        "to the journey recovery controller when the user has no selection" in {
          navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the returning from living abroad page to the telephone number page" in {
        navigator.nextPage(AreYouReturningFromLivingAbroadPage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourTelephoneNumberController.onPageLoad(NormalMode)
      }

      "must go from the telephone number page to the do you know your national insurance number page" ignore {
        navigator.nextPage(WhatIsYourTelephoneNumberPage, NormalMode, emptyUserAnswers) mustBe ???
      }

      "must go from the do you know your national insurance number page" ignore {

        "to the national insurance page when the user selects yes" in {
          // TODO
        }

        "to the are you married page when the user selects no" in {
          // TODO
        }

        "to the journey recovery controller when the user has no selection" in {
          // TODO
        }
      }

      "must go from the national insurance number page to the are you married page" in {
        navigator.nextPage(WhatIsYourNationalInsuranceNumberPage, NormalMode, emptyUserAnswers) mustBe routes.AreYouMarriedController.onPageLoad(NormalMode)
      }

      "must go from the are you married page" - {

        "to the have you previously been married page when the user selects yes" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, true).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the are you in a civil partnership page when the user selects no" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, false).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.AreYouInACivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(AreYouMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
