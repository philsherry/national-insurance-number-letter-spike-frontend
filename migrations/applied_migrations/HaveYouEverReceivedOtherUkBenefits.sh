#!/bin/bash

echo ""
echo "Applying migration HaveYouEverReceivedOtherUkBenefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /haveYouEverReceivedOtherUkBenefits                        controllers.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /haveYouEverReceivedOtherUkBenefits                        controllers.HaveYouEverReceivedOtherUkBenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHaveYouEverReceivedOtherUkBenefits                  controllers.HaveYouEverReceivedOtherUkBenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHaveYouEverReceivedOtherUkBenefits                  controllers.HaveYouEverReceivedOtherUkBenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "haveYouEverReceivedOtherUkBenefits.title = haveYouEverReceivedOtherUkBenefits" >> ../conf/messages.en
echo "haveYouEverReceivedOtherUkBenefits.heading = haveYouEverReceivedOtherUkBenefits" >> ../conf/messages.en
echo "haveYouEverReceivedOtherUkBenefits.checkYourAnswersLabel = haveYouEverReceivedOtherUkBenefits" >> ../conf/messages.en
echo "haveYouEverReceivedOtherUkBenefits.error.required = Select yes if haveYouEverReceivedOtherUkBenefits" >> ../conf/messages.en
echo "haveYouEverReceivedOtherUkBenefits.change.hidden = HaveYouEverReceivedOtherUkBenefits" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverReceivedOtherUkBenefitsUserAnswersEntry: Arbitrary[(HaveYouEverReceivedOtherUkBenefitsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HaveYouEverReceivedOtherUkBenefitsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverReceivedOtherUkBenefitsPage: Arbitrary[HaveYouEverReceivedOtherUkBenefitsPage.type] =";\
    print "    Arbitrary(HaveYouEverReceivedOtherUkBenefitsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HaveYouEverReceivedOtherUkBenefitsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration HaveYouEverReceivedOtherUkBenefits completed"
