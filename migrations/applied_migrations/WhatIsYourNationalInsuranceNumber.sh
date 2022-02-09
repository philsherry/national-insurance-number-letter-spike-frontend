#!/bin/bash

echo ""
echo "Applying migration WhatIsYourNationalInsuranceNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourNationalInsuranceNumber                        controllers.WhatIsYourNationalInsuranceNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourNationalInsuranceNumber                        controllers.WhatIsYourNationalInsuranceNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourNationalInsuranceNumber                  controllers.WhatIsYourNationalInsuranceNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourNationalInsuranceNumber                  controllers.WhatIsYourNationalInsuranceNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.title = whatIsYourNationalInsuranceNumber" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.heading = whatIsYourNationalInsuranceNumber" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.checkYourAnswersLabel = whatIsYourNationalInsuranceNumber" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.error.required = Enter whatIsYourNationalInsuranceNumber" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.error.length = WhatIsYourNationalInsuranceNumber must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourNationalInsuranceNumber.change.hidden = WhatIsYourNationalInsuranceNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourNationalInsuranceNumberUserAnswersEntry: Arbitrary[(WhatIsYourNationalInsuranceNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourNationalInsuranceNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourNationalInsuranceNumberPage: Arbitrary[WhatIsYourNationalInsuranceNumberPage.type] =";\
    print "    Arbitrary(WhatIsYourNationalInsuranceNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourNationalInsuranceNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourNationalInsuranceNumber completed"
