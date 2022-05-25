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
import models.{Country, JourneyModel, WhatIsYourGender}
import play.api.libs.json.{Format, Json}

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

  def from(model: JourneyModel): DownloadAuditEvent =
    DownloadAuditEvent(
      names = Names(
        currentName = Name(model.currentName.title, model.currentName.firstName, model.currentName.middleNames, model.currentName.lastName),
        previousNames = model.previousNames.map { name =>
          Name(None, name.firstName, name.middleNames, name.lastName)
        }
      ),
      dateOfBirth = model.dateOfBirth,
      gender = model.gender,
      addresses = Addresses(
        currentAddress = Address(
          model.currentAddress.addressLine1,
          model.currentAddress.addressLine2,
          model.currentAddress.addressLine3,
          model.currentAddress.postcodeOption,
          model.currentAddress.countryOption
        ),
        previousAddresses = model.previousAddresses.map { address =>
          PreviousAddress(
            address.addressLine1,
            address.addressLine2,
            address.addressLine3,
            address.postcodeOption,
            address.countryOption,
            address.from,
            address.to
          )
        }
      ),
      returningFromLivingAbroad = model.returningFromLivingAbroad,
      telephoneNumber = model.telephoneNumber,
      nationalInsuranceNumber = model.nationalInsuranceNumber,
      relationships = Relationships(
        currentRelationship = model.currentRelationship.map(relationship => Relationship(relationship.relationshipType.toString, relationship.from)),
        previousRelationships = model.previousRelationships.map { relationship =>
          PreviousRelationship(relationship.relationshipType.toString, relationship.from, relationship.to, relationship.endReason)
        }
      ),
      benefits = Benefits(
        claimedChildBenefit = model.claimedChildBenefit,
        childBenefitNumber = model.childBenefitNumber,
        otherBenefits = model.otherBenefits
      ),
      employers = model.employers.map { employer =>
        Employer(
          employer.name,
          EmployerAddress(employer.address.addressLine1, employer.address.addressLine2, employer.address.addressLine3, employer.address.postcode),
          employer.startDate,
          employer.endDate
        )
      },
      documents = model.primaryDocument.toList ++ model.alternativeDocuments
    )

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
