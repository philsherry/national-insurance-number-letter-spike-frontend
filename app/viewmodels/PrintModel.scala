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
                             previousNames: List[WhatIsYourPreviousName],
                             dob: String,
                             gender: WhatIsYourGender,
                             currentAddress: List[String],
                             previousAddresses: List[PreviousAddressPrintModel],
                             returningFromLivingAbroad: Boolean,
                             telephoneNumber: String,
                             nino: Option[String],
                             currentRelationshipType: Option[String],
                             currentRelationshipDate: Option[String],
                             previousRelationships: Seq[PreviousMarriageOrPartnershipPrintModel],
                             claimedChildBenefit: Boolean,
                             childBenefitNumber: Option[String],
                             otherBenefits: Option[String],
                             currentEmployers: List[EmployerPrintModel],
                             previousEmployers: List[EmployerPrintModel],
                             primaryDocument: Option[String],
                             secondaryDocuments: Option[List[String]]
                           ) {

  def hasDocuments: Boolean = {
    primaryDocument.isDefined || secondaryDocuments.isDefined
  }

}

final case class PreviousAddressPrintModel(address: List[String], from: String, to: String)

final case class EmployerPrintModel(name: String, address: List[String], from: String, to: Option[String])

final case class PreviousMarriageOrPartnershipPrintModel(relationshipType: String, from: String, to: String, reason: String)

object PrintModel {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def from(userAnswers: UserAnswers): Option[PrintModel] = {
    for {
      name                                  <- userAnswers.get(WhatIsYourNamePage)
      previousName                          = getPreviousNames(userAnswers)
      dob                                   <- userAnswers.get(WhatIsYourDateOfBirthPage)
      gender                                <- userAnswers.get(WhatIsYourGenderPage)
      currentAddress                        <- getCurrentAddress(userAnswers)
      previousAddresses                     = getPreviousAddresses(userAnswers)
      returningFromWorkingAbroad            <- userAnswers.get(AreYouReturningFromLivingAbroadPage)
      telephoneNumber                       <- userAnswers.get(WhatIsYourTelephoneNumberPage)
      nino                                  = userAnswers.get(WhatIsYourNationalInsuranceNumberPage).map(_.nino)
      currentRelationshipType               = userAnswers.get(CurrentRelationshipTypePage)
      currentRelationshipDate               = userAnswers.get(WhenDidYouGetMarriedPage)
      previousRelationships                 = getPreviousRelationships(userAnswers)
      claimedChildBenefit                   <- userAnswers.get(HaveYouEverClaimedChildBenefitPage)
      childBenefitNumber                    = userAnswers.get(WhatIsYourChildBenefitNumberPage)
      otherBenefits                         = userAnswers.get(WhatOtherUkBenefitsHaveYouReceivedPage)
      (previousEmployers, currentEmployers) = getEmployers(userAnswers).partition(_.to.isDefined)
      primaryDocument                       = userAnswers.get(WhichPrimaryDocumentPage).map(key => s"whichPrimaryDocument.$key")
      secondaryDocuments                    = userAnswers.get(WhichAlternativeDocumentsPage).map(_.toList.map(key => s"whichAlternativeDocuments.$key"))
    } yield {
      PrintModel(
        name,
        previousName,
        dob.format(formatter),
        gender,
        currentAddress,
        previousAddresses,
        returningFromWorkingAbroad,
        telephoneNumber,
        nino,
        currentRelationshipType.map(_.toString),
        currentRelationshipDate.map(_.format(formatter)),
        previousRelationships,
        claimedChildBenefit,
        childBenefitNumber,
        otherBenefits,
        currentEmployers,
        previousEmployers,
        primaryDocument,
        secondaryDocuments
      )
    }
  }

  private[viewmodels] def getCurrentAddress(userAnswers: UserAnswers): Option[List[String]] = {
    userAnswers.get(WhatIsYourCurrentAddressUkPage).map { ukAddr =>
      ukAddr.lines
    }.orElse { userAnswers.get(WhatIsYourCurrentAddressInternationalPage).map { intAddr =>
        intAddr.lines
      }
    }
  }

  private[viewmodels] def getPreviousNames(userAnswers: UserAnswers): List[WhatIsYourPreviousName] = {
    val count = userAnswers.get(PreviousNamesQuery).getOrElse(List.empty).length
    (0 until count).toList.flatMap { index =>
      userAnswers.get(WhatIsYourPreviousNamePage(Index(index)))
    }
  }

  private def getPreviousRelationships(userAnswers: UserAnswers): Seq[PreviousMarriageOrPartnershipPrintModel] = {
    userAnswers.get(PreviousRelationshipsQuery).getOrElse(List.empty).indices.flatMap { index =>
      for {
        relationshipType <- userAnswers.get(PreviousRelationshipTypePage(Index(index)))
        details <- userAnswers.get(PreviousMarriageOrPartnershipDetailsPage(Index(index)))
      } yield PreviousMarriageOrPartnershipPrintModel(
        relationshipType.toString,
        formatter.format(details.startDate),
        formatter.format(details.endDate),
        details.endingReason
      )
    }
  }

  private[viewmodels] def getPreviousAddresses(userAnswers: UserAnswers): List[PreviousAddressPrintModel] = {
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

  private[viewmodels] def getEmployers(userAnswers: UserAnswers): List[EmployerPrintModel] = {
    val count = userAnswers.get(EmployersQuery).getOrElse(List.empty).length
    (0 until count).toList.flatMap { index =>
      for {
        name <- userAnswers.get(WhatIsYourEmployersNamePage(Index(index)))
        address <- userAnswers.get(WhatIsYourEmployersAddressPage(Index(index)))
        from <- userAnswers.get(WhenDidYouStartWorkingForEmployerPage(Index(index)))
        to = userAnswers.get(WhenDidYouStopWorkingForEmployerPage(Index(index)))
      } yield {
        EmployerPrintModel(
          name,
          address.lines,
          from.format(formatter),
          to.map(_.format(formatter))
        )
      }
    }
  }

}
