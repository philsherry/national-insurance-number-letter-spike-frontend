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

import java.time.LocalDate

abstract class PreviousAddress extends Product with Serializable {

  def addressLine1: String
  def addressLine2: Option[String]
  def addressLine3: Option[String]

  def postcodeOption: Option[String]
  def countryOption: Option[Country]

  def lines: List[String]
  def from: LocalDate
  def to: LocalDate
}
