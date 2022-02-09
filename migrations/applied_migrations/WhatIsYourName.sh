#!/bin/bash

echo ""
echo "Applying migration WhatIsYourName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourName                        controllers.WhatIsYourNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourName                        controllers.WhatIsYourNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourName                  controllers.WhatIsYourNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourName                  controllers.WhatIsYourNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourName.title = whatIsYourName" >> ../conf/messages.en
echo "whatIsYourName.heading = whatIsYourName" >> ../conf/messages.en
echo "whatIsYourName.firstName = firstName" >> ../conf/messages.en
echo "whatIsYourName.middleNames = middleNames" >> ../conf/messages.en
echo "whatIsYourName.checkYourAnswersLabel = WhatIsYourName" >> ../conf/messages.en
echo "whatIsYourName.error.firstName.required = Enter firstName" >> ../conf/messages.en
echo "whatIsYourName.error.middleNames.required = Enter middleNames" >> ../conf/messages.en
echo "whatIsYourName.error.firstName.length = firstName must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourName.error.middleNames.length = middleNames must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourName.firstName.change.hidden = firstName" >> ../conf/messages.en
echo "whatIsYourName.middleNames.change.hidden = middleNames" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourNameUserAnswersEntry: Arbitrary[(WhatIsYourNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourNamePage.type]";\
    print "        value <- arbitrary[WhatIsYourName].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourNamePage: Arbitrary[WhatIsYourNamePage.type] =";\
    print "    Arbitrary(WhatIsYourNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourName: Arbitrary[WhatIsYourName] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        firstName <- arbitrary[String]";\
    print "        middleNames <- arbitrary[String]";\
    print "      } yield WhatIsYourName(firstName, middleNames)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourName completed"
