#!/bin/bash

echo ""
echo "Applying migration WhatIsYourChildBenefitNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatIsYourChildBenefitNumber                        controllers.WhatIsYourChildBenefitNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatIsYourChildBenefitNumber                        controllers.WhatIsYourChildBenefitNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatIsYourChildBenefitNumber                  controllers.WhatIsYourChildBenefitNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatIsYourChildBenefitNumber                  controllers.WhatIsYourChildBenefitNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.title = whatIsYourChildBenefitNumber" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.heading = whatIsYourChildBenefitNumber" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.checkYourAnswersLabel = whatIsYourChildBenefitNumber" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.error.required = Enter whatIsYourChildBenefitNumber" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.error.length = WhatIsYourChildBenefitNumber must be 100 characters or less" >> ../conf/messages.en
echo "whatIsYourChildBenefitNumber.change.hidden = WhatIsYourChildBenefitNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourChildBenefitNumberUserAnswersEntry: Arbitrary[(WhatIsYourChildBenefitNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatIsYourChildBenefitNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatIsYourChildBenefitNumberPage: Arbitrary[WhatIsYourChildBenefitNumberPage.type] =";\
    print "    Arbitrary(WhatIsYourChildBenefitNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatIsYourChildBenefitNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatIsYourChildBenefitNumber completed"
