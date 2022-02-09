#!/bin/bash

echo ""
echo "Applying migration WhatIsYourPreviousAddressInternational"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourPreviousAddressInternational                        controllers.WhatIsYourPreviousAddressInternationalController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourPreviousAddressInternational                        controllers.WhatIsYourPreviousAddressInternationalController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourPreviousAddressInternational                  controllers.WhatIsYourPreviousAddressInternationalController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourPreviousAddressInternational                  controllers.WhatIsYourPreviousAddressInternationalController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.title = whatIsYourPreviousAddressInternational" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.heading = whatIsYourPreviousAddressInternational" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.adressLine2 = adressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.checkYourAnswersLabel = WhatIsYourPreviousAddressInternational" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.error.adressLine2.required = Enter adressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.error.adressLine2.length = adressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousAddressInternational.adressLine2.change.hidden = adressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressInternationalUserAnswersEntry: Arbitrary[(WhatIsYourPreviousAddressInternationalPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourPreviousAddressInternationalPage.type]";\
    print "        value <- arbitrary[WhatIsYourPreviousAddressInternational].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressInternationalPage: Arbitrary[WhatIsYourPreviousAddressInternationalPage.type] =";\
    print "    Arbitrary(WhatIsYourPreviousAddressInternationalPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousAddressInternational: Arbitrary[WhatIsYourPreviousAddressInternational] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        adressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourPreviousAddressInternational(addressLine1, adressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourPreviousAddressInternationalPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourPreviousAddressInternational completed"
