#!/bin/bash

echo ""
echo "Applying migration WhatIsYourPreviousEmployersAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourPreviousEmployersAddress                        controllers.WhatIsYourPreviousEmployersAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourPreviousEmployersAddress                        controllers.WhatIsYourPreviousEmployersAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourPreviousEmployersAddress                  controllers.WhatIsYourPreviousEmployersAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourPreviousEmployersAddress                  controllers.WhatIsYourPreviousEmployersAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.title = whatIsYourPreviousEmployersAddress" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.heading = whatIsYourPreviousEmployersAddress" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.addressLine1 = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.addressLine2 = addressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.checkYourAnswersLabel = WhatIsYourPreviousEmployersAddress" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.error.addressLine1.required = Enter addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.error.addressLine2.required = Enter addressLine2" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.error.addressLine1.length = addressLine1 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.error.addressLine2.length = addressLine2 must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.addressLine1.change.hidden = addressLine1" >> ../conf/messages.en
echo "whatIsYourPreviousEmployersAddress.addressLine2.change.hidden = addressLine2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousEmployersAddressUserAnswersEntry: Arbitrary[(WhatIsYourPreviousEmployersAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourPreviousEmployersAddressPage.type]";\
    print "        value <- arbitrary[WhatIsYourPreviousEmployersAddress].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousEmployersAddressPage: Arbitrary[WhatIsYourPreviousEmployersAddressPage.type] =";\
    print "    Arbitrary(WhatIsYourPreviousEmployersAddressPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourPreviousEmployersAddress: Arbitrary[WhatIsYourPreviousEmployersAddress] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        addressLine1 <- arbitrary[String]";\
    print "        addressLine2 <- arbitrary[String]";\
    print "      } yield WhatIsYourPreviousEmployersAddress(addressLine1, addressLine2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourPreviousEmployersAddressPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourPreviousEmployersAddress completed"
