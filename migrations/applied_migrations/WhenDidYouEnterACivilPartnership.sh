#!/bin/bash

echo ""
echo "Applying migration WhenDidYouEnterACivilPartnership"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouEnterACivilPartnership                  controllers.WhenDidYouEnterACivilPartnershipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouEnterACivilPartnership                  controllers.WhenDidYouEnterACivilPartnershipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouEnterACivilPartnership                        controllers.WhenDidYouEnterACivilPartnershipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouEnterACivilPartnership                        controllers.WhenDidYouEnterACivilPartnershipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.title = WhenDidYouEnterACivilPartnership" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.heading = WhenDidYouEnterACivilPartnership" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.checkYourAnswersLabel = WhenDidYouEnterACivilPartnership" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.error.required.all = Enter the whenDidYouEnterACivilPartnership" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.error.required.two = The whenDidYouEnterACivilPartnership" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.error.required = The whenDidYouEnterACivilPartnership must include {0}" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.error.invalid = Enter a real WhenDidYouEnterACivilPartnership" >> ../conf/messages.en
echo "whenDidYouEnterACivilPartnership.change.hidden = WhenDidYouEnterACivilPartnership" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouEnterACivilPartnershipUserAnswersEntry: Arbitrary[(WhenDidYouEnterACivilPartnershipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouEnterACivilPartnershipPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouEnterACivilPartnershipPage: Arbitrary[WhenDidYouEnterACivilPartnershipPage.type] =";\
    print "    Arbitrary(WhenDidYouEnterACivilPartnershipPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouEnterACivilPartnershipPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouEnterACivilPartnership completed"
