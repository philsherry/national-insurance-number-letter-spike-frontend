#!/bin/bash

echo ""
echo "Applying migration WhatIsYourPreviousAddressUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourPreviousAddressUk                        controllers.WhatIsYourPreviousAddressUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourPreviousAddressUk                        controllers.WhatIsYourPreviousAddressUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourPreviousAddressUk                  controllers.WhatIsYourPreviousAddressUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourPreviousAddressUk                  controllers.WhatIsYourPreviousAddressUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.title = whatIsYourPreviousAddressUk" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.heading = whatIsYourPreviousAddressUk" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.adressLine2 = adressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.checkYourAnswersLabel = WhatIsYourPreviousAddressUk" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.error.adressLine2.required = Enter adressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.error.adressLine2.length = adressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressUk.adressLine2.change.hidden = adressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressUkUserAnswersEntry: Arbitrary[(WhatIsYourPreviousAddressUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourPreviousAddressUkPage.type]";\
    print "        value <- arbitrary[WhatIsYourPreviousAddressUk].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressUkPage: Arbitrary[WhatIsYourPreviousAddressUkPage.type] =";\
    print "    Arbitrary(WhatIsYourPreviousAddressUkPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressUk: Arbitrary[WhatIsYourPreviousAddressUk] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        adressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourPreviousAddressUk(addressLine1, adressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourPreviousAddressUkPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourPreviousAddressUk completed"
