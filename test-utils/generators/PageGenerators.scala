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

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryWhatIsYourPreviousAddressUkPage: Arbitrary[WhatIsYourPreviousAddressUkPage.type] =
    Arbitrary(WhatIsYourPreviousAddressUkPage)

  implicit lazy val arbitraryWhatIsYourPreviousAddressInternationalPage: Arbitrary[WhatIsYourPreviousAddressInternationalPage.type] =
    Arbitrary(WhatIsYourPreviousAddressInternationalPage)

  implicit lazy val arbitraryIsYourPreviousAddressInUkPage: Arbitrary[IsYourPreviousAddressInUkPage.type] =
    Arbitrary(IsYourPreviousAddressInUkPage)

  implicit lazy val arbitraryDoYouHaveAnyPreviousAddressesPage: Arbitrary[DoYouHaveAnyPreviousAddressesPage.type] =
    Arbitrary(DoYouHaveAnyPreviousAddressesPage)

  implicit lazy val arbitraryWhatIsYourCurrentAddressUkPage: Arbitrary[WhatIsYourCurrentAddressUkPage.type] =
    Arbitrary(WhatIsYourCurrentAddressUkPage)

  implicit lazy val arbitraryWhatIsYourCurrentAddressInternationalPage: Arbitrary[WhatIsYourCurrentAddressInternationalPage.type] =
    Arbitrary(WhatIsYourCurrentAddressInternationalPage)

  implicit lazy val arbitraryIsYourCurrentAddressInUkPage: Arbitrary[IsYourCurrentAddressInUkPage.type] =
    Arbitrary(IsYourCurrentAddressInUkPage)

  implicit lazy val arbitraryWhatIsYourDateOfBirthPage: Arbitrary[WhatIsYourDateOfBirthPage.type] =
    Arbitrary(WhatIsYourDateOfBirthPage)

  implicit lazy val arbitraryWhatIsYourPreviousNamePage: Arbitrary[WhatIsYourPreviousNamePage.type] =
    Arbitrary(WhatIsYourPreviousNamePage)

  implicit lazy val arbitraryDoYouHaveAPreviousNamePage: Arbitrary[DoYouHaveAPreviousNamePage.type] =
    Arbitrary(DoYouHaveAPreviousNamePage)

  implicit lazy val arbitraryWhatIsYourNamePage: Arbitrary[WhatIsYourNamePage.type] =
    Arbitrary(WhatIsYourNamePage)
}
