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
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryWhatIsYourPreviousAddressUkUserAnswersEntry: Arbitrary[(WhatIsYourPreviousAddressUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourPreviousAddressUkPage.type]
        value <- arbitrary[WhatIsYourPreviousAddressUk].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourPreviousAddressInternationalUserAnswersEntry: Arbitrary[(WhatIsYourPreviousAddressInternationalPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourPreviousAddressInternationalPage.type]
        value <- arbitrary[WhatIsYourPreviousAddressInternational].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIsYourPreviousAddressInUkUserAnswersEntry: Arbitrary[(IsYourPreviousAddressInUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IsYourPreviousAddressInUkPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDoYouHaveAnyPreviousAddressesUserAnswersEntry: Arbitrary[(DoYouHaveAnyPreviousAddressesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DoYouHaveAnyPreviousAddressesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressUkUserAnswersEntry: Arbitrary[(WhatIsYourCurrentAddressUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourCurrentAddressUkPage.type]
        value <- arbitrary[WhatIsYourCurrentAddressUk].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourCurrentAddressInternationalUserAnswersEntry: Arbitrary[(WhatIsYourCurrentAddressInternationalPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourCurrentAddressInternationalPage.type]
        value <- arbitrary[WhatIsYourCurrentAddressInternational].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIsYourCurrentAddressInUkUserAnswersEntry: Arbitrary[(IsYourCurrentAddressInUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IsYourCurrentAddressInUkPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourDateOfBirthUserAnswersEntry: Arbitrary[(WhatIsYourDateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourDateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourPreviousNameUserAnswersEntry: Arbitrary[(WhatIsYourPreviousNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourPreviousNamePage.type]
        value <- arbitrary[WhatIsYourPreviousName].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDoYouHaveAPreviousNameUserAnswersEntry: Arbitrary[(DoYouHaveAPreviousNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DoYouHaveAPreviousNamePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryWhatIsYourNameUserAnswersEntry: Arbitrary[(WhatIsYourNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[WhatIsYourNamePage.type]
        value <- arbitrary[WhatIsYourName].map(Json.toJson(_))
      } yield (page, value)
    }
}
