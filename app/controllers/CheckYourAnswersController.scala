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
import models.CheckMode
import pages._
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

      val previousNames = answers.get(PreviousNamesQuery).getOrElse(List.empty)
      .indices.map(PreviousNameSummary.item(answers, CheckMode, _))

      val personalDetails = SummaryListViewModel(Seq(
        WhatIsYourNameSummary.row(answers),
        DoYouHaveAPreviousNameSummary.row(answers),
        WhatIsYourDateOfBirthSummary.row(answers),
        WhatIsYourGenderSummary.row(answers),
        AreYouReturningFromLivingAbroadSummary.row(answers),
        WhatIsYourTelephoneNumberSummary.row(answers),
        DoYouKnowYourNationalInsuranceNumberSummary.row(answers),
        WhatIsYourNationalInsuranceNumberSummary.row(answers)
      ).flatten)

      val currentAddress =
        SummaryListViewModel(Seq(
          IsYourCurrentAddressInUkSummary.row(answers),
          WhatIsYourCurrentAddressUkSummary.row(answers),
          WhatIsYourCurrentAddressInternationalSummary.row(answers)
        ).flatten)

      val previousAddresses = answers.get(PreviousAddressesQuery).getOrElse(List.empty)
        .indices.map(PreviousAddressSummary.item(answers, CheckMode, _))

      val currentRelationship = SummaryListViewModel(Seq(
        AreYouMarriedSummary.row(answers),
        CurrentRelationshipTypeSummary.row(answers),
        WhenDidYouGetMarriedSummary.row(answers)
      ).flatten)

      val previousRelationships = answers.get(PreviousRelationshipsQuery).getOrElse(List.empty)
        .indices.flatMap(PreviousRelationshipSummary.item(answers, CheckMode, _))

      val benefitHistory = SummaryListViewModel(Seq(
        HaveYouEverClaimedChildBenefitSummary.row(answers),
        DoYouKnowYourChildBenefitNumberSummary.row(answers),
        WhatIsYourChildBenefitNumberSummary.row(answers),
        HaveYouEverReceivedOtherUkBenefitsSummary.row(answers),
        WhatOtherUkBenefitsHaveYouReceivedSummary.row(answers)
      ).flatten)

      val employmentHistory =
        SummaryListViewModel(Seq(
          HaveYouEverWorkedInUkSummary.row(answers)
        ).flatten)

      val employers = answers.get(EmployersQuery).getOrElse(List.empty)
        .indices.map(EmployerSummary.item(answers, CheckMode, _))

      val supportingDocuments = SummaryListViewModel(Seq(
        DoYouHavePrimaryDocumentSummary.row(answers),
        WhichPrimaryDocumentSummary.row(answers),
        DoYouHaveTwoSecondaryDocumentsSummary.row(answers),
        WhichAlternativeDocumentsSummary.row(answers)
      ).flatten)

      Ok(view(
        personalDetails,
        previousNames,
        currentAddress,
        previousAddresses,
        currentRelationship,
        previousRelationships,
        benefitHistory,
        employmentHistory,
        employers,
        supportingDocuments
      ))
  }
}
