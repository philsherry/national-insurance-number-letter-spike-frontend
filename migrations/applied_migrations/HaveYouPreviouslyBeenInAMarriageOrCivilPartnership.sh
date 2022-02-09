#!/bin/bash

echo ""
echo "Applying migration HaveYouPreviouslyBeenInAMarriageOrCivilPartnership"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /haveYouPreviouslyBeenInAMarriageOrCivilPartnership                        controllers.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /haveYouPreviouslyBeenInAMarriageOrCivilPartnership                        controllers.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHaveYouPreviouslyBeenInAMarriageOrCivilPartnership                  controllers.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHaveYouPreviouslyBeenInAMarriageOrCivilPartnership                  controllers.HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "haveYouPreviouslyBeenInAMarriageOrCivilPartnership.title = haveYouPreviouslyBeenInAMarriageOrCivilPartnership" >> ../conf/messages.en
echo "haveYouPreviouslyBeenInAMarriageOrCivilPartnership.heading = haveYouPreviouslyBeenInAMarriageOrCivilPartnership" >> ../conf/messages.en
echo "haveYouPreviouslyBeenInAMarriageOrCivilPartnership.checkYourAnswersLabel = haveYouPreviouslyBeenInAMarriageOrCivilPartnership" >> ../conf/messages.en
echo "haveYouPreviouslyBeenInAMarriageOrCivilPartnership.error.required = Select yes if haveYouPreviouslyBeenInAMarriageOrCivilPartnership" >> ../conf/messages.en
echo "haveYouPreviouslyBeenInAMarriageOrCivilPartnership.change.hidden = HaveYouPreviouslyBeenInAMarriageOrCivilPartnership" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouPreviouslyBeenInAMarriageOrCivilPartnershipUserAnswersEntry: Arbitrary[(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage: Arbitrary[HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage.type] =";\
    print "    Arbitrary(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HaveYouPreviouslyBeenInAMarriageOrCivilPartnershipPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration HaveYouPreviouslyBeenInAMarriageOrCivilPartnership completed"
