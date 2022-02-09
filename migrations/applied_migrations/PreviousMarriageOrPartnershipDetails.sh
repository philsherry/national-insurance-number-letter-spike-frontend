#!/bin/bash

echo ""
echo "Applying migration PreviousMarriageOrPartnershipDetails"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /previousMarriageOrPartnershipDetails                        controllers.PreviousMarriageOrPartnershipDetailsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /previousMarriageOrPartnershipDetails                        controllers.PreviousMarriageOrPartnershipDetailsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePreviousMarriageOrPartnershipDetails                  controllers.PreviousMarriageOrPartnershipDetailsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePreviousMarriageOrPartnershipDetails                  controllers.PreviousMarriageOrPartnershipDetailsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.title = previousMarriageOrPartnershipDetails" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.heading = previousMarriageOrPartnershipDetails" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.startDate = startDate" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.endDate = endDate" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.checkYourAnswersLabel = PreviousMarriageOrPartnershipDetails" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.error.startDate.required = Enter startDate" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.error.endDate.required = Enter endDate" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.error.startDate.length = startDate must be 100 characters or less" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.error.endDate.length = endDate must be 100 characters or less" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.startDate.change.hidden = startDate" >> ../conf/messages.en
echo "previousMarriageOrPartnershipDetails.endDate.change.hidden = endDate" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousMarriageOrPartnershipDetailsUserAnswersEntry: Arbitrary[(PreviousMarriageOrPartnershipDetailsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PreviousMarriageOrPartnershipDetailsPage.type]";\
    print "        value <- arbitrary[PreviousMarriageOrPartnershipDetails].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousMarriageOrPartnershipDetailsPage: Arbitrary[PreviousMarriageOrPartnershipDetailsPage.type] =";\
    print "    Arbitrary(PreviousMarriageOrPartnershipDetailsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousMarriageOrPartnershipDetails: Arbitrary[PreviousMarriageOrPartnershipDetails] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        startDate <- arbitrary[String]";\
    print "        endDate <- arbitrary[String]";\
    print "      } yield PreviousMarriageOrPartnershipDetails(startDate, endDate)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PreviousMarriageOrPartnershipDetailsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration PreviousMarriageOrPartnershipDetails completed"
