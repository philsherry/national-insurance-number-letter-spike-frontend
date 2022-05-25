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
import pages.{WhatIsYourPreviousAddressUkPage, WhatIsYourPreviousAddressInternationalPage}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.hmrcfrontend.views.Aliases.{ListWithActionsAction, ListWithActionsItem}

import java.time.format.DateTimeFormatter

object PreviousAddressSummary {

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def item(answers: UserAnswers, mode: Mode, i: Int)(implicit messages: Messages): Option[ListWithActionsItem] = {
    for {
      address <- answers.get(WhatIsYourPreviousAddressUkPage(Index(i))) orElse answers.get(WhatIsYourPreviousAddressInternationalPage(Index(i)))
    } yield {

      val content = HtmlContent {
        List(
          address.lines.mkString(", "),
          Messages("site.range", address.from.format(dateFormatter), address.to.format(dateFormatter))
        ).map(HtmlFormat.escape(_).toString).mkString("<br/>")
      }

      ListWithActionsItem(
        name = content,
        actions = List(
          ListWithActionsAction(content = Text(Messages("site.change")), visuallyHiddenText = Some(Messages("checkYourAnswers.changePreviousAddressHidden", address.lines.mkString(", "))), href = routes.IsYourPreviousAddressInUkController.onPageLoad(Index(i), mode).url),
          ListWithActionsAction(content = Text(Messages("site.remove")), visuallyHiddenText = Some(Messages("checkYourAnswers.removePreviousAddressHidden", address.lines.mkString(", "))), href = routes.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(Index(i), mode).url)
        )
      )
    }
  }
}
