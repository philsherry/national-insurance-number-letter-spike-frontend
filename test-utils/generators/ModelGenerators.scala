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

package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino

import java.time.LocalDate

trait ModelGenerators { this: Generators =>

  implicit lazy val arbitraryAlternativeDocuments: Arbitrary[AlternativeDocuments] =
    Arbitrary {
      Gen.oneOf(AlternativeDocuments.values)
    }

  implicit lazy val arbitraryWhichPrimaryDocument: Arbitrary[PrimaryDocument] =
    Arbitrary {
      Gen.oneOf(PrimaryDocument.values.toSeq)
    }

  implicit lazy val arbitraryWhatIsYourPreviousEmployersAddress: Arbitrary[EmployersAddress] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[Option[String]]
        addressLine3 <- arbitrary[Option[String]]
        postcode     <- arbitrary[String]
      } yield EmployersAddress(addressLine1, addressLine2, addressLine3, postcode)
    }

  implicit lazy val arbitraryPreviousMarriageOrPartnershipDetails: Arbitrary[PreviousMarriageOrPartnershipDetails] =
    Arbitrary {
      for {
        startDate    <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
        endDate      <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
        endingReason <- arbitrary[String]
      } yield PreviousMarriageOrPartnershipDetails(startDate, endDate, endingReason)
    }

  implicit lazy val arbitraryWhatIsYourPreviousAddressUk: Arbitrary[PreviousAddressUk] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[Option[String]]
        addressLine3 <- arbitrary[Option[String]]
        postcode     <- arbitrary[String]
        from         <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
        to           <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
      } yield PreviousAddressUk(addressLine1, addressLine2, addressLine3, postcode, from, to)
    }

  implicit lazy val arbitraryWhatIsYourPreviousAddressInternational: Arbitrary[PreviousAddressInternational] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[Option[String]]
        addressLine3 <- arbitrary[Option[String]]
        country      <- arbitrary[String]
        from         <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
        to           <- datesBetween(LocalDate.of(2000, 1, 1), LocalDate.now)
      } yield PreviousAddressInternational(addressLine1, addressLine2, addressLine3, country, from, to)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressUk: Arbitrary[CurrentAddressUk] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[Option[String]]
        addressLine3 <- arbitrary[Option[String]]
        postcode     <- arbitrary[String]
      } yield CurrentAddressUk(addressLine1, addressLine2, addressLine3, postcode)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressInternational: Arbitrary[CurrentAddressInternational] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[Option[String]]
        addressLine3 <- arbitrary[Option[String]]
        country      <- arbitrary[String]
      } yield CurrentAddressInternational(addressLine1, addressLine2, addressLine3, country)
    }

  implicit lazy val arbitraryWhatIsYourPreviousName: Arbitrary[WhatIsYourPreviousName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        middleNames <- arbitrary[Option[String]]
        lastName <- arbitrary[String]
      } yield WhatIsYourPreviousName(firstName, middleNames, lastName)
    }

  implicit lazy val arbitraryWhatIsYourName: Arbitrary[WhatIsYourName] =
    Arbitrary {
      for {
        title <- arbitrary[Option[String]]
        firstName <- arbitrary[String]
        middleNames <- arbitrary[Option[String]]
        lastName <- arbitrary[String]
      } yield WhatIsYourName(title, firstName, middleNames, lastName)
    }

  implicit lazy val arbitraryNino: Arbitrary[Nino] = Arbitrary {
    for {
      firstChar <- Gen.oneOf('A', 'C', 'E', 'H', 'J', 'L', 'M', 'O', 'P', 'R', 'S', 'W', 'X', 'Y').map(_.toString)
      secondChar <- Gen.oneOf('A', 'B', 'C', 'E', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z').map(_.toString)
      digits <- Gen.listOfN(6, Gen.numChar)
      lastChar <- Gen.oneOf('A', 'B', 'C', 'D')
    } yield Nino(firstChar ++ secondChar ++ digits :+ lastChar)
  }

}
