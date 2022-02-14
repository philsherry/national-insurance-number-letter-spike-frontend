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
import pages.{PreviousAddressesQuery, PreviousEmployersQuery}
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

  // scalastyle:off
  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      
      val answers = request.userAnswers

      val personalDetails = SummaryListViewModel(Seq(
        WhatIsYourNameSummary.row(answers),
        DoYouHaveAPreviousNameSummary.row(answers),
        WhatIsYourPreviousNameSummary.row(answers),
        WhatIsYourDateOfBirthSummary.row(answers),
        AreYouReturningFromLivingAbroadSummary.row(answers),
        WhatIsYourTelephoneNumberSummary.row(answers),
        DoYouKnowYourNationalInsuranceNumberSummary.row(answers),
        WhatIsYourNationalInsuranceNumberSummary.row(answers)
      ).flatten)

      val addressHistory = {

        val previousAddressRows = {
          val numberOfAddresses = answers.get(PreviousAddressesQuery).getOrElse(List.empty).length
          (0 until numberOfAddresses).flatMap { index =>
            Seq(
              IsYourPreviousAddressInUkSummary.row(answers, index),
              WhatIsYourPreviousAddressUkSummary.row(answers, index),
              WhatIsYourPreviousAddressInternationalSummary.row(answers, index)
            ).flatten
          }
        }

        SummaryListViewModel(Seq(
          IsYourCurrentAddressInUkSummary.row(answers),
          WhatIsYourCurrentAddressUkSummary.row(answers),
          WhatIsYourCurrentAddressInternationalSummary.row(answers)
        ).flatten ++ previousAddressRows)
      }

      val relationshipHistory = SummaryListViewModel(Seq(
        AreYouMarriedSummary.row(answers),
        WhenDidYouGetMarriedSummary.row(answers),
        HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipSummary.row(answers),
        PreviousMarriageOrPartnershipDetailsSummary.row(answers)
      ).flatten)

      val benefitHistory = SummaryListViewModel(Seq(
        HaveYouEverClaimedChildBenefitSummary.row(answers),
        DoYouKnowYourChildBenefitNumberSummary.row(answers),
        WhatIsYourChildBenefitNumberSummary.row(answers),
        HaveYouEverReceivedOtherUkBenefitsSummary.row(answers),
        WhatOtherUkBenefitsHaveYouReceivedSummary.row(answers)
      ).flatten)

      val employmentHistory = {

        val previousEmployerRows = {
          val numberOfPreviousEmployers = answers.get(PreviousEmployersQuery).getOrElse(List.empty).length
          (0 until numberOfPreviousEmployers).flatMap { index =>
            Seq(
              WhatIsYourPreviousEmployersNameSummary.row(answers, index),
              WhatIsYourPreviousEmployersAddressSummary.row(answers, index),
              WhenDidYouStartWorkingForPreviousEmployerSummary.row(answers, index),
              WhenDidYouStopWorkingForPreviousEmployerSummary.row(answers, index)
            ).flatten
          }
        }

        SummaryListViewModel(Seq(
          HaveYouEverWorkedInUkSummary.row(answers),
          WhatIsYourEmployersNameSummary.row(answers),
          WhatIsYourEmployersAddressSummary.row(answers),
          WhenDidYouStartWorkingForEmployerSummary.row(answers),
          AreYouStillEmployedSummary.row(answers),
          WhenDidYouFinishYourEmploymentSummary.row(answers)
        ).flatten ++ previousEmployerRows)
      }

      val supportingDocuments = SummaryListViewModel(Seq(
        DoYouHavePrimaryDocumentSummary.row(answers),
        WhichPrimaryDocumentSummary.row(answers),
        DoYouHaveTwoSecondaryDocumentsSummary.row(answers),
        WhichAlternativeDocumentsSummary.row(answers)
      ).flatten)

      Ok(view(personalDetails, addressHistory, relationshipHistory, benefitHistory, employmentHistory, supportingDocuments))
  }
}
