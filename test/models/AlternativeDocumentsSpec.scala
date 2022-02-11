package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class AlternativeDocumentsSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "AlternativeDocuments" - {

    "must deserialise valid values" in {

      val gen = arbitrary[AlternativeDocuments]

      forAll(gen) {
        whichAlternativeDocuments =>

          JsString(whichAlternativeDocuments.toString).validate[AlternativeDocuments].asOpt.value mustEqual whichAlternativeDocuments
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!AlternativeDocuments.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[AlternativeDocuments] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = arbitrary[AlternativeDocuments]

      forAll(gen) {
        whichAlternativeDocuments =>

          Json.toJson(whichAlternativeDocuments) mustEqual JsString(whichAlternativeDocuments.toString)
      }
    }
  }
}
