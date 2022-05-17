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

import audit.DownloadAuditEvent._
import models.{Index, UserAnswers, WhatIsYourGender}
import pages._
import cats.implicits._
import play.api.libs.json.{Json, Format}

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

  def apply(answers: UserAnswers): Option[DownloadAuditEvent] = for {
    names                     <- getNames(answers)
    dob                       <- answers.get(WhatIsYourDateOfBirthPage)
    gender                    <- answers.get(WhatIsYourGenderPage)
    addresses                 <- getAddresses(answers)
    returningFromLivingAbroad <- answers.get(AreYouReturningFromLivingAbroadPage)
    telephoneNumber           <- answers.get(WhatIsYourTelephoneNumberPage)
    nino                      =  answers.get(WhatIsYourNationalInsuranceNumberPage).map(_.value)
    relationships             <- getRelationships(answers)
    benefits                  <- getBenefits(answers)
    employers                 <- getEmployers(answers)
    documents                 <- getDocuments(answers)
  } yield DownloadAuditEvent(
    names, dob, gender, addresses, returningFromLivingAbroad, telephoneNumber,
    nino, relationships, benefits, employers, documents
  )

  private def getNames(answers: UserAnswers): Option[Names] = for {
    current  <- answers.get(WhatIsYourNamePage)
    previous <-  answers.get(PreviousNamesQuery).getOrElse(Seq.empty).indices.toList.traverse(i => answers.get(WhatIsYourPreviousNamePage(Index(i))))
  } yield Names(
    currentName = Name(current.title, current.firstName, current.middleNames, current.lastName),
    previousNames = previous.map { previous =>
      Name(None, previous.firstName, previous.middleNames, previous.lastName)
    }
  )

  private def getAddresses(answers: UserAnswers): Option[Addresses] = for {
    current  <- getCurrentAddress(answers)
    previous <- getPreviousAddresses(answers)
  } yield Addresses(current, previous)

  private def getCurrentAddress(answers: UserAnswers): Option[Address] =
    answers.get(IsYourCurrentAddressInUkPage).flatMap {
      case true  => answers.get(WhatIsYourCurrentAddressUkPage)
        .map(a => Address(a.addressLine1, a.addressLine2, a.addressLine3, Some(a.postcode), None))
      case false => answers.get(WhatIsYourCurrentAddressInternationalPage)
        .map(a => Address(a.addressLine1, a.addressLine2, a.addressLine3, a.postcode, Some(a.country)))
    }

  private def getPreviousAddresses(answers: UserAnswers): Option[List[PreviousAddress]] =
    answers.get(PreviousAddressesQuery).getOrElse(Seq.empty).indices.toList.traverse { i =>
      answers.get(IsYourPreviousAddressInUkPage(Index(i))).flatMap {
        case true  => answers.get(WhatIsYourPreviousAddressUkPage(Index(i)))
          .map(a => PreviousAddress(a.addressLine1, a.addressLine2, a.addressLine3, Some(a.postcode), None, a.from, a.to))
        case false => answers.get(WhatIsYourPreviousAddressInternationalPage(Index(i)))
          .map(a => PreviousAddress(a.addressLine1, a.addressLine2, a.addressLine3, a.postcode, Some(a.country), a.from, a.to))
      }
    }

  private def getRelationships(answers: UserAnswers): Option[Relationships] = for {
    previous <- getPreviousRelationships(answers)
    current  =  getCurrentRelationship(answers)
  } yield Relationships(current, previous)

  private def getCurrentRelationship(answers: UserAnswers): Option[Relationship] = for {
    relationshipType <- answers.get(CurrentRelationshipTypePage)
    date             <- answers.get(WhenDidYouGetMarriedPage)
  } yield Relationship(relationshipType.toString, date)

  private def getPreviousRelationships(answers: UserAnswers): Option[List[PreviousRelationship]] =
    answers.get(PreviousRelationshipsQuery).getOrElse(Seq.empty).indices.toList.traverse { i =>
      answers.get(PreviousMarriageOrPartnershipDetailsPage(Index(i))).map { details =>
        PreviousRelationship(details.startDate, details.endDate, details.endingReason)
      }
    }

  private def getBenefits(answers: UserAnswers): Option[Benefits] = for {
    claimedChildBenefit <- answers.get(HaveYouEverClaimedChildBenefitPage)
    childBenefitNumber  =  answers.get(WhatIsYourChildBenefitNumberPage)
    otherBenefits       =  answers.get(WhatOtherUkBenefitsHaveYouReceivedPage)
  } yield Benefits(claimedChildBenefit, childBenefitNumber, otherBenefits)

  private def getEmployers(answers: UserAnswers): Option[List[Employer]] =
    answers.get(EmployersQuery).getOrElse(Seq.empty).indices.toList.traverse { i =>
      for {
        name    <- answers.get(WhatIsYourEmployersNamePage(Index(i)))
        address <- answers.get(WhatIsYourEmployersAddressPage(Index(i)))
        from    <- answers.get(WhenDidYouStartWorkingForEmployerPage(Index(i)))
        to      =  answers.get(WhenDidYouStopWorkingForEmployerPage(Index(i)))
      } yield Employer(name, EmployerAddress(address.addressLine1, address.addressLine2, address.addressLine3, address.postcode), from, to)
    }

  private def getDocuments(answers: UserAnswers): Option[List[String]] =
    answers.get(WhichPrimaryDocumentPage).map(d => List(d.toString)) orElse
      answers.get(WhichAlternativeDocumentsPage).map(d => d.map(_.toString).toList)

  private[audit] final case class Name(title: Option[String], firstName: String, middleNames: Option[String], lastName: String)
  object Name {
    implicit lazy val formats: Format[Name] = Json.format
  }

  private[audit] final case class Names(currentName: Name, previousNames: List[Name])
  object Names {
    implicit lazy val formats: Format[Names] = Json.format
  }

  private[audit] final case class Address(line1: String, line2: Option[String], line3: Option[String], postcode: Option[String], country: Option[String])
  object Address {
    implicit lazy val formats: Format[Address] = Json.format
  }

  private[audit] final case class PreviousAddress(line1: String, line2: Option[String], line3: Option[String], postcode: Option[String], country: Option[String], from: LocalDate, to: LocalDate)
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

  private[audit] final case class PreviousRelationship(startDate: LocalDate, endDate: LocalDate, reasonForEnding: String)
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
