package pages

import pages.behaviours.PageBehaviours

class AreYouSureYouWantToRemovePreviousNamePageSpec extends PageBehaviours {

  "AreYouSureYouWantToRemovePreviousNamePage" - {

    beRetrievable[Boolean](AreYouSureYouWantToRemovePreviousNamePage)

    beSettable[Boolean](AreYouSureYouWantToRemovePreviousNamePage)

    beRemovable[Boolean](AreYouSureYouWantToRemovePreviousNamePage)
  }
}
