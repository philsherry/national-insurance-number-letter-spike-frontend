#!/bin/bash

echo ""
echo "Applying migration WhatIsYourPreviousName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourPreviousName                        controllers.WhatIsYourPreviousNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourPreviousName                        controllers.WhatIsYourPreviousNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourPreviousName                  controllers.WhatIsYourPreviousNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourPreviousName                  controllers.WhatIsYourPreviousNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourPreviousName.title = whatIsYourPreviousName" >> ../conf/messages.en
echo "whatIsYourPreviousName.heading = whatIsYourPreviousName" >> ../conf/messages.en
echo "whatIsYourPreviousName.firstName = firstName" >> ../conf/messages.en
echo "whatIsYourPreviousName.middleNames = middleNames" >> ../conf/messages.en
echo "whatIsYourPreviousName.checkYourAnswersLabel = WhatIsYourPreviousName" >> ../conf/messages.en
echo "whatIsYourPreviousName.error.firstName.required = Enter firstName" >> ../conf/messages.en
echo "whatIsYourPreviousName.error.middleNames.required = Enter middleNames" >> ../conf/messages.en
echo "whatIsYourPreviousName.error.firstName.length = firstName must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousName.error.middleNames.length = middleNames must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousName.firstName.change.hidden = firstName" >> ../conf/messages.en
echo "whatIsYourPreviousName.middleNames.change.hidden = middleNames" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousNameUserAnswersEntry: Arbitrary[(WhatIsYourPreviousNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourPreviousNamePage.type]";\
    print "        value <- arbitrary[WhatIsYourPreviousName].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousNamePage: Arbitrary[WhatIsYourPreviousNamePage.type] =";\
    print "    Arbitrary(WhatIsYourPreviousNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousName: Arbitrary[WhatIsYourPreviousName] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        firstName <- arbitrary[String]";\
    print "        middleNames <- arbitrary[String]";\
    print "      } yield WhatIsYourPreviousName(firstName, middleNames)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourPreviousNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourPreviousName completed"
