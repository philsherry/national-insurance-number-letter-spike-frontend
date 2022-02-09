#!/bin/bash

echo ""
echo "Applying migration AreYouReturningFromLivingAbroad"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouReturningFromLivingAbroad                        controllers.AreYouReturningFromLivingAbroadController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouReturningFromLivingAbroad                        controllers.AreYouReturningFromLivingAbroadController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouReturningFromLivingAbroad                  controllers.AreYouReturningFromLivingAbroadController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouReturningFromLivingAbroad                  controllers.AreYouReturningFromLivingAbroadController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouReturningFromLivingAbroad.title = areYouReturningFromLivingAbroad" >> ../conf/messages.en
echo "areYouReturningFromLivingAbroad.heading = areYouReturningFromLivingAbroad" >> ../conf/messages.en
echo "areYouReturningFromLivingAbroad.checkYourAnswersLabel = areYouReturningFromLivingAbroad" >> ../conf/messages.en
echo "areYouReturningFromLivingAbroad.error.required = Select yes if areYouReturningFromLivingAbroad" >> ../conf/messages.en
echo "areYouReturningFromLivingAbroad.change.hidden = AreYouReturningFromLivingAbroad" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouReturningFromLivingAbroadUserAnswersEntry: Arbitrary[(AreYouReturningFromLivingAbroadPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouReturningFromLivingAbroadPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouReturningFromLivingAbroadPage: Arbitrary[AreYouReturningFromLivingAbroadPage.type] =";\
    print "    Arbitrary(AreYouReturningFromLivingAbroadPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouReturningFromLivingAbroadPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouReturningFromLivingAbroad completed"
