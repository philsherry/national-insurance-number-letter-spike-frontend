#!/bin/bash

echo ""
echo "Applying migration WhatIsYourTelephoneNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourTelephoneNumber                        controllers.WhatIsYourTelephoneNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourTelephoneNumber                        controllers.WhatIsYourTelephoneNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourTelephoneNumber                  controllers.WhatIsYourTelephoneNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourTelephoneNumber                  controllers.WhatIsYourTelephoneNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.title = whatIsYourTelephoneNumber" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.heading = whatIsYourTelephoneNumber" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.checkYourAnswersLabel = whatIsYourTelephoneNumber" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.error.required = Enter whatIsYourTelephoneNumber" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.error.length = WhatIsYourTelephoneNumber must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourTelephoneNumber.change.hidden = WhatIsYourTelephoneNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourTelephoneNumberUserAnswersEntry: Arbitrary[(WhatIsYourTelephoneNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourTelephoneNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourTelephoneNumberPage: Arbitrary[WhatIsYourTelephoneNumberPage.type] =";\
    print "    Arbitrary(WhatIsYourTelephoneNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourTelephoneNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourTelephoneNumber completed"
