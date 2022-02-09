#!/bin/bash

echo ""
echo "Applying migration HaveYouEverClaimedChildBenefit"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /haveYouEverClaimedChildBenefit                        controllers.HaveYouEverClaimedChildBenefitController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /haveYouEverClaimedChildBenefit                        controllers.HaveYouEverClaimedChildBenefitController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHaveYouEverClaimedChildBenefit                  controllers.HaveYouEverClaimedChildBenefitController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHaveYouEverClaimedChildBenefit                  controllers.HaveYouEverClaimedChildBenefitController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "haveYouEverClaimedChildBenefit.title = haveYouEverClaimedChildBenefit" >> ../conf/messages.en
echo "haveYouEverClaimedChildBenefit.heading = haveYouEverClaimedChildBenefit" >> ../conf/messages.en
echo "haveYouEverClaimedChildBenefit.checkYourAnswersLabel = haveYouEverClaimedChildBenefit" >> ../conf/messages.en
echo "haveYouEverClaimedChildBenefit.error.required = Select yes if haveYouEverClaimedChildBenefit" >> ../conf/messages.en
echo "haveYouEverClaimedChildBenefit.change.hidden = HaveYouEverClaimedChildBenefit" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverClaimedChildBenefitUserAnswersEntry: Arbitrary[(HaveYouEverClaimedChildBenefitPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HaveYouEverClaimedChildBenefitPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverClaimedChildBenefitPage: Arbitrary[HaveYouEverClaimedChildBenefitPage.type] =";\
    print "    Arbitrary(HaveYouEverClaimedChildBenefitPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HaveYouEverClaimedChildBenefitPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration HaveYouEverClaimedChildBenefit completed"
