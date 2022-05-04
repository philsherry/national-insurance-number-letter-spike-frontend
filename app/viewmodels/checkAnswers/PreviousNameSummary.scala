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
import pages.WhatIsYourPreviousNamePage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}

object PreviousNameSummary {

  def item(answers: UserAnswers, mode: Mode, i: Int)(implicit messages: Messages): ListWithActionsItem = {

    val name = answers
      .get(WhatIsYourPreviousNamePage(Index(i)))
      .map(n => Seq(
        Some(n.firstName),
        n.middleNames,
        Some(n.lastName)).flatten).getOrElse(Seq.empty).mkString(" ")

    ListWithActionsItem(
      name = HtmlContent(name),
      actions = List(
        ListWithActionsAction(content = Text(Messages("site.change")), visuallyHiddenText = Some(Messages("checkYourAnswers.changePreviousNameHidden", name)), href = routes.WhatIsYourPreviousNameController.onPageLoad(Index(i), mode).url),
        ListWithActionsAction(content = Text(Messages("site.remove")), visuallyHiddenText = Some(Messages("checkYourAnswers.removePreviousNameHidden", name)), href = routes.AreYouSureYouWantToRemovePreviousNameController.onPageLoad(Index(i), mode).url)
      )
    )
  }
}
