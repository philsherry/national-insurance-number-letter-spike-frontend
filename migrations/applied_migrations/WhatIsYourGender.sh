#!/bin/bash

echo ""
echo "Applying migration WhatIsYourGender"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourGender                        controllers.WhatIsYourGenderController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourGender                        controllers.WhatIsYourGenderController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourGender                  controllers.WhatIsYourGenderController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourGender                  controllers.WhatIsYourGenderController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "whatIsYourGender.title = What is your gender?" >> ../conf/messages
echo "whatIsYourGender.heading = What is your gender?" >> ../conf/messages
echo "whatIsYourGender.male = Male" >> ../conf/messages
echo "whatIsYourGender.female = Female" >> ../conf/messages
echo "whatIsYourGender.checkYourAnswersLabel = What is your gender?" >> ../conf/messages
echo "whatIsYourGender.error.required = Select whatIsYourGender" >> ../conf/messages
echo "whatIsYourGender.change.hidden = WhatIsYourGender" >> ../conf/messages

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourGenderUserAnswersEntry: Arbitrary[(WhatIsYourGenderPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourGenderPage.type]";\
    print "        value <- arbitrary[WhatIsYourGender].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourGenderPage: Arbitrary[WhatIsYourGenderPage.type] =";\
    print "    Arbitrary(WhatIsYourGenderPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourGender: Arbitrary[WhatIsYourGender] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(WhatIsYourGender.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourGenderPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourGender completed"
