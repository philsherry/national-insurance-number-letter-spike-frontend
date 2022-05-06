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

import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case WhatIsYourNamePage                                     => _ => routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
    case DoYouHaveAPreviousNamePage                             => doYouHaveAPreviousNameRoutes
    case WhatIsYourPreviousNamePage(_)                          => _ => routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
    case AreYouSureYouWantToRemovePreviousNamePage(_)           => _ => routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
    case WhatIsYourDateOfBirthPage                              => _ => routes.IsYourCurrentAddressInUkController.onPageLoad(NormalMode)
    case IsYourCurrentAddressInUkPage                           => isYourCurrentAddressInUkRoutes
    case WhatIsYourCurrentAddressUkPage                         => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case WhatIsYourCurrentAddressInternationalPage              => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case DoYouHaveAnyPreviousAddressesPage                      => doYouHaveAnyPreviousAddressesRoutes
    case IsYourPreviousAddressInUkPage(index)                   => isYourPreviousAddressInUkRoutes(_, index, NormalMode)
    case WhatIsYourPreviousAddressUkPage(_)                     => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case WhatIsYourPreviousAddressInternationalPage(_)          => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case AreYouSureYouWantToRemovePreviousAddressPage(_)        => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case AreYouReturningFromLivingAbroadPage                    => _ => routes.WhatIsYourTelephoneNumberController.onPageLoad(NormalMode)
    case WhatIsYourTelephoneNumberPage                          => _ => routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(NormalMode)
    case DoYouKnowYourNationalInsuranceNumberPage               => doYouKnowYourNationalInsuranceNumberRoutes
    case WhatIsYourNationalInsuranceNumberPage                  => _ => routes.AreYouMarriedController.onPageLoad(NormalMode)
    case AreYouMarriedPage                                      => areYouMarriedRoutes
    case WhenDidYouGetMarriedPage                               => _ => routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
    case HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage => haveYouPreviouslyBeenInAMarriageOrCivilPartnershipRoutes
    case PreviousMarriageOrPartnershipDetailsPage               => _ => routes.HaveYouEverClaimedChildBenefitController.onPageLoad(NormalMode)
    case HaveYouEverClaimedChildBenefitPage                     => haveYouEverClaimedChildBenefitRoutes
    case DoYouKnowYourChildBenefitNumberPage                    => doYouKnowYourChildBenefitNumberRoutes
    case WhatIsYourChildBenefitNumberPage                       => _ => routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
    case HaveYouEverReceivedOtherUkBenefitsPage                 => haveYouEverReceivedOtherUkBenefitsRoutes
    case WhatOtherUkBenefitsHaveYouReceivedPage                 => _ => routes.HaveYouEverWorkedInUkController.onPageLoad(NormalMode)
    case HaveYouEverWorkedInUkPage                              => haveYouEverWorkedInUkRoutes
    case AreYouStillEmployedPage(index)                         => areYouStillEmployedRoutes(index)
    case WhenDidYouFinishYourEmploymentPage                     => _ => routes.EmploymentHistoryController.onPageLoad(NormalMode)
    case DoYouHaveAnyPreviousEmployersPage                      => doYouHaveAnyPreviousEmployersRoutes
    case WhatIsYourPreviousEmployersNamePage(index)             => _ => routes.WhatIsYourPreviousEmployersAddressController.onPageLoad(index, NormalMode)
    case WhatIsYourPreviousEmployersAddressPage(index)          => _ => routes.WhenDidYouStartWorkingForPreviousEmployerController.onPageLoad(index, NormalMode)
    case WhenDidYouStartWorkingForPreviousEmployerPage(index)   => _ => routes.AreYouStillEmployedController.onPageLoad(index, NormalMode)
    case WhenDidYouStopWorkingForPreviousEmployerPage(_)        => _ => routes.EmploymentHistoryController.onPageLoad(NormalMode)
    case AreYouSureYouWantToRemovePreviousEmployerPage(_)       => _ => routes.EmploymentHistoryController.onPageLoad(NormalMode)
    case DoYouHavePrimaryDocumentPage                           => doYouHaveAPrimaryDocumentRoutes
    case WhichPrimaryDocumentPage                               => _ => routes.CheckYourAnswersController.onPageLoad
    case DoYouHaveTwoSecondaryDocumentsPage                     => doYouHaveTwoSecondaryDocumentsRoutes
    case WhichAlternativeDocumentsPage                          => _ => routes.CheckYourAnswersController.onPageLoad
    case _ => _ => routes.IndexController.onPageLoad
  }

  private def doYouHaveAPreviousNameRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveAPreviousNamePage).map {
      case true  =>
        val previousNames = answers.get(PreviousNamesQuery).getOrElse(Seq.empty)
        routes.WhatIsYourPreviousNameController.onPageLoad(Index(previousNames.length), NormalMode)
      case false => routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def isYourCurrentAddressInUkRoutes(answers: UserAnswers): Call =
    answers.get(IsYourCurrentAddressInUkPage).map {
      case true  => routes.WhatIsYourCurrentAddressUkController.onPageLoad(NormalMode)
      case false => routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouHaveAnyPreviousAddressesRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveAnyPreviousAddressesPage).map {
      case true  =>
        val previousAddresses = answers.get(PreviousAddressesQuery).getOrElse(Seq.empty)
        routes.IsYourPreviousAddressInUkController.onPageLoad(Index(previousAddresses.length), NormalMode)
      case false => routes.AreYouReturningFromLivingAbroadController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def isYourPreviousAddressInUkRoutes(answers: UserAnswers, index: Index, mode: Mode): Call =
    answers.get(IsYourPreviousAddressInUkPage(index)).map {
      case true  => routes.WhatIsYourPreviousAddressUkController.onPageLoad(index, mode)
      case false => routes.WhatIsYourPreviousAddressInternationalController.onPageLoad(index, mode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouKnowYourNationalInsuranceNumberRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourNationalInsuranceNumberPage).map {
      case true  => routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(NormalMode)
      case false => routes.AreYouMarriedController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def areYouMarriedRoutes(answers: UserAnswers): Call =
    answers.get(AreYouMarriedPage).map {
      case true  => routes.WhenDidYouGetMarriedController.onPageLoad(NormalMode)
      case false => routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def haveYouPreviouslyBeenInAMarriageOrCivilPartnershipRoutes(answers: UserAnswers): Call =
    answers.get(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage).map {
      case true  => routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(NormalMode)
      case false => routes.HaveYouEverClaimedChildBenefitController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def haveYouEverClaimedChildBenefitRoutes(answers: UserAnswers): Call =
    answers.get(HaveYouEverClaimedChildBenefitPage).map {
      case true  => routes.DoYouKnowYourChildBenefitNumberController.onPageLoad(NormalMode)
      case false => routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouKnowYourChildBenefitNumberRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourChildBenefitNumberPage).map {
      case true  => routes.WhatIsYourChildBenefitNumberController.onPageLoad(NormalMode)
      case false => routes.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def haveYouEverReceivedOtherUkBenefitsRoutes(answers: UserAnswers): Call =
    answers.get(HaveYouEverReceivedOtherUkBenefitsPage).map {
      case true  => routes.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(NormalMode)
      case false => routes.HaveYouEverWorkedInUkController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def haveYouEverWorkedInUkRoutes(answers: UserAnswers): Call =
    answers.get(HaveYouEverWorkedInUkPage).map {
      case true  => routes.EmploymentHistoryController.onPageLoad(NormalMode)
      case false => routes.DoYouHavePrimaryDocumentController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def areYouStillEmployedRoutes(index: Index)(answers: UserAnswers): Call =
    answers.get(AreYouStillEmployedPage(index)).map {
      case true  => routes.EmploymentHistoryController.onPageLoad(NormalMode)
      case false => routes.WhenDidYouStopWorkingForPreviousEmployerController.onPageLoad(index, NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouHaveAnyPreviousEmployersRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveAnyPreviousEmployersPage).map {
      case true  =>
        val previousEmployers = answers.get(PreviousEmployersQuery).getOrElse(List.empty)
        routes.WhatIsYourPreviousEmployersNameController.onPageLoad(Index(previousEmployers.length), NormalMode)
      case false => routes.DoYouHavePrimaryDocumentController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouHaveAPrimaryDocumentRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHavePrimaryDocumentPage).map {
      case true  => routes.WhichPrimaryDocumentController.onPageLoad(NormalMode)
      case false => routes.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouHaveTwoSecondaryDocumentsRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveTwoSecondaryDocumentsPage).map {
      case true  => routes.WhichAlternativeDocumentsController.onPageLoad(NormalMode)
      case false => routes.InsufficientDocumentsController.onPageLoad()
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private val checkRouteMap: Page => UserAnswers => Call = {
    case DoYouHaveAPreviousNamePage => doYouHaveAPreviousNameCheckRoutes
    case IsYourPreviousAddressInUkPage(index) => isYourPreviousAddressInUkRoutes(_, index, CheckMode)
    case AreYouMarriedPage => areYouMarriedCheckRoutes
    case HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage => haveYouPreviouslyBeenInAMarriageOrCivilPartnershipCheckRoutes
    case IsYourCurrentAddressInUkPage => isYourCurrentAddressInUkCheckRoutes
    case HaveYouEverClaimedChildBenefitPage => haveYouEverClaimedChildBenefitCheckRoutes
    case DoYouKnowYourChildBenefitNumberPage => doYouKnowYourChildBenefitNumberCheckRoutes
    case HaveYouEverReceivedOtherUkBenefitsPage => haveYouEverReceivedOtherUkBenefitsCheckRoutes
    case DoYouKnowYourNationalInsuranceNumberPage => doYouKnowYourNationalInsuranceNumberCheckRoutes
    case DoYouHavePrimaryDocumentPage => doYouHavePrimaryDocumentCheckRoutes
    case DoYouHaveTwoSecondaryDocumentsPage => doYouHaveTwoSecondaryDocumentsCheckRoutes
    case WhatIsYourPreviousEmployersNamePage(index) => _ => routes.WhatIsYourPreviousEmployersAddressController.onPageLoad(index, CheckMode)
    case WhatIsYourPreviousEmployersAddressPage(index) => _ => routes.WhenDidYouStartWorkingForPreviousEmployerController.onPageLoad(index, CheckMode)
    case WhenDidYouStartWorkingForPreviousEmployerPage(index) => _ => routes.AreYouStillEmployedController.onPageLoad(index, CheckMode)
    case _ => _ => routes.CheckYourAnswersController.onPageLoad
  }

  private def doYouHaveAPreviousNameCheckRoutes(answers: UserAnswers): Call =
    (answers.get(DoYouHaveAPreviousNamePage), answers.get(WhatIsYourPreviousNamePage(Index(0)))) match {
      case (Some(true), None) => routes.WhatIsYourPreviousNameController.onPageLoad(Index(0), CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def areYouMarriedCheckRoutes(answers: UserAnswers): Call =
    (answers.get(AreYouMarriedPage), answers.get(WhenDidYouGetMarriedPage)) match {
      case (Some(true), None) => routes.WhenDidYouGetMarriedController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def haveYouPreviouslyBeenInAMarriageOrCivilPartnershipCheckRoutes(answers: UserAnswers): Call =
    (answers.get(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage), answers.get(PreviousMarriageOrPartnershipDetailsPage)) match {
      case (Some(true), None) => routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def isYourCurrentAddressInUkCheckRoutes(answers: UserAnswers): Call =
    (answers.get(IsYourCurrentAddressInUkPage), answers.get(WhatIsYourCurrentAddressUkPage), answers.get(WhatIsYourCurrentAddressInternationalPage)) match {
      case (Some(true), None, _) => routes.WhatIsYourCurrentAddressUkController.onPageLoad(CheckMode)
      case (Some(false), _, None) => routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(CheckMode)
      case (_, _, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def haveYouEverClaimedChildBenefitCheckRoutes(answers: UserAnswers): Call =
    (answers.get(HaveYouEverClaimedChildBenefitPage), answers.get(DoYouKnowYourChildBenefitNumberPage)) match {
      case (Some(true), None) => routes.DoYouKnowYourChildBenefitNumberController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def doYouKnowYourChildBenefitNumberCheckRoutes(answers: UserAnswers): Call =
    (answers.get(DoYouKnowYourChildBenefitNumberPage), answers.get(WhatIsYourChildBenefitNumberPage)) match {
      case (Some(true), None) => routes.WhatIsYourChildBenefitNumberController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def haveYouEverReceivedOtherUkBenefitsCheckRoutes(answers: UserAnswers): Call =
    (answers.get(HaveYouEverReceivedOtherUkBenefitsPage), answers.get(WhatOtherUkBenefitsHaveYouReceivedPage)) match {
      case (Some(true), None) => routes.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def doYouKnowYourNationalInsuranceNumberCheckRoutes(answers: UserAnswers): Call =
    (answers.get(DoYouKnowYourNationalInsuranceNumberPage), answers.get(WhatIsYourNationalInsuranceNumberPage)) match {
      case (Some(true), None) => routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(CheckMode)
      case (_, _) => routes.CheckYourAnswersController.onPageLoad
    }

  private def doYouHavePrimaryDocumentCheckRoutes(answers: UserAnswers): Call =
    (answers.get(DoYouHavePrimaryDocumentPage), answers.get(WhichPrimaryDocumentPage)) match {
      case (Some(true), Some(_)) => routes.CheckYourAnswersController.onPageLoad
      case (Some(true), None) => routes.WhichPrimaryDocumentController.onPageLoad(CheckMode)
      case (_, _) => routes.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(CheckMode)
    }

  private def doYouHaveTwoSecondaryDocumentsCheckRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveTwoSecondaryDocumentsPage).map {
      case true => routes.WhichAlternativeDocumentsController.onPageLoad(CheckMode)
      case false => routes.InsufficientDocumentsController.onPageLoad()
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
