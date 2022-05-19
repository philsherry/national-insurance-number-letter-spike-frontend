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
import models.AlternativeDocuments.{AdoptionCertificate, MarriageOrCivilPartnershipCertificate}
import models.PrimaryDocument.BirthCertificate
import pages._
import models._
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

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

        "to the what is your previous name page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouHaveAPreviousNamePage, true).success.value
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, answers) mustBe routes.WhatIsYourPreviousNameController.onPageLoad(Index(0), NormalMode)
        }

        "to the date of birth page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHaveAPreviousNamePage, false).success.value
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, answers) mustBe routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouHaveAPreviousNamePage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the previous name page to the do you have a previous name page (add to list)" in {
        navigator.nextPage(WhatIsYourPreviousNamePage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
      }

      "must go from the date of birth page to the what is your gender page" in {
        navigator.nextPage(WhatIsYourDateOfBirthPage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourGenderController.onPageLoad(NormalMode)
      }

      "must go from the what is your gender page to the telephone number page" in {
        navigator.nextPage(WhatIsYourGenderPage, NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourTelephoneNumberController.onPageLoad(NormalMode)
      }

      "must go from the telephone number page to the do you know your national insurance number page" in {
        navigator.nextPage(WhatIsYourTelephoneNumberPage, NormalMode, emptyUserAnswers) mustBe routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(NormalMode)
      }

      "must go from the do you know your national insurance number page" - {

        "to the national insurance page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, true).success.value
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, answers) mustBe routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(NormalMode)
        }

        "to the returning from living abroad page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouKnowYourNationalInsuranceNumberPage, false).success.value
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, answers) mustBe routes.AreYouReturningFromLivingAbroadController.onPageLoad(NormalMode)
        }

        "to the journey recovery controller when the user has no selection" in {
          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the national insurance number page to the returning from living abroad page" in {
        navigator.nextPage(WhatIsYourNationalInsuranceNumberPage, NormalMode, emptyUserAnswers) mustBe routes.AreYouReturningFromLivingAbroadController.onPageLoad(NormalMode)
      }

      "must go from the returning from living abroad page to the current address in UK page" in {
        navigator.nextPage(AreYouReturningFromLivingAbroadPage, NormalMode, emptyUserAnswers) mustBe routes.IsYourCurrentAddressInUkController.onPageLoad(NormalMode)
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

        "to the is your previous address in the uk page when the user selects yes" - {

          "at index 0 when there are no previous addresses" in {
            val answers = emptyUserAnswers.set(DoYouHaveAnyPreviousAddressesPage, true).success.value
            navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, answers) mustBe routes.IsYourPreviousAddressInUkController.onPageLoad(Index(0), NormalMode)
          }

          "at index 1 when there is already a previous address" in {
            val address = PreviousAddressUk(
              addressLine1 = "line 1",
              addressLine2 = None,
              addressLine3 = None,
              postcode = "postcode",
              from = LocalDate.now,
              to = LocalDate.now
            )
            val answers = emptyUserAnswers
              .set(DoYouHaveAnyPreviousAddressesPage, true).success.value
              .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
              .set(WhatIsYourPreviousAddressUkPage(Index(0)), address).success.value
            navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, answers) mustBe routes.IsYourPreviousAddressInUkController.onPageLoad(Index(1), NormalMode)
          }
        }

        "to the are you married page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHaveAnyPreviousAddressesPage, false).success.value
          navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, answers) mustBe routes.AreYouMarriedController.onPageLoad(NormalMode)
        }

        "to the journey recovery controller when the user has no selection" in {
          navigator.nextPage(DoYouHaveAnyPreviousAddressesPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the is your previous address in the uk" - {

        "to the what is your previous address uk page when the user selects yes" in {
          val answers = emptyUserAnswers.set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), NormalMode, answers) mustBe routes.WhatIsYourPreviousAddressUkController.onPageLoad(Index(0), NormalMode)
        }

        "to the what is your previous address international page when the user selects no" in {
          val answers = emptyUserAnswers.set(IsYourPreviousAddressInUkPage(Index(0)), false).success.value
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), NormalMode, answers) mustBe routes.WhatIsYourPreviousAddressInternationalController.onPageLoad(Index(0), NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the what is your previous address uk to do you have any previous address" in {
        navigator.nextPage(WhatIsYourPreviousAddressUkPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
      }

      "must go from the what is your previous address international to do you have any previous address" in {
        navigator.nextPage(WhatIsYourPreviousAddressInternationalPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
      }

      "must go from the are you sure you want to remove your previous address page to the do you have a previous address page" in {
        navigator.nextPage(AreYouSureYouWantToRemovePreviousAddressPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
      }

      "must go from the are you married page" - {

        "to the current relationship type page when the user selects yes" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, true).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.CurrentRelationshipTypeController.onPageLoad(NormalMode)
        }

        "to the have you previously been married details page when the user selects no" in {
          val answers = emptyUserAnswers.set(AreYouMarriedPage, false).success.value
          navigator.nextPage(AreYouMarriedPage, NormalMode, answers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(AreYouMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "must go from the current relationship type page to the when did you get married page" in {
        navigator.nextPage(CurrentRelationshipTypePage, NormalMode, emptyUserAnswers) mustBe routes.WhenDidYouGetMarriedController.onPageLoad(NormalMode)
      }

      "must go from the when did you get married page to the have you previously been married page" in {
        navigator.nextPage(WhenDidYouGetMarriedPage, NormalMode, emptyUserAnswers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
      }

      "must go from the previously married page" - {

        "to the previous relationship type page when the user selects yes" - {

          "when there are no previous relationships set" in {
            val answers = emptyUserAnswers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
            navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, answers) mustBe routes.PreviousRelationshipTypeController.onPageLoad(Index(0), NormalMode)
          }

          "when there are previous relationships set" in {
            val answers = emptyUserAnswers
              .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
              .set(PreviousRelationshipTypePage(Index(0)), PreviousRelationshipType.Marriage).success.value
              .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), PreviousMarriageOrPartnershipDetails(LocalDate.now, LocalDate.now, "nunya")).success.value
            navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, answers) mustBe routes.PreviousRelationshipTypeController.onPageLoad(Index(1), NormalMode)
          }
        }

        "to have you ever claimed child benefit page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, false).success.value
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, answers) mustBe routes.HaveYouEverClaimedChildBenefitController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the previous relationship type page to the previous relationship details page" in {
        navigator.nextPage(PreviousRelationshipTypePage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(0), NormalMode)
      }

      "go from the when previous marriage or civil partnership details page to the do you want to add another previous relationship page" in {
        navigator.nextPage(PreviousMarriageOrPartnershipDetailsPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
      }

      "go from the are you sure you want to remove this previous relationship page to the do you have a previous relationship page" in {
        navigator.nextPage(AreYouSureYouWantToRemovePreviousRelationshipPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
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

        "to the employment history page when the user selects yes" in {
          val answers = emptyUserAnswers.set(HaveYouEverWorkedInUkPage, true).success.value
          navigator.nextPage(HaveYouEverWorkedInUkPage, NormalMode, answers) mustBe routes.EmploymentHistoryController.onPageLoad(NormalMode)
        }

        "to the do you have a primary document page when the user selects no" in {
          val answers = emptyUserAnswers.set(HaveYouEverWorkedInUkPage, false).success.value
          navigator.nextPage(HaveYouEverWorkedInUkPage, NormalMode, answers) mustBe routes.DoYouHavePrimaryDocumentController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(HaveYouEverWorkedInUkPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the are you still employed page" - {

        "to the employment history page when the user selects yes" in {
          val answers = emptyUserAnswers.set(AreYouStillEmployedPage(Index(0)), true).success.value
          navigator.nextPage(AreYouStillEmployedPage(Index(0)), NormalMode, answers) mustBe routes.EmploymentHistoryController.onPageLoad(NormalMode)
        }

        "to the when did your employment end page when the user selects no" in {
          val answers = emptyUserAnswers.set(AreYouStillEmployedPage(Index(0)), false).success.value
          navigator.nextPage(AreYouStillEmployedPage(Index(0)), NormalMode, answers) mustBe routes.WhenDidYouStopWorkingForEmployerController.onPageLoad(Index(0), NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(AreYouStillEmployedPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the employment history page" - {

        "to the what is your employers name page when the user selects yes" - {

          "at index 0 when there are no previous addresses" in {
            val answers = emptyUserAnswers.set(EmploymentHistoryPage, true).success.value
            navigator.nextPage(EmploymentHistoryPage, NormalMode, answers) mustBe routes.WhatIsYourEmployersNameController.onPageLoad(Index(0), NormalMode)
          }

          "at index 1 when there is a previous address" in {
            val address = EmployersAddress(
              addressLine1 = "line 1", addressLine2 = Some("line 2"), addressLine3 = None, postcode = "postcode"
            )
            val answers = emptyUserAnswers
              .set(EmploymentHistoryPage, true).success.value
              .set(WhatIsYourEmployersNamePage(Index(0)), "foobar").success.value
              .set(WhatIsYourEmployersAddressPage(Index(0)), address).success.value
              .set(WhenDidYouStartWorkingForEmployerPage(Index(0)), LocalDate.now).success.value
              .set(WhenDidYouStopWorkingForEmployerPage(Index(0)), LocalDate.now).success.value
            navigator.nextPage(EmploymentHistoryPage, NormalMode, answers) mustBe routes.WhatIsYourEmployersNameController.onPageLoad(Index(1), NormalMode)
          }
        }

        "to the do you have a primary document page when the user selects no" in {
          val answers = emptyUserAnswers.set(EmploymentHistoryPage, false).success.value
          navigator.nextPage(EmploymentHistoryPage, NormalMode, answers) mustBe routes.DoYouHavePrimaryDocumentController.onPageLoad(NormalMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(EmploymentHistoryPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the what is your employers name page to the what is your employers address page" in {
        navigator.nextPage(WhatIsYourEmployersNamePage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.WhatIsYourEmployersAddressController.onPageLoad(Index(0), NormalMode)
      }

      "go from the what is your employers address page to the when did you start working for employer page" in {
        navigator.nextPage(WhatIsYourEmployersAddressPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.WhenDidYouStartWorkingForEmployerController.onPageLoad(Index(0), NormalMode)
      }

      "go from when did you start working for your employer page to the are you still employed page" in {
        navigator.nextPage(WhenDidYouStartWorkingForEmployerPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.AreYouStillEmployedController.onPageLoad(Index(0), NormalMode)
      }

      "go from when did you stop working for your employer page to the employment history page" in {
        navigator.nextPage(WhenDidYouStopWorkingForEmployerPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.EmploymentHistoryController.onPageLoad(NormalMode)
      }

      "go from are you sure you want to remove your employer page to the employment history page" in {
        navigator.nextPage(AreYouSureYouWantToRemovePreviousEmployerPage(Index(0)), NormalMode, emptyUserAnswers) mustBe routes.EmploymentHistoryController.onPageLoad(NormalMode)
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

        "to the which two alternative documents page when the user selects yes" in {
          val answers = emptyUserAnswers.set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, answers) mustBe routes.WhichAlternativeDocumentsController.onPageLoad(NormalMode)
        }

        "to check your answers page when the user selects no" in {
          val answers = emptyUserAnswers.set(DoYouHaveTwoSecondaryDocumentsPage, false).success.value
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, NormalMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the which two secondary documents page to the check your answers page" in {
        navigator.nextPage(WhichAlternativeDocumentsPage, NormalMode, emptyUserAnswers) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }

    "in Check mode" - {

      "go from the are you married page" - {

        "to the check your answers page if yes and relationship type is set" in {
          val answers = emptyUserAnswers
            .set(AreYouMarriedPage, true).success.value
            .set(CurrentRelationshipTypePage, CurrentRelationshipType.Marriage).success.value

          navigator.nextPage(AreYouMarriedPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if no" in {
          val answers = emptyUserAnswers
            .set(AreYouMarriedPage, false).get

          navigator.nextPage(AreYouMarriedPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the current relationship type page if the current relationship type is not set" in {
          val answers = emptyUserAnswers
            .set(AreYouMarriedPage, true).get
          navigator.nextPage(AreYouMarriedPage, CheckMode, answers) mustBe routes.CurrentRelationshipTypeController.onPageLoad(CheckMode)
        }
      }

      "go from the current relationship type page" - {

        "to the check your answers page if the current relationship date is set" in {
          val answers = emptyUserAnswers
            .set(WhenDidYouGetMarriedPage, LocalDate.now).success.value
          navigator.nextPage(CurrentRelationshipTypePage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the current relationship date page if the current relationship date is not set" in {
          navigator.nextPage(CurrentRelationshipTypePage, CheckMode, emptyUserAnswers) mustBe routes.WhenDidYouGetMarriedController.onPageLoad(CheckMode)
        }
      }

      "go from the previous marriage or civil partnership page" - {

        "to the previous relationship type page" - {

          "at index 0 if there are no previous relationships" in {
            val answers = emptyUserAnswers
              .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
            navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, CheckMode, answers) mustBe routes.PreviousRelationshipTypeController.onPageLoad(Index(0), CheckMode)
          }

          "at the next available index if there are previous relationships" in {
            val previous = PreviousMarriageOrPartnershipDetails(LocalDate.of(2000,1,1), LocalDate.of(2001, 1, 1), "reason")
            val answers = emptyUserAnswers
              .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, true).success.value
              .set(PreviousRelationshipTypePage(Index(0)), PreviousRelationshipType.Marriage).success.value
              .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), previous).success.value
            navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, CheckMode, answers) mustBe routes.PreviousRelationshipTypeController.onPageLoad(Index(1), CheckMode)
          }
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, false).success.value
          navigator.nextPage(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }
      }

      "go from the previous relationship type page" - {

        "to the previous relationship details page when the details aren't already set" in {
          navigator.nextPage(PreviousRelationshipTypePage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(0), CheckMode)
        }

        "to the check your answers page when the details are already set" in {
          val previous = PreviousMarriageOrPartnershipDetails(LocalDate.of(2000,1,1), LocalDate.of(2001, 1, 1), "reason")
          val answers = emptyUserAnswers
            .set(PreviousMarriageOrPartnershipDetailsPage(Index(0)), previous).success.value
          navigator.nextPage(PreviousRelationshipTypePage(Index(0)), CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }
      }

      "go from the have you ever claimed child benefit page" - {

        "to the check your answers page if answer is yes and child benefit number is set" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverClaimedChildBenefitPage, true).get
            .set(DoYouKnowYourChildBenefitNumberPage, true).get
            .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get

          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverClaimedChildBenefitPage, false).get

          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the do you know your child benefit number page if answer is yes and do you know your child benefit number is not set" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverClaimedChildBenefitPage, true).get

          navigator.nextPage(HaveYouEverClaimedChildBenefitPage, CheckMode, answers) mustBe routes.DoYouKnowYourChildBenefitNumberController.onPageLoad(CheckMode)
        }

      }

      "go from the do you know your child benefit number page" - {

        "to the check your answers page if answer is yes and child benefit number is set" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourChildBenefitNumberPage, true).get
            .set(WhatIsYourChildBenefitNumberPage, "CHB12345678").get

          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourChildBenefitNumberPage, false).get

          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the what is your child benefit number page if answer is yes and child benefit number is not set" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourChildBenefitNumberPage, true).get

          navigator.nextPage(DoYouKnowYourChildBenefitNumberPage, CheckMode, answers) mustBe routes.WhatIsYourChildBenefitNumberController.onPageLoad(CheckMode)
        }

      }

      "go from the do you have previous name page" - {

        "to the check your answers page if answer is yes and previous name is set" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveAPreviousNamePage, true).get
            .set(WhatIsYourPreviousNamePage(Index(0)), WhatIsYourPreviousName("first", None, "last")).get

          navigator.nextPage(DoYouHaveAPreviousNamePage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveAPreviousNamePage, false).get

          navigator.nextPage(DoYouHaveAPreviousNamePage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the what is your previous name page if answer is yes and previous name is not set" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveAPreviousNamePage, true).get

          navigator.nextPage(DoYouHaveAPreviousNamePage, CheckMode, answers) mustBe routes.WhatIsYourPreviousNameController.onPageLoad(Index(0), CheckMode)
        }

      }

      "go from the is your previous address in uk page" - {

        "to the check your answers page if answer is yes and uk address is set" in {
          val answers = emptyUserAnswers
            .set(IsYourCurrentAddressInUkPage, true).get
            .set(WhatIsYourCurrentAddressUkPage, CurrentAddressUk("line 1", None, None, "AA1 1AA")).get

          navigator.nextPage(IsYourCurrentAddressInUkPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no and international address is set" in {
          val answers = emptyUserAnswers
            .set(IsYourCurrentAddressInUkPage, false).get
            .set(WhatIsYourCurrentAddressInternationalPage, CurrentAddressInternational("line 1", None, None, None, Country("FR", "France"))).get

          navigator.nextPage(IsYourCurrentAddressInUkPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the uk address page if answer is yes and uk address is not set" in {
          val answers = emptyUserAnswers
            .set(IsYourCurrentAddressInUkPage, true).get

          navigator.nextPage(IsYourCurrentAddressInUkPage, CheckMode, answers) mustBe routes.WhatIsYourCurrentAddressUkController.onPageLoad(CheckMode)
        }

        "to the international address page if answer is no and international address is not set" in {
          val answers = emptyUserAnswers
            .set(IsYourCurrentAddressInUkPage, false).get

          navigator.nextPage(IsYourCurrentAddressInUkPage, CheckMode, answers) mustBe routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(CheckMode)
        }

      }

      "go from the do you have know your national insurance number page" - {

        "to the check your answers page if answer is yes and national insurance number is set" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourNationalInsuranceNumberPage, true).get
            .set(WhatIsYourNationalInsuranceNumberPage, Nino("AA123456A")).get

          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourNationalInsuranceNumberPage, false).get

          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the what is your national insurance number page if answer is no and national insurance number is not set" in {
          val answers = emptyUserAnswers
            .set(DoYouKnowYourNationalInsuranceNumberPage, true).get

          navigator.nextPage(DoYouKnowYourNationalInsuranceNumberPage, CheckMode, answers) mustBe routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(CheckMode)
        }

      }

      "go from the have you ever claimed any other uk benefits page" - {

        "to the check your answers page if answer is yes and other uk benefits is set" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverReceivedOtherUkBenefitsPage, true).get
            .set(WhatOtherUkBenefitsHaveYouReceivedPage, "some other benefits").get

          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page if answer is no" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverReceivedOtherUkBenefitsPage, false).get

          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the what other uk benefits page if answer is no and what other uk benefits is not set" in {
          val answers = emptyUserAnswers
            .set(HaveYouEverReceivedOtherUkBenefitsPage, true).get

          navigator.nextPage(HaveYouEverReceivedOtherUkBenefitsPage, CheckMode, answers) mustBe routes.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(CheckMode)
        }

      }

      "go from the do you have primary document page" - {

        "to the check your answers page when the user selects yes and which primary document is set" in {
          val answers = emptyUserAnswers
            .set(DoYouHavePrimaryDocumentPage, true).success.value
            .set(WhichPrimaryDocumentPage, BirthCertificate).success.value
          navigator.nextPage(DoYouHavePrimaryDocumentPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the which primary documents page when the user selects yes and which primary document is not set" in {
          val answers = emptyUserAnswers
            .set(DoYouHavePrimaryDocumentPage, true).success.value
          navigator.nextPage(DoYouHavePrimaryDocumentPage, CheckMode, answers) mustBe routes.WhichPrimaryDocumentController.onPageLoad(CheckMode)
        }

        "to the do you have two secondary documents page when the user selects no" in {
          val answers = emptyUserAnswers
            .set(DoYouHavePrimaryDocumentPage, false).success.value
          navigator.nextPage(DoYouHavePrimaryDocumentPage, CheckMode, answers) mustBe routes.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(CheckMode)
        }

      }

      "go from the is your previous address in the uk" - {

        "to the what is your previous address uk page when the user selects yes" in {
          val answers = emptyUserAnswers
            .set(IsYourPreviousAddressInUkPage(Index(0)), true).success.value
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), CheckMode, answers) mustBe routes.WhatIsYourPreviousAddressUkController.onPageLoad(Index(0), CheckMode)
        }

        "to the what is your previous address international page when the user selects no" in {
          val answers = emptyUserAnswers
            .set(IsYourPreviousAddressInUkPage(Index(0)), false).success.value
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), CheckMode, answers) mustBe routes.WhatIsYourPreviousAddressInternationalController.onPageLoad(Index(0), CheckMode)
        }

        "to the journey recovery page when the user has no selection" in {
          navigator.nextPage(IsYourPreviousAddressInUkPage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.JourneyRecoveryController.onPageLoad()
        }
      }

      "go from the what is your employers name page to the what is your employers address page" in {
        navigator.nextPage(WhatIsYourEmployersNamePage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.WhatIsYourEmployersAddressController.onPageLoad(Index(0), CheckMode)
      }

      "go from the what is your employers address page to the when did you start working for employer page" in {
        navigator.nextPage(WhatIsYourEmployersAddressPage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.WhenDidYouStartWorkingForEmployerController.onPageLoad(Index(0), CheckMode)
      }

      "go from when did you start working for your employer page to the are you still employed page" in {
        navigator.nextPage(WhenDidYouStartWorkingForEmployerPage(Index(0)), CheckMode, emptyUserAnswers) mustBe routes.AreYouStillEmployedController.onPageLoad(Index(0), CheckMode)
      }

      "go from the are you still employed page" - {

        "to the when did you stop working page when answered yes" in {
          val answers = emptyUserAnswers
            .set(AreYouStillEmployedPage(Index(0)), false).success.value

          navigator.nextPage(AreYouStillEmployedPage(Index(0)), CheckMode, answers) mustBe routes.WhenDidYouStopWorkingForEmployerController.onPageLoad(Index(0), CheckMode)
        }

        "to the check your answers page when answered no" in {
          val answers = emptyUserAnswers
            .set(AreYouStillEmployedPage(Index(0)), true).success.value

          navigator.nextPage(AreYouStillEmployedPage(Index(0)), CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

      }

      "go from the do you have alternative documents page" - {

        "to the which alternative documents page when true and documents not set" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value

          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, CheckMode, answers) mustBe routes.WhichAlternativeDocumentsController.onPageLoad(CheckMode)
        }

        "to the check your answers page when true and documents are set" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveTwoSecondaryDocumentsPage, true).success.value
            .set(WhichAlternativeDocumentsPage, Set[AlternativeDocuments](MarriageOrCivilPartnershipCertificate, AdoptionCertificate)).success.value

          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

        "to the check your answers page when false" in {
          val answers = emptyUserAnswers
            .set(DoYouHaveTwoSecondaryDocumentsPage, false).success.value

          navigator.nextPage(DoYouHaveTwoSecondaryDocumentsPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
        }

      }

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
