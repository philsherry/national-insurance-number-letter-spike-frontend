#!/bin/bash

echo ""
echo "Applying migration AreYouInACivilPartnership"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouInACivilPartnership                        controllers.AreYouInACivilPartnershipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouInACivilPartnership                        controllers.AreYouInACivilPartnershipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouInACivilPartnership                  controllers.AreYouInACivilPartnershipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouInACivilPartnership                  controllers.AreYouInACivilPartnershipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouInACivilPartnership.title = areYouInACivilPartnership" >> ../conf/messages.en
echo "areYouInACivilPartnership.heading = areYouInACivilPartnership" >> ../conf/messages.en
echo "areYouInACivilPartnership.checkYourAnswersLabel = areYouInACivilPartnership" >> ../conf/messages.en
echo "areYouInACivilPartnership.error.required = Select yes if areYouInACivilPartnership" >> ../conf/messages.en
echo "areYouInACivilPartnership.change.hidden = AreYouInACivilPartnership" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouInACivilPartnershipUserAnswersEntry: Arbitrary[(AreYouInACivilPartnershipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouInACivilPartnershipPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouInACivilPartnershipPage: Arbitrary[AreYouInACivilPartnershipPage.type] =";\
    print "    Arbitrary(AreYouInACivilPartnershipPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouInACivilPartnershipPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouInACivilPartnership completed"
