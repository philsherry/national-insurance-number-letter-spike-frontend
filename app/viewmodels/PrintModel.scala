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

package viewmodels

import models._
import pages._
import play.api.libs.json.Json
import queries.PreviousAddressListQuery

import java.time.LocalDate

final case class PrintModel(
                           name: WhatIsYourName,
                           previousName: Option[WhatIsYourPreviousName],
                           dob: LocalDate,
                           currentAddress: List[String],
                           previousAddresses: List[PreviousAddressPrintModel]
                           )

final case class PreviousAddressPrintModel(address: List[String], from: LocalDate, to: LocalDate)

object PrintModel {

  def from(userAnswers: UserAnswers): Option[PrintModel] = {
    for {
      name <- userAnswers.get(WhatIsYourNamePage)
      previousName = userAnswers.get(WhatIsYourPreviousNamePage)
      dob <- userAnswers.get(WhatIsYourDateOfBirthPage)
      currentAddress <- getCurrentAddress(userAnswers)
      previousAddresses = getPreviousAddresses(userAnswers)
    } yield {
      PrintModel(
        name,
        previousName,
        dob,
        currentAddress,
        previousAddresses
      )
    }
  }

  private def getCurrentAddress(userAnswers: UserAnswers): Option[List[String]] = {
    userAnswers.get(WhatIsYourCurrentAddressUkPage).map { address =>
      List(
        Some(address.addressLine1), address.addressLine2, address.addressLine3, Some(address.postcode))
    }.orElse { userAnswers.get(WhatIsYourCurrentAddressInternationalPage).map { address =>
        List(Some(address.addressLine1), address.addressLine2, address.addressLine3, Some(address.country)
        )
      }
    }.map(_.flatten)
  }

  private def getPreviousAddresses(userAnswers: UserAnswers): List[PreviousAddressPrintModel] = {
    val count = userAnswers.get(PreviousAddressListQuery).getOrElse(List.empty).length
    (0 until count).flatMap { index =>
      userAnswers.get(PreviousAddressQuery(Index(index))).flatMap { jsValue =>
        Json.fromJson(jsValue)(PreviousAddressUk.format).asOpt.map { ukAddr =>
          PreviousAddressPrintModel(
            List(Some(ukAddr.addressLine1), ukAddr.addressLine2, ukAddr.addressLine3, Some(ukAddr.postcode)).flatten,
            ukAddr.from,
            ukAddr.to
          )
        }.orElse {
          Json.fromJson(jsValue)(PreviousAddressInternational.format).asOpt.map { intAddr =>
            PreviousAddressPrintModel(
              List(Some(intAddr.addressLine1), intAddr.addressLine2, intAddr.addressLine3, Some(intAddr.country)).flatten,
              intAddr.from,
              intAddr.to
            )
          }
        }
      }
    }.toList
  }

}
