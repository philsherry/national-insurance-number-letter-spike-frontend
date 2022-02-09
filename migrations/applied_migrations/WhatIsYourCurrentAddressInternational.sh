#!/bin/bash

echo ""
echo "Applying migration WhatIsYourCurrentAddressInternational"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourCurrentAddressInternational                        controllers.WhatIsYourCurrentAddressInternationalController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourCurrentAddressInternational                        controllers.WhatIsYourCurrentAddressInternationalController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourCurrentAddressInternational                  controllers.WhatIsYourCurrentAddressInternationalController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourCurrentAddressInternational                  controllers.WhatIsYourCurrentAddressInternationalController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.title = whatIsYourCurrentAddressInternational" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.heading = whatIsYourCurrentAddressInternational" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.addressLine2 = addressLine2" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.checkYourAnswersLabel = WhatIsYourCurrentAddressInternational" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.error.addressLine2.required = Enter addressLine2" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.error.addressLine2.length = addressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourCurrentAddressInternational.addressLine2.change.hidden = addressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressInternationalUserAnswersEntry: Arbitrary[(WhatIsYourCurrentAddressInternationalPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourCurrentAddressInternationalPage.type]";\
    print "        value <- arbitrary[WhatIsYourCurrentAddressInternational].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressInternationalPage: Arbitrary[WhatIsYourCurrentAddressInternationalPage.type] =";\
    print "    Arbitrary(WhatIsYourCurrentAddressInternationalPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourCurrentAddressInternational: Arbitrary[WhatIsYourCurrentAddressInternational] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        addressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourCurrentAddressInternational(addressLine1, addressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourCurrentAddressInternationalPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourCurrentAddressInternational completed"
