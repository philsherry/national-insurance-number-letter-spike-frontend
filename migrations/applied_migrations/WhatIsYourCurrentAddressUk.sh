#!/bin/bash

echo ""
echo "Applying migration WhatIsYourCurrentAddressUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourCurrentAddressUk                        controllers.WhatIsYourCurrentAddressUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourCurrentAddressUk                        controllers.WhatIsYourCurrentAddressUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourCurrentAddressUk                  controllers.WhatIsYourCurrentAddressUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourCurrentAddressUk                  controllers.WhatIsYourCurrentAddressUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.title = whatIsYourCurrentAddressUk" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.heading = whatIsYourCurrentAddressUk" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.addressLine2 = addressLine2" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.checkYourAnswersLabel = WhatIsYourCurrentAddressUk" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.error.addressLine2.required = Enter addressLine2" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.error.addressLine2.length = addressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressUk.addressLine2.change.hidden = addressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressUkUserAnswersEntry: Arbitrary[(WhatIsYourCurrentAddressUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourCurrentAddressUkPage.type]";\
    print "        value <- arbitrary[WhatIsYourCurrentAddressUk].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressUkPage: Arbitrary[WhatIsYourCurrentAddressUkPage.type] =";\
    print "    Arbitrary(WhatIsYourCurrentAddressUkPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressUk: Arbitrary[WhatIsYourCurrentAddressUk] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        addressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourCurrentAddressUk(addressLine1, addressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourCurrentAddressUkPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourCurrentAddressUk completed"
