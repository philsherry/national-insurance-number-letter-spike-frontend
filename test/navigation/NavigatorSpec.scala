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

      "must go from the telephone number page to the do you know your national insurance number page" in {
        navigator.nextPage(WhatIsYourTelephoneNumberPage, NormalMode, emptyUserAnswers) mustBe routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(NormalMode)
      }

      "must go from the do you know your national insurance number page" - {

        "to the national insurance page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, answers) mustBe routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(NormalMode)
        }

        "to the are you married page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, false).success.value
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, answers) mustBe routes.AreYouMarriedController.onPageLoad(NormalMode)
        }

        "to the journey recovery controller when the user has no selection" in {
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the national insurance number page to the are you married page" in {
        navigator.nextPage(WhatIsYourNationalInsuranceNumberPage, NormalMode, emptyUserAnswers) mustBe routes.AreYouMarriedController.onPageLoad(NormalMode)
      }

      "must go from the are you married page" - {

        "to the have you previously been married page when the user selects yes" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, true).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.WhenDidYouGetMarriedController.onPageLoad(NormalMode)
        }

        "to the are you in a civil partnership page when the user selects no" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, false).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.AreYouInACivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(AreYouMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the when did you get married page to the have you previously been married page" in {
        navigator.nextPage(WhenDidYouGetMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
      }

      "must go from the are you in a civil partnership page" - {

        "to the when did you enter a civil partnership page when the user selects yes" in {
          val answers = emptyUserAnswers.set(AreYouInACivilPartnershipPage, true).success.value
          navigator.nextPage(AreYouInACivilPartnershipPage, NormalMode, answers) mustBe routes.WhenDidYouEnterACivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the have you previously been in a marriage page when the user selects no" in {
          val answers = emptyUserAnswers.set(AreYouInACivilPartnershipPage, false).success.value
          navigator.nextPage(AreYouInACivilPartnershipPage, NormalMode, answers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(AreYouMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the when did you enter a civil partnership page to the have you previously been married page" in {
        navigator.nextPage(WhenDidYouEnterACivilPartnershipPage, NormalMode, emptyUserAnswers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
      }

      "must go from the previously married page" - {

        "to the when previous marriage or civil partnership details page when the user selects yes" in {
          val answers = emptyUserAnswers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, answers) mustBe routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(NormalMode)
        }

        "to have you ever claimed child benefit page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, false).success.value
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, answers) mustBe routes.HaveYouEverClaimedChildBenefitController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the have you claimed child benefit page" - {

        "to the do you know your child benefit number page when the user selects yes" in {
          val answers = emptyUserAnswers.set(HaveYouEverClaimedChildBenefitPage, true).success.value
          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, NormalMode, answers) mustBe routes.DoYouKnowYourChildBenefitNumberController.onPageLoad(NormalMode)
        }

        "to the have you claimed other benefits page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouEverClaimedChildBenefitPage, false).success.value
          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, NormalMode, answers) mustBe routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the do you know your child benefit number page" - {

        "to the what is your child benefit number page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourChildBenefitNumberPage, true).success.value
          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, NormalMode, answers) mustBe routes.WhatIsYourChildBenefitNumberController.onPageLoad(NormalMode)
        }

        "to the have you claimed other benefits page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourChildBenefitNumberPage, false).success.value
          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, NormalMode, answers) mustBe routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the what is your child benefit number page to the have you claimed other benefits page when the user selects no" in {
        navigator.nextPage(WhatIsYourChildBenefitNumberPage, NormalMode, emptyUserAnswers) mustBe routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
      }

      "go from the have you claimed other benefits page" - {

        "to the what other benefits have you claimed page when the user selects yes" in {
          val answers = emptyUserAnswers.set(HaveYouEverReceivedOtherUkBenefitsPage, true).success.value
          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, NormalMode, answers) mustBe routes.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(NormalMode)
        }

        "to the have you ever worked in the uk page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouEverReceivedOtherUkBenefitsPage, false).success.value
          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, NormalMode, answers) mustBe routes.HaveYouEverWorkedInUkController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the what other benefits page to the have you worked in the uk page" in {
        navigator.nextPage(WhatOtherUkBenefitsHaveYouReceivedPage, NormalMode, emptyUserAnswers) mustBe routes.HaveYouEverWorkedInUkController.onPageLoad(NormalMode)
      }

      "go from the have you worked in the uk page" - {

        "to the employer details page when the user selects yes" ignore {
          // TODO
        }

        "to the do you have a primary document page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouEverWorkedInUkPage, false).success.value
          navigator.nextPage(HaveYouEverWorkedInUkPage, NormalMode, answers) mustBe routes.DoYouHavePrimaryDocumentController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouEverWorkedInUkPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the do you have a primary document page" - {

        "to the which primary document page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouHavePrimaryDocumentPage, true).success.value
          navigator.nextPage(DoYouHavePrimaryDocumentPage, NormalMode, answers) mustBe routes.WhichPrimaryDocumentController.onPageLoad(NormalMode)
        }

        "to the do you have two secondary documents page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHavePrimaryDocumentPage, false).success.value
          navigator.nextPage(DoYouHavePrimaryDocumentPage, NormalMode, answers) mustBe routes.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouHavePrimaryDocumentPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the which primary document page to the check your answers page" in {
        navigator.nextPage(WhichPrimaryDocumentPage, NormalMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "go from the do you have two secondary documents page" - {

        "to the which two secondary documents page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, answers) mustBe routes.WhichSecondaryDocumentsController.onPageLoad(NormalMode)
        }

        "to ... when the user selects no" ignore {
          val answers = emptyUserAnswers.set(DoYouHaveTwoSecondaryDocumentsPage, false).success.value
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, answers) mustBe ???
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the which two secondary documents page to the check your answers page" in {
        navigator.nextPage(WhichSecondaryDocumentsPage, NormalMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
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
