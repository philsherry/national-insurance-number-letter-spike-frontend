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

trait ModelGenerators {

  implicit lazy val arbitraryPreviousMarriageOrPartnershipDetails: Arbitrary[PreviousMarriageOrPartnershipDetails] =
    Arbitrary {
      for {
        startDate <- arbitrary[String]
        endDate <- arbitrary[String]
      } yield PreviousMarriageOrPartnershipDetails(startDate, endDate)
    }

  implicit lazy val arbitraryWhatIsYourPreviousAddressUk: Arbitrary[WhatIsYourPreviousAddressUk] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        adressLine2 <- arbitrary[String]
      } yield WhatIsYourPreviousAddressUk(addressLine1, adressLine2)
    }

  implicit lazy val arbitraryWhatIsYourPreviousAddressInternational: Arbitrary[WhatIsYourPreviousAddressInternational] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        adressLine2 <- arbitrary[String]
      } yield WhatIsYourPreviousAddressInternational(addressLine1, adressLine2)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressUk: Arbitrary[WhatIsYourCurrentAddressUk] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[String]
      } yield WhatIsYourCurrentAddressUk(addressLine1, addressLine2)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressInternational: Arbitrary[WhatIsYourCurrentAddressInternational] =
    Arbitrary {
      for {
        addressLine1 <- arbitrary[String]
        addressLine2 <- arbitrary[String]
      } yield WhatIsYourCurrentAddressInternational(addressLine1, addressLine2)
    }

  implicit lazy val arbitraryWhatIsYourPreviousName: Arbitrary[WhatIsYourPreviousName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        middleNames <- arbitrary[String]
      } yield WhatIsYourPreviousName(firstName, middleNames)
    }

  implicit lazy val arbitraryWhatIsYourName: Arbitrary[WhatIsYourName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        middleNames <- arbitrary[String]
      } yield WhatIsYourName(firstName, middleNames)
    }
}
