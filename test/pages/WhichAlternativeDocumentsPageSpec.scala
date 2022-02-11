package pages

import models.AlternativeDocuments
import pages.behaviours.PageBehaviours

class WhichAlternativeDocumentsPageSpec extends PageBehaviours {

  "WhichAlternativeDocumentsPage" - {

    beRetrievable[Set[AlternativeDocuments]](WhichAlternativeDocumentsPage)

    beSettable[Set[AlternativeDocuments]](WhichAlternativeDocumentsPage)

    beRemovable[Set[AlternativeDocuments]](WhichAlternativeDocumentsPage)
  }
}
