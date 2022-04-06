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
import pages.{WhatIsYourPreviousEmployersAddressPage, WhatIsYourPreviousEmployersNamePage, WhenDidYouStartWorkingForPreviousEmployerPage, WhenDidYouStopWorkingForPreviousEmployerPage}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}

import java.time.format.DateTimeFormatter

object PreviousEmployerSummary {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def item(answers: UserAnswers, mode: Mode, i: Int)(implicit messages: Messages): ListWithActionsItem = {

    val content = HtmlContent(
      List(
        answers.get(WhatIsYourPreviousEmployersNamePage(Index(i))),
        answers.get(WhatIsYourPreviousEmployersAddressPage(Index(i))).map(_.lines.mkString(", ")),
        for {
          from <- answers.get(WhenDidYouStartWorkingForPreviousEmployerPage(Index(i))).map(_.format(dateFormatter))
          to   <- answers.get(WhenDidYouStopWorkingForPreviousEmployerPage(Index(i))).map(_.format(dateFormatter))
        } yield Messages("site.range", from, to)
      ).flatten.map(HtmlFormat.escape(_).toString).mkString("<br/>")
    )

    ListWithActionsItem(
      name = content,
      actions = List(
        ListWithActionsAction(content = Text(Messages("site.change")), href = routes.WhatIsYourPreviousEmployersNameController.onPageLoad(Index(i), mode).url),
        ListWithActionsAction(content = Text(Messages("site.remove")), href = routes.AreYouSureYouWantToRemovePreviousEmployerController.onPageLoad(Index(i), mode).url)
      )
    )
  }
}
