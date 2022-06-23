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

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

  def from(model: JourneyModel): PrintModel =
    PrintModel(
      name = model.currentName,
      previousNames = model.previousNames,
      dob = formatter.format(model.dateOfBirth),
      gender = model.gender,
      currentAddress = model.currentAddress.lines,
      previousAddresses = model.previousAddresses.map(a => PreviousAddressPrintModel(a.lines, formatter.format(a.from), formatter.format(a.to))),
      returningFromLivingAbroad = model.returningFromLivingAbroad,
      telephoneNumber = model.telephoneNumber,
      nino = model.nationalInsuranceNumber,
      currentRelationshipType = model.currentRelationship.map(_.relationshipType.toString),
      currentRelationshipDate = model.currentRelationship.map(d => formatter.format(d.from)),
      previousRelationships = model.previousRelationships.map(r => PreviousMarriageOrPartnershipPrintModel(r.relationshipType.toString, formatter.format(r.from), formatter.format(r.to), r.endReason)),
      claimedChildBenefit = model.claimedChildBenefit,
      childBenefitNumber = model.childBenefitNumber,
      otherBenefits = model.otherBenefits,
      currentEmployers = model.employers.filter(_.endDate.isEmpty).map(e => EmployerPrintModel(e.name, e.address.lines, formatter.format(e.startDate), e.endDate.map(formatter.format))),
      previousEmployers = model.employers.filter(_.endDate.nonEmpty).map(e => EmployerPrintModel(e.name, e.address.lines, formatter.format(e.startDate), e.endDate.map(formatter.format))),
      primaryDocument = model.primaryDocument.map(key => s"whichPrimaryDocument.$key"),
      secondaryDocuments = if (model.alternativeDocuments.nonEmpty) Some(model.alternativeDocuments.map(key => s"whichAlternativeDocuments.$key")) else None
    )
}
