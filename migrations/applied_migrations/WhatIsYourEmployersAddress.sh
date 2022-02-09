#!/bin/bash

echo ""
echo "Applying migration WhatIsYourEmployersAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourEmployersAddress                        controllers.WhatIsYourEmployersAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourEmployersAddress                        controllers.WhatIsYourEmployersAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourEmployersAddress                  controllers.WhatIsYourEmployersAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourEmployersAddress                  controllers.WhatIsYourEmployersAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.title = whatIsYourEmployersAddress" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.heading = whatIsYourEmployersAddress" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.addressLine2 = addressLine2" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.checkYourAnswersLabel = WhatIsYourEmployersAddress" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.error.addressLine2.required = Enter addressLine2" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.error.addressLine2.length = addressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourEmployersAddress.addressLine2.change.hidden = addressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmployersAddressUserAnswersEntry: Arbitrary[(WhatIsYourEmployersAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourEmployersAddressPage.type]";\
    print "        value <- arbitrary[WhatIsYourEmployersAddress].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmployersAddressPage: Arbitrary[WhatIsYourEmployersAddressPage.type] =";\
    print "    Arbitrary(WhatIsYourEmployersAddressPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourEmployersAddress: Arbitrary[WhatIsYourEmployersAddress] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        addressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourEmployersAddress(addressLine1, addressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourEmployersAddressPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourEmployersAddress completed"
