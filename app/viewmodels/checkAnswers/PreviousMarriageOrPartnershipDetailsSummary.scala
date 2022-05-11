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

package viewmodels.checkAnswers

import controllers.routes
import models.{Index, Mode, UserAnswers}
import pages.PreviousMarriageOrPartnershipDetailsPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.hmrcfrontend.views.Aliases.ListWithActionsItem
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.listwithactions.ListWithActionsAction

import java.time.format.DateTimeFormatter

object PreviousMarriageOrPartnershipDetailsSummary  {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def item(answers: UserAnswers, mode: Mode, i: Int)(implicit messages: Messages): Option[ListWithActionsItem] =
    answers
      .get(PreviousMarriageOrPartnershipDetailsPage(Index(i)))
      .map { details =>
        val from = dateFormatter.format(details.startDate)
        val to = dateFormatter.format(details.endDate)
        val content = messages("previousMarriageOrPartnershipDetails.checkYourAnswersFormat", from, to)

        ListWithActionsItem(
          name = Text(content),
          actions = List(
            ListWithActionsAction(content = Text(Messages("site.change")), visuallyHiddenText = Some(Messages("checkYourAnswers.changePreviousRelationshipHidden", from, to)), href = routes.PreviousMarriageOrPartnershipDetailsController.onPageLoad(Index(i), mode).url),
            ListWithActionsAction(content = Text(Messages("site.remove")), visuallyHiddenText = Some(Messages("checkYourAnswers.removePreviousRelationshipHidden", from, to)), href = routes.AreYouSureYouWantToRemovePreviousRelationshipController.onPageLoad(Index(i), mode).url)
          )
        )
      }
}
