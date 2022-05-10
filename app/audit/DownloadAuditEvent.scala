package audit

import models.UserAnswers

import java.time.LocalDate

final case class DownloadAuditEvent(
                                   names: Names,
                                   dateOfBirth: LocalDate,
                                   addresses: Addresses,
                                   returningFromLivingAbroad: Boolean,
                                   telephoneNumber: String,
                                   nationalInsuranceNumber: Option[String],
                                   relationships: Relationships,
                                   benefits: Benefits,
                                   employers: List[Employer]
                              )

private[audit] final case class Name(firstName: String, middleNames: Option[String], lastName: String)
private[audit] final case class Names(currentName: Name, previousNames: List[Name])

private[audit] final case class Address(line1: String, line2: Option[String], line3: Option[String], postcode: Option[String], country: Option[String])
private[audit] final case class Addresses(currentAddress: Address, previousAddresses: List[Address])

private[audit] final case class Relationship(startDate: LocalDate)
private[audit] final case class PreviousRelationship(startDate: LocalDate, endDate: LocalDate, reasonForEnding: String)
private[audit] final case class Relationships(currentRelationship: Option[Relationship], previousRelationships: List[PreviousRelationship])

private[audit] final case class Benefits(claimedChildBenefit: Boolean, childBenefitNumber: Option[String], otherBenefits: Option[String])

private[audit] final case class EmployerAddress(line1: String, line2: Option[String], line3: Option[String], postcode: String)
private[audit] final case class Employer(name: String, address: EmployerAddress, startDate: LocalDate, endDate: Option[LocalDate])

object DownloadAuditEvent {

  def apply(userAnswers: UserAnswers): DownloadAuditEvent = {

    ???
  }

}
