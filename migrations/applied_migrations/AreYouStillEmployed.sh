#!/bin/bash

echo ""
echo "Applying migration AreYouStillEmployed"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouStillEmployed                        controllers.AreYouStillEmployedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouStillEmployed                        controllers.AreYouStillEmployedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouStillEmployed                  controllers.AreYouStillEmployedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouStillEmployed                  controllers.AreYouStillEmployedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouStillEmployed.title = areYouStillEmployed" >> ../conf/messages.en
echo "areYouStillEmployed.heading = areYouStillEmployed" >> ../conf/messages.en
echo "areYouStillEmployed.checkYourAnswersLabel = areYouStillEmployed" >> ../conf/messages.en
echo "areYouStillEmployed.error.required = Select yes if areYouStillEmployed" >> ../conf/messages.en
echo "areYouStillEmployed.change.hidden = AreYouStillEmployed" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouStillEmployedUserAnswersEntry: Arbitrary[(AreYouStillEmployedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouStillEmployedPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouStillEmployedPage: Arbitrary[AreYouStillEmployedPage.type] =";\
    print "    Arbitrary(AreYouStillEmployedPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouStillEmployedPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouStillEmployed completed"
