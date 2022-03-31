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
import queries._
import java.time.format.DateTimeFormatter

final case class PrintModel(
                           name: WhatIsYourName,
                           previousName: Option[WhatIsYourPreviousName],
                           dob: String,
                           currentAddress: List[String],
                           previousAddresses: List[PreviousAddressPrintModel],
                           returningFromLivingAbroad: Boolean,
                           telephoneNumber: String,
                           nino: Option[String],
                           marriage: Option[String],
                           civilPartnership: Option[String],
                           previousMarriageOrPartnership: Option[PreviousMarriageOrPartnershipPrintModel],
                           claimedChildBenefit: Boolean,
                           childBenefitNumber: Option[String],
                           otherBenefits: Option[String],
                           mostRecentUkEmployer: Option[MostRecentUkEmployerPrintModel],
                           previousEmployers: List[PreviousEmployerPrintModel],
                           primaryDocument: Option[PrimaryDocument],
                           secondaryDocuments: Option[Set[AlternativeDocuments]]
                           )

final case class PreviousAddressPrintModel(address: List[String], from: String, to: String)

final case class MostRecentUkEmployerPrintModel(name: String, address: List[String], from: String, to: Option[String])

final case class PreviousEmployerPrintModel(name: String, address: List[String], from: String, to: String)

final case class PreviousMarriageOrPartnershipPrintModel(from: String, to: String, reason: String)

object PreviousMarriageOrPartnershipPrintModel {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def from(model: PreviousMarriageOrPartnershipDetails): PreviousMarriageOrPartnershipPrintModel = {
    PreviousMarriageOrPartnershipPrintModel(
      model.startDate.format(formatter),
      model.endDate.format(formatter),
      model.endingReason
    )
  }
}

object PrintModel {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def from(userAnswers: UserAnswers): Option[PrintModel] = {
    for {
      name <- userAnswers.get(WhatIsYourNamePage)
      previousName = userAnswers.get(WhatIsYourPreviousNamePage)
      dob <- userAnswers.get(WhatIsYourDateOfBirthPage)
      currentAddress <- getCurrentAddress(userAnswers)
      previousAddresses = getPreviousAddresses(userAnswers)
      returningFromWorkingAbroad <- userAnswers.get(AreYouReturningFromLivingAbroadPage)
      telephoneNumber <- userAnswers.get(WhatIsYourTelephoneNumberPage)
      nino = userAnswers.get(WhatIsYourNationalInsuranceNumberPage).map(_.nino)
      marriage = userAnswers.get(WhenDidYouGetMarriedPage)
      civilPartnership = userAnswers.get(WhenDidYouEnterACivilPartnershipPage)
      previousMarriageOrPartnership = userAnswers.get(PreviousMarriageOrPartnershipDetailsPage)
      claimedChildBenefit <- userAnswers.get(HaveYouEverClaimedChildBenefitPage)
      childBenefitNumber = userAnswers.get(WhatIsYourChildBenefitNumberPage)
      otherBenefits = userAnswers.get(WhatOtherUkBenefitsHaveYouReceivedPage)
      mostRecentUkEmployer = getMostRecentUkEmployer(userAnswers)
      previousEmployers = getPreviousEmployers(userAnswers)
      primaryDocument = userAnswers.get(WhichPrimaryDocumentPage)
      secondaryDocuments = userAnswers.get(WhichAlternativeDocumentsPage)
    } yield {
      PrintModel(
        name,
        previousName,
        dob.format(formatter),
        currentAddress,
        previousAddresses,
        returningFromWorkingAbroad,
        telephoneNumber,
        nino,
        marriage.map(_.format(formatter)),
        civilPartnership.map(_.format(formatter)),
        previousMarriageOrPartnership.map(PreviousMarriageOrPartnershipPrintModel.from),
        claimedChildBenefit,
        childBenefitNumber,
        otherBenefits,
        mostRecentUkEmployer,
        previousEmployers,
        primaryDocument,
        secondaryDocuments
      )
    }
  }

  private def getCurrentAddress(userAnswers: UserAnswers): Option[List[String]] = {
    userAnswers.get(WhatIsYourCurrentAddressUkPage).map { ukAddr =>
      ukAddr.lines
    }.orElse { userAnswers.get(WhatIsYourCurrentAddressInternationalPage).map { intAddr =>
        intAddr.lines
      }
    }
  }

  private def getPreviousAddresses(userAnswers: UserAnswers): List[PreviousAddressPrintModel] = {
    val count = userAnswers.get(PreviousAddressListQuery).getOrElse(List.empty).length
    (0 until count).toList.flatMap { index =>
      userAnswers.get(WhatIsYourPreviousAddressUkPage(Index(index))).map { prevUk =>
        PreviousAddressPrintModel(
          prevUk.lines,
          prevUk.from.format(formatter),
          prevUk.to.format(formatter)
        )
      }.orElse { userAnswers.get(WhatIsYourPreviousAddressInternationalPage(Index(index))).map { prevInt =>
        PreviousAddressPrintModel(
          prevInt.lines,
          prevInt.from.format(formatter),
          prevInt.to.format(formatter)
        )
        }
      }
    }
  }

  private def getMostRecentUkEmployer(userAnswers: UserAnswers): Option[MostRecentUkEmployerPrintModel] = {
    for {
      name <- userAnswers.get(WhatIsYourEmployersNamePage)
      address <- userAnswers.get(WhatIsYourEmployersAddressPage)
      from <- userAnswers.get(WhenDidYouStartWorkingForEmployerPage)
      to = userAnswers.get(WhenDidYouFinishYourEmploymentPage)
    } yield {
      MostRecentUkEmployerPrintModel(
        name,
        address.lines,
        from.format(formatter),
        to.map(_.format(formatter))
      )
    }
  }

  private def getPreviousEmployers(userAnswers: UserAnswers): List[PreviousEmployerPrintModel] = {
    val count = userAnswers.get(PreviousEmployersQuery).getOrElse(List.empty).length
    (0 until count).toList.flatMap { index =>
      for {
        name <- userAnswers.get(WhatIsYourPreviousEmployersNamePage(Index(index)))
        address <- userAnswers.get(WhatIsYourPreviousEmployersAddressPage(Index(index)))
        from <- userAnswers.get(WhenDidYouStartWorkingForPreviousEmployerPage(Index(index)))
        to <- userAnswers.get(WhenDidYouStopWorkingForPreviousEmployerPage(Index(index)))
      } yield {
        PreviousEmployerPrintModel(
          name,
          address.lines,
          from.format(formatter),
          to.format(formatter)
        )
      }
    }
  }

}
