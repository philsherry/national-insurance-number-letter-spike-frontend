#!/bin/bash

echo ""
echo "Applying migration WhatIsYourEmployersName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourEmployersName                        controllers.WhatIsYourEmployersNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourEmployersName                        controllers.WhatIsYourEmployersNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourEmployersName                  controllers.WhatIsYourEmployersNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourEmployersName                  controllers.WhatIsYourEmployersNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourEmployersName.title = whatIsYourEmployersName" >> ../conf/messages.en
echo "whatIsYourEmployersName.heading = whatIsYourEmployersName" >> ../conf/messages.en
echo "whatIsYourEmployersName.checkYourAnswersLabel = whatIsYourEmployersName" >> ../conf/messages.en
echo "whatIsYourEmployersName.error.required = Enter whatIsYourEmployersName" >> ../conf/messages.en
echo "whatIsYourEmployersName.error.length = WhatIsYourEmployersName must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourEmployersName.change.hidden = WhatIsYourEmployersName" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmployersNameUserAnswersEntry: Arbitrary[(WhatIsYourEmployersNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourEmployersNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmployersNamePage: Arbitrary[WhatIsYourEmployersNamePage.type] =";\
    print "    Arbitrary(WhatIsYourEmployersNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourEmployersNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourEmployersName completed"
