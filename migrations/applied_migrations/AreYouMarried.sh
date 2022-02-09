#!/bin/bash

echo ""
echo "Applying migration AreYouMarried"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouMarried                        controllers.AreYouMarriedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouMarried                        controllers.AreYouMarriedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouMarried                  controllers.AreYouMarriedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouMarried                  controllers.AreYouMarriedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouMarried.title = areYouMarried" >> ../conf/messages.en
echo "areYouMarried.heading = areYouMarried" >> ../conf/messages.en
echo "areYouMarried.checkYourAnswersLabel = areYouMarried" >> ../conf/messages.en
echo "areYouMarried.error.required = Select yes if areYouMarried" >> ../conf/messages.en
echo "areYouMarried.change.hidden = AreYouMarried" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouMarriedUserAnswersEntry: Arbitrary[(AreYouMarriedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouMarriedPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouMarriedPage: Arbitrary[AreYouMarriedPage.type] =";\
    print "    Arbitrary(AreYouMarriedPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouMarriedPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouMarried completed"
