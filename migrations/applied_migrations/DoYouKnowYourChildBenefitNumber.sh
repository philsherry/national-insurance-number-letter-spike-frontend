#!/bin/bash

echo ""
echo "Applying migration DoYouKnowYourChildBenefitNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouKnowYourChildBenefitNumber                        controllers.DoYouKnowYourChildBenefitNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouKnowYourChildBenefitNumber                        controllers.DoYouKnowYourChildBenefitNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouKnowYourChildBenefitNumber                  controllers.DoYouKnowYourChildBenefitNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouKnowYourChildBenefitNumber                  controllers.DoYouKnowYourChildBenefitNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouKnowYourChildBenefitNumber.title = doYouKnowYourChildBenefitNumber" >> ../conf/messages.en
echo "doYouKnowYourChildBenefitNumber.heading = doYouKnowYourChildBenefitNumber" >> ../conf/messages.en
echo "doYouKnowYourChildBenefitNumber.checkYourAnswersLabel = doYouKnowYourChildBenefitNumber" >> ../conf/messages.en
echo "doYouKnowYourChildBenefitNumber.error.required = Select yes if doYouKnowYourChildBenefitNumber" >> ../conf/messages.en
echo "doYouKnowYourChildBenefitNumber.change.hidden = DoYouKnowYourChildBenefitNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouKnowYourChildBenefitNumberUserAnswersEntry: Arbitrary[(DoYouKnowYourChildBenefitNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouKnowYourChildBenefitNumberPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouKnowYourChildBenefitNumberPage: Arbitrary[DoYouKnowYourChildBenefitNumberPage.type] =";\
    print "    Arbitrary(DoYouKnowYourChildBenefitNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouKnowYourChildBenefitNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouKnowYourChildBenefitNumber completed"
