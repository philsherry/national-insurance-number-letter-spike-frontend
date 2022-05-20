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

package audit

import audit.DownloadAuditEvent.{Addresses, Relationships, _}
import cats.data.EitherNec
import models.{Country, Index, UserAnswers, WhatIsYourGender}
import pages._
import cats.implicits._
import play.api.libs.json.{Format, Json}
import queries.Query

import java.time.LocalDate

final case class DownloadAuditEvent(
                                     names: Names,
                                     dateOfBirth: LocalDate,
                                     gender: WhatIsYourGender,
                                     addresses: Addresses,
                                     returningFromLivingAbroad: Boolean,
                                     telephoneNumber: String,
                                     nationalInsuranceNumber: Option[String],
                                     relationships: Relationships,
                                     benefits: Benefits,
                                     employers: List[Employer],
                                     documents: List[String]
                                   )

// scalastyle:off
object DownloadAuditEvent {

  def apply(answers: UserAnswers): EitherNec[Query, DownloadAuditEvent] = {
    (
      getNames(answers), answers.getNec(WhatIsYourDateOfBirthPage), answers.getNec(WhatIsYourGenderPage), getNino(answers),
      getAddresses(answers), answers.getNec(AreYouReturningFromLivingAbroadPage), answers.getNec(WhatIsYourTelephoneNumberPage),
      getRelationships(answers), getBenefits(answers), getEmployers(answers), getDocuments(answers)
    ).parMapN { (names, dob, gender, nino, addresses, returningFromLivingAbroad, telephoneNumber, relationships, benefits, employers, documents) =>
      DownloadAuditEvent(
        names, dob, gender, addresses, returningFromLivingAbroad, telephoneNumber,
        nino, relationships, benefits, employers, documents
      )
    }
  }

  private def getNames(answers: UserAnswers): EitherNec[Query, Names] = {
    (
      answers.getNec(WhatIsYourNamePage),
      answers.get(PreviousNamesQuery).getOrElse(Seq.empty).indices.toList.parTraverse(i => answers.getNec(WhatIsYourPreviousNamePage(Index(i))))
    ).parMapN { (current, previous) =>
      Names(
        currentName = Name(current.title, current.firstName, current.middleNames, current.lastName),
        previousNames = previous.map { previous =>
          Name(None, previous.firstName, previous.middleNames, previous.lastName)
        }
      )
    }
  }

  private def getNino(answers: UserAnswers): EitherNec[Query, Option[String]] =
    answers.getNec(DoYouKnowYourNationalInsuranceNumberPage).flatMap {
      case true  => answers.getNec(WhatIsYourNationalInsuranceNumberPage).map(nino => Some(nino.value))
      case false => Right(None)
    }

  private def getAddresses(answers: UserAnswers): EitherNec[Query, Addresses] =
    (getCurrentAddress(answers), getPreviousAddresses(answers)).parMapN(Addresses.apply)

  private def getCurrentAddress(answers: UserAnswers): EitherNec[Query, Address] =
    answers.getNec(IsYourCurrentAddressInUkPage).flatMap {
      case true  => answers.getNec(WhatIsYourCurrentAddressUkPage)
        .map(a => Address(a.addressLine1, a.addressLine2, a.addressLine3, Some(a.postcode), None))
      case false => answers.getNec(WhatIsYourCurrentAddressInternationalPage)
        .map(a => Address(a.addressLine1, a.addressLine2, a.addressLine3, a.postcode, Some(a.country)))
    }

  private def getPreviousAddresses(answers: UserAnswers): EitherNec[Query, List[PreviousAddress]] =
    answers.getNec(PreviousAddressesQuery).getOrElse(Seq.empty).indices.toList.parTraverse { i =>
      answers.getNec(IsYourPreviousAddressInUkPage(Index(i))).flatMap {
        case true  => answers.getNec(WhatIsYourPreviousAddressUkPage(Index(i)))
          .map(a => PreviousAddress(a.addressLine1, a.addressLine2, a.addressLine3, Some(a.postcode), None, a.from, a.to))
        case false => answers.getNec(WhatIsYourPreviousAddressInternationalPage(Index(i)))
          .map(a => PreviousAddress(a.addressLine1, a.addressLine2, a.addressLine3, a.postcode, Some(a.country), a.from, a.to))
      }
    }

  private def getRelationships(answers: UserAnswers): EitherNec[Query, Relationships] =
    (getCurrentRelationship(answers), getPreviousRelationships(answers)).parMapN(Relationships.apply)

  private def getCurrentRelationship(answers: UserAnswers): EitherNec[Query, Option[Relationship]] =
    answers.getNec(AreYouMarriedPage).flatMap {
      case true  => (answers.getNec(CurrentRelationshipTypePage), answers.getNec(WhenDidYouGetMarriedPage)).parMapN { (relationshipType, date) =>
        Some(Relationship(relationshipType.toString, date))
      }
      case false => Right(None)
    }

  private def getPreviousRelationships(answers: UserAnswers): EitherNec[Query, List[PreviousRelationship]] =
    answers.get(PreviousRelationshipsQuery).getOrElse(Seq.empty).indices.toList.parTraverse { i =>
      (answers.getNec(PreviousMarriageOrPartnershipDetailsPage(Index(i))), answers.getNec(PreviousRelationshipTypePage(Index(i)))).parMapN { (details, relationshipType) =>
        PreviousRelationship(relationshipType.toString, details.startDate, details.endDate, details.endingReason)
      }
    }

  private def getBenefits(answers: UserAnswers): EitherNec[Query, Benefits] =
    (
      answers.getNec(HaveYouEverClaimedChildBenefitPage),
      getChildBenefitNumber(answers),
      getOtherBenefits(answers)
    ).parMapN(Benefits.apply)

  private def getChildBenefitNumber(answers: UserAnswers): EitherNec[Query, Option[String]] =
    answers.getNec(DoYouKnowYourChildBenefitNumberPage).flatMap {
      case true  => answers.getNec(WhatIsYourChildBenefitNumberPage).map(Some(_))
      case false => Right(None)
    }

  private def getOtherBenefits(answers: UserAnswers): EitherNec[Query, Option[String]] =
    answers.getNec(HaveYouEverReceivedOtherUkBenefitsPage).flatMap {
      case true  => answers.getNec(WhatOtherUkBenefitsHaveYouReceivedPage).map(Some(_))
      case false => Right(None)
    }

  private def getEmployers(answers: UserAnswers): EitherNec[Query, List[Employer]] =
    answers.get(EmployersQuery).getOrElse(Seq.empty).indices.toList.parTraverse { i =>
      (
        answers.getNec(WhatIsYourEmployersNamePage(Index(i))),
        answers.getNec(WhatIsYourEmployersAddressPage(Index(i))),
        answers.getNec(WhenDidYouStartWorkingForEmployerPage(Index(i))),
        getEmploymentEndDate(answers, Index(i))
      ).parMapN { (name, address, from, to) =>
        Employer(name, EmployerAddress(address.addressLine1, address.addressLine2, address.addressLine3, address.postcode), from, to)
      }
    }

  private def getEmploymentEndDate(answers: UserAnswers, index: Index): EitherNec[Query, Option[LocalDate]] =
    answers.getNec(AreYouStillEmployedPage(index)).flatMap {
      case true  => Right(None)
      case false => answers.getNec(WhenDidYouStopWorkingForEmployerPage(index)).map(Some(_))
    }

  private def getDocuments(answers: UserAnswers): EitherNec[Query, List[String]] = {
    answers.getNec(DoYouHavePrimaryDocumentPage).flatMap {
      case true  => answers.getNec(WhichPrimaryDocumentPage).map(d => List(d.toString))
      case false => answers.getNec(DoYouHaveTwoSecondaryDocumentsPage).flatMap {
        case true  => answers.getNec(WhichAlternativeDocumentsPage).map(_.map(_.toString).toList)
        case false => Right(List.empty)
      }
    }
  }

  private[audit] final case class Name(title: Option[String], firstName: String, middleNames: Option[String], lastName: String)
  object Name {
    implicit lazy val formats: Format[Name] = Json.format
  }

  private[audit] final case class Names(currentName: Name, previousNames: List[Name])
  object Names {
    implicit lazy val formats: Format[Names] = Json.format
  }

  private[audit] final case class Address(line1: String, line2: Option[String], line3: Option[String], postcode: Option[String], country: Option[Country])
  object Address {
    implicit lazy val formats: Format[Address] = Json.format
  }

  private[audit] final case class PreviousAddress(line1: String, line2: Option[String], line3: Option[String], postcode: Option[String], country: Option[Country], from: LocalDate, to: LocalDate)
  object PreviousAddress {
    implicit lazy val formats: Format[PreviousAddress] = Json.format
  }

  private[audit] final case class Addresses(currentAddress: Address, previousAddresses: List[PreviousAddress])
  object Addresses {
    implicit lazy val formats: Format[Addresses] = Json.format
  }

  private[audit] final case class Relationship(relationshipType: String, startDate: LocalDate)
  object Relationship {
    implicit lazy val formats: Format[Relationship] = Json.format
  }

  private[audit] final case class PreviousRelationship(relationshipType: String, startDate: LocalDate, endDate: LocalDate, reasonForEnding: String)
  object PreviousRelationship {
    implicit lazy val formats: Format[PreviousRelationship] = Json.format
  }

  private[audit] final case class Relationships(currentRelationship: Option[Relationship], previousRelationships: List[PreviousRelationship])
  object Relationships {
    implicit lazy val formats: Format[Relationships] = Json.format
  }

  private[audit] final case class Benefits(claimedChildBenefit: Boolean, childBenefitNumber: Option[String], otherBenefits: Option[String])
  object Benefits {
    implicit lazy val formats: Format[Benefits] = Json.format
  }

  private[audit] final case class EmployerAddress(line1: String, line2: Option[String], line3: Option[String], postcode: String)
  object EmployerAddress {
    implicit lazy val formats: Format[EmployerAddress] = Json.format
  }

  private[audit] final case class Employer(name: String, address: EmployerAddress, startDate: LocalDate, endDate: Option[LocalDate])
  object Employer {
    implicit lazy val formats: Format[Employer] = Json.format
  }

  implicit lazy val writes: Format[DownloadAuditEvent] = Json.format
}
