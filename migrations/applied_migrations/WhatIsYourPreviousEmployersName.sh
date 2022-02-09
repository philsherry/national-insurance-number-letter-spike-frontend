#!/bin/bash

echo ""
echo "Applying migration WhatIsYourPreviousEmployersName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourPreviousEmployersName                        controllers.WhatIsYourPreviousEmployersNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourPreviousEmployersName                        controllers.WhatIsYourPreviousEmployersNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourPreviousEmployersName                  controllers.WhatIsYourPreviousEmployersNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourPreviousEmployersName                  controllers.WhatIsYourPreviousEmployersNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.title = whatIsYourPreviousEmployersName" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.heading = whatIsYourPreviousEmployersName" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.checkYourAnswersLabel = whatIsYourPreviousEmployersName" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.error.required = Enter whatIsYourPreviousEmployersName" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.error.length = WhatIsYourPreviousEmployersName must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersName.change.hidden = WhatIsYourPreviousEmployersName" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousEmployersNameUserAnswersEntry: Arbitrary[(WhatIsYourPreviousEmployersNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourPreviousEmployersNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousEmployersNamePage: Arbitrary[WhatIsYourPreviousEmployersNamePage.type] =";\
    print "    Arbitrary(WhatIsYourPreviousEmployersNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourPreviousEmployersNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourPreviousEmployersName completed"
