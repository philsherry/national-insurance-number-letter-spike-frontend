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

package models

import cats.data.{Ior, IorNec}
import cats.implicits._
import models.JourneyModel._
import pages._
import queries.Query

import java.time.LocalDate

final case class JourneyModel(
                               currentName: WhatIsYourName,
                               previousNames: List[WhatIsYourPreviousName],
                               dateOfBirth: LocalDate,
                               gender: WhatIsYourGender,
                               telephoneNumber: String,
                               nationalInsuranceNumber: Option[String],
                               returningFromLivingAbroad: Boolean,
                               currentAddress: CurrentAddress,
                               previousAddresses: List[PreviousAddress],
                               currentRelationship: Option[CurrentRelationship],
                               previousRelationships: List[PreviousRelationship],
                               claimedChildBenefit: Boolean,
                               childBenefitNumber: Option[String],
                               otherBenefits: Option[String],
                               employers: List[Employer],
                               primaryDocument: Option[String],
                               alternativeDocuments: List[String]
                             )

object JourneyModel {

  final case class CurrentRelationship(relationshipType: CurrentRelationshipType, from: LocalDate)
  final case class PreviousRelationship(relationshipType: PreviousRelationshipType, from: LocalDate, to: LocalDate, endReason: String)

  final case class Employer(name: String, address: EmployersAddress, startDate: LocalDate, endDate: Option[LocalDate])

  def from(answers: UserAnswers): IorNec[Query, JourneyModel] = {
    (
      answers.getIor(WhatIsYourNamePage),
      getPreviousNames(answers),
      answers.getIor(WhatIsYourDateOfBirthPage),
      answers.getIor(WhatIsYourGenderPage),
      answers.getIor(WhatIsYourTelephoneNumberPage),
      getNino(answers),
      answers.getIor(AreYouReturningFromLivingAbroadPage),
      getCurrentAddress(answers),
      getPreviousAddresses(answers),
      getCurrentRelationship(answers),
      getPreviousRelationships(answers),
      answers.getIor(HaveYouEverClaimedChildBenefitPage),
      getChildBenefitNumber(answers),
      getOtherBenefits(answers),
      getEmployers(answers),
      getPrimaryDocument(answers),
      getAlternativeDocuments(answers)
    ).parMapN(JourneyModel.apply)
  }

  private def getPreviousNames(answers: UserAnswers): IorNec[Query, List[WhatIsYourPreviousName]] =
    answers.get(PreviousNamesQuery).getOrElse(Seq.empty).indices.toList
      .parTraverse(i => answers.getIor(WhatIsYourPreviousNamePage(Index(i))))

  private def getNino(answers: UserAnswers): IorNec[Query, Option[String]] =
    answers.getIor(DoYouKnowYourNationalInsuranceNumberPage).flatMap {
      case true  => answers.getIor(WhatIsYourNationalInsuranceNumberPage).map(nino => Some(nino.value))
      case false => Ior.Right(None)
    }

  private def getCurrentAddress(answers: UserAnswers): IorNec[Query, CurrentAddress] =
    answers.getIor(IsYourCurrentAddressInUkPage).flatMap {
      case true  => answers.getIor(WhatIsYourCurrentAddressUkPage)
      case false => answers.getIor(WhatIsYourCurrentAddressInternationalPage)
    }

  private def getPreviousAddresses(answers: UserAnswers): IorNec[Query, List[PreviousAddress]] =
    answers.get(PreviousAddressesQuery).getOrElse(Seq.empty).indices.toList.parFlatTraverse { i =>
      val previousAddress = answers.getIor(IsYourPreviousAddressInUkPage(Index(i))).flatMap {
        case true  => answers.getIor(WhatIsYourPreviousAddressUkPage(Index(i)))
        case false => answers.getIor(WhatIsYourPreviousAddressInternationalPage(Index(i)))
      }
      previousAddress.putRight(previousAddress.toList)
    }

  private def getCurrentRelationship(answers: UserAnswers): IorNec[Query, Option[CurrentRelationship]] =
    answers.getIor(AreYouMarriedPage).flatMap {
      case true  => (answers.getIor(CurrentRelationshipTypePage), answers.getIor(WhenDidYouGetMarriedPage)).parMapN { (relationshipType, date) =>
        Some(CurrentRelationship(relationshipType, date))
      }
      case false => Ior.Right(None)
    }

  private def getPreviousRelationships(answers: UserAnswers): IorNec[Query, List[PreviousRelationship]] =
    answers.get(PreviousRelationshipsQuery).getOrElse(Seq.empty).indices.toList.parFlatTraverse { i =>
      val previousRelationship = (answers.getIor(PreviousMarriageOrPartnershipDetailsPage(Index(i))), answers.getIor(PreviousRelationshipTypePage(Index(i))))
        .parMapN { (details, relationshipType) =>
          PreviousRelationship(relationshipType, details.startDate, details.endDate, details.endingReason)
        }
      previousRelationship.putRight(previousRelationship.toList)
    }

  private def getChildBenefitNumber(answers: UserAnswers): IorNec[Query, Option[String]] =
    answers.getIor(HaveYouEverClaimedChildBenefitPage).flatMap {
      case true =>
        answers.getIor(DoYouKnowYourChildBenefitNumberPage).flatMap {
          case true  => answers.getIor(WhatIsYourChildBenefitNumberPage).map(Some(_))
          case false => Ior.Right(None)
        }
      case false => Ior.Right(None)
    }

  private def getOtherBenefits(answers: UserAnswers): IorNec[Query, Option[String]] =
    answers.getIor(HaveYouEverReceivedOtherUkBenefitsPage).flatMap {
      case true  => answers.getIor(WhatOtherUkBenefitsHaveYouReceivedPage).map(Some(_))
      case false => Ior.Right(None)
    }

  private def getEmployers(answers: UserAnswers): IorNec[Query, List[Employer]] =
    answers.get(EmployersQuery).getOrElse(Seq.empty).indices.toList.parFlatTraverse { i =>
      val employer = (
        answers.getIor(WhatIsYourEmployersNamePage(Index(i))),
        answers.getIor(WhatIsYourEmployersAddressPage(Index(i))),
        answers.getIor(WhenDidYouStartWorkingForEmployerPage(Index(i))),
        getEmploymentEndDate(answers, Index(i))
      ).parMapN(Employer.apply)
      employer.putRight(employer.toList)
    }

  private def getEmploymentEndDate(answers: UserAnswers, index: Index): IorNec[Query, Option[LocalDate]] =
    answers.getIor(AreYouStillEmployedPage(index)).flatMap {
      case true  => Ior.Right(None)
      case false => answers.getIor(WhenDidYouStopWorkingForEmployerPage(index)).map(Some(_))
    }

  private def getPrimaryDocument(answers: UserAnswers): IorNec[Query, Option[String]] =
    answers.getIor(DoYouHavePrimaryDocumentPage).flatMap {
      case true  => answers.getIor(WhichPrimaryDocumentPage).map(d => Some(d.toString))
      case false => Ior.Right(None)
    }

  private def getAlternativeDocuments(answers: UserAnswers): IorNec[Query, List[String]] =
    answers.getIor(DoYouHavePrimaryDocumentPage).flatMap {
      case true  => Ior.Right(List.empty)
      case false => answers.getIor(DoYouHaveTwoSecondaryDocumentsPage).flatMap {
        case true  => answers.getIor(WhichAlternativeDocumentsPage).map(_.map(_.toString).toList)
        case false => Ior.Right(List.empty)
      }
    }
}