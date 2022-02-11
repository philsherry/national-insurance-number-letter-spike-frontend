package forms

import forms.behaviours.CheckboxFieldBehaviours
import models.AlternativeDocuments
import play.api.data.FormError

class WhichAlternativeDocumentsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new WhichAlternativeDocumentsFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "whichAlternativeDocuments.error.required"

    behave like checkboxField[AlternativeDocuments](
      form,
      fieldName,
      validValues  = AlternativeDocuments.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
