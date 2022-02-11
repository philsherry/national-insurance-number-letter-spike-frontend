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

package controllers

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers._
import viewmodels.govuk.summarylist._
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: CheckYourAnswersView
                                          ) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      
      val answers = request.userAnswers

      val list = SummaryListViewModel(
        rows = Seq(
          WhatIsYourNameSummary.row(answers),
          DoYouHaveAPreviousNameSummary.row(answers),
          WhatIsYourPreviousNameSummary.row(answers),
          WhatIsYourDateOfBirthSummary.row(answers),
          IsYourCurrentAddressInUkSummary.row(answers),
          WhatIsYourCurrentAddressUkSummary.row(answers),
          WhatIsYourCurrentAddressInternationalSummary.row(answers),
          DoYouHaveAnyPreviousAddressesSummary.row(answers),
          IsYourPreviousAddressInUkSummary.row(answers, 0),
          WhatIsYourPreviousAddressUkSummary.row(answers, 0),
          WhatIsYourPreviousAddressInternationalSummary.row(answers, 0),
          AreYouReturningFromLivingAbroadSummary.row(answers),
          WhatIsYourTelephoneNumberSummary.row(answers),
          DoYouKnowYourNationalInsuranceNumberSummary.row(answers),
          WhatIsYourNationalInsuranceNumberSummary.row(answers),
          AreYouMarriedSummary.row(answers),
          WhenDidYouGetMarriedSummary.row(answers),
          HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipSummary.row(answers),
          PreviousMarriageOrPartnershipDetailsSummary.row(answers),
          HaveYouEverClaimedChildBenefitSummary.row(answers),
          DoYouKnowYourChildBenefitNumberSummary.row(answers),
          WhatIsYourChildBenefitNumberSummary.row(answers),
          HaveYouEverReceivedOtherUkBenefitsSummary.row(answers),
          WhatOtherUkBenefitsHaveYouReceivedSummary.row(answers),
          HaveYouEverWorkedInUkSummary.row(answers),
          WhatIsYourEmployersNameSummary.row(answers),
          WhatIsYourEmployersAddressSummary.row(answers),
          WhenDidYouStartWorkingForEmployerSummary.row(answers),
          AreYouStillEmployedSummary.row(answers),
          WhenDidYouFinishYourEmploymentSummary.row(answers),
          DoYouHaveAnyPreviousEmployersSummary.row(answers),
          WhatIsYourPreviousEmployersNameSummary.row(answers, 0),
          WhatIsYourPreviousEmployersAddressSummary.row(answers, 0),
          WhenDidYouStartWorkingForPreviousEmployerSummary.row(answers, 0),
          WhenDidYouStopWorkingForPreviousEmployerSummary.row(answers, 0),
          DoYouHavePrimaryDocumentSummary.row(answers),
          WhichPrimaryDocumentSummary.row(answers),
          DoYouHaveTwoSecondaryDocumentsSummary.row(answers),
          WhichAlternativeDocumentsSummary.row(answers)
        ).flatten
      )

      Ok(view(list))
  }
}
