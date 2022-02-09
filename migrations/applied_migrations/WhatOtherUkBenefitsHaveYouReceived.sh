#!/bin/bash

echo ""
echo "Applying migration WhatOtherUkBenefitsHaveYouReceived"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whatOtherUkBenefitsHaveYouReceived                        controllers.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whatOtherUkBenefitsHaveYouReceived                        controllers.WhatOtherUkBenefitsHaveYouReceivedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhatOtherUkBenefitsHaveYouReceived                  controllers.WhatOtherUkBenefitsHaveYouReceivedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhatOtherUkBenefitsHaveYouReceived                  controllers.WhatOtherUkBenefitsHaveYouReceivedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.title = whatOtherUkBenefitsHaveYouReceived" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.heading = whatOtherUkBenefitsHaveYouReceived" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.checkYourAnswersLabel = whatOtherUkBenefitsHaveYouReceived" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.error.required = Enter whatOtherUkBenefitsHaveYouReceived" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.error.length = WhatOtherUkBenefitsHaveYouReceived must be 100 characters or less" >> ../conf/messages.en
echo "whatOtherUkBenefitsHaveYouReceived.change.hidden = WhatOtherUkBenefitsHaveYouReceived" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatOtherUkBenefitsHaveYouReceivedUserAnswersEntry: Arbitrary[(WhatOtherUkBenefitsHaveYouReceivedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhatOtherUkBenefitsHaveYouReceivedPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhatOtherUkBenefitsHaveYouReceivedPage: Arbitrary[WhatOtherUkBenefitsHaveYouReceivedPage.type] =";\
    print "    Arbitrary(WhatOtherUkBenefitsHaveYouReceivedPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhatOtherUkBenefitsHaveYouReceivedPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhatOtherUkBenefitsHaveYouReceived completed"
