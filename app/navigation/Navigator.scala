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
    case WhatIsYourNamePage                         => _ => routes.DoYouHaveAPreviousNameController.onPageLoad(NormalMode)
    case DoYouHaveAPreviousNamePage                 => doYouHaveAPreviousNameRoutes
    case WhatIsYourPreviousNamePage                 => _ => routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
    case WhatIsYourDateOfBirthPage                  => _ => routes.IsYourCurrentAddressInUkController.onPageLoad(NormalMode)
    case IsYourCurrentAddressInUkPage               => isYourCurrentAddressInUkRoutes
    case WhatIsYourCurrentAddressUkPage             => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case WhatIsYourCurrentAddressInternationalPage  => _ => routes.DoYouHaveAnyPreviousAddressesController.onPageLoad(NormalMode)
    case DoYouHaveAnyPreviousAddressesPage          => doYouHaveAnyPreviousAddressesRoutes
    case AreYouReturningFromLivingAbroadPage        => _ => routes.WhatIsYourTelephoneNumberController.onPageLoad(NormalMode)
    case WhatIsYourTelephoneNumberPage              => _ => routes.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(NormalMode)
    case DoYouKnowYourNationalInsuranceNumberPage   => doYouKnowYourNationalInsuranceNumberRoutes
    case WhatIsYourNationalInsuranceNumberPage      => _ => routes.AreYouMarriedController.onPageLoad(NormalMode)
    case AreYouMarriedPage                          => areYouMarriedRoutes
    case _ => _ => routes.IndexController.onPageLoad
  }

  private def doYouHaveAPreviousNameRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveAPreviousNamePage).map {
      case true  => routes.WhatIsYourPreviousNameController.onPageLoad(NormalMode)
      case false => routes.WhatIsYourDateOfBirthController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def isYourCurrentAddressInUkRoutes(answers: UserAnswers): Call =
    answers.get(IsYourCurrentAddressInUkPage).map {
      case true  => routes.WhatIsYourCurrentAddressUkController.onPageLoad(NormalMode)
      case false => routes.WhatIsYourCurrentAddressInternationalController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouHaveAnyPreviousAddressesRoutes(answers: UserAnswers): Call =
    answers.get(DoYouHaveAnyPreviousAddressesPage).map {
      case true  => ???
      case false => routes.AreYouReturningFromLivingAbroadController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def doYouKnowYourNationalInsuranceNumberRoutes(answers: UserAnswers): Call =
    answers.get(DoYouKnowYourNationalInsuranceNumberPage).map {
      case true  => routes.WhatIsYourNationalInsuranceNumberController.onPageLoad(NormalMode)
      case false => routes.AreYouMarriedController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private def areYouMarriedRoutes(answers: UserAnswers): Call =
    answers.get(AreYouMarriedPage).map {
      case true  => routes.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(NormalMode)
      case false => routes.AreYouInACivilPartnershipController.onPageLoad(NormalMode)
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ => _ => routes.CheckYourAnswersController.onPageLoad
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
