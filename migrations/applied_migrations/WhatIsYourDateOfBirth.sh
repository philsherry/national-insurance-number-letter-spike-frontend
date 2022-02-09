#!/bin/bash

echo ""
echo "Applying migration WhatIsYourDateOfBirth"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourDateOfBirth                  controllers.WhatIsYourDateOfBirthController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourDateOfBirth                  controllers.WhatIsYourDateOfBirthController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourDateOfBirth                        controllers.WhatIsYourDateOfBirthController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourDateOfBirth                        controllers.WhatIsYourDateOfBirthController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.title = WhatIsYourDateOfBirth" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.heading = WhatIsYourDateOfBirth" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.checkYourAnswersLabel = WhatIsYourDateOfBirth" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.error.required.all = Enter the whatIsYourDateOfBirth" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.error.required.two = The whatIsYourDateOfBirth" must include {0} and {1} >> ../conf/messages.en
echo "whatIsYourDateOfBirth.error.required = The whatIsYourDateOfBirth must include {0}" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.error.invalid = Enter a real WhatIsYourDateOfBirth" >> ../conf/messages.en
echo "whatIsYourDateOfBirth.change.hidden = WhatIsYourDateOfBirth" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourDateOfBirthUserAnswersEntry: Arbitrary[(WhatIsYourDateOfBirthPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourDateOfBirthPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourDateOfBirthPage: Arbitrary[WhatIsYourDateOfBirthPage.type] =";\
    print "    Arbitrary(WhatIsYourDateOfBirthPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourDateOfBirthPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourDateOfBirth completed"
