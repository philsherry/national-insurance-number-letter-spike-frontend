#!/bin/bash

echo ""
echo "Applying migration DoYouKnowYourNationalInsuranceNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouKnowYourNationalInsuranceNumber                        controllers.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouKnowYourNationalInsuranceNumber                        controllers.DoYouKnowYourNationalInsuranceNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouKnowYourNationalInsuranceNumber                  controllers.DoYouKnowYourNationalInsuranceNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouKnowYourNationalInsuranceNumber                  controllers.DoYouKnowYourNationalInsuranceNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouKnowYourNationalInsuranceNumber.title = doYouKnowYourNationalInsuranceNumber" >> ../conf/messages.en
echo "doYouKnowYourNationalInsuranceNumber.heading = doYouKnowYourNationalInsuranceNumber" >> ../conf/messages.en
echo "doYouKnowYourNationalInsuranceNumber.checkYourAnswersLabel = doYouKnowYourNationalInsuranceNumber" >> ../conf/messages.en
echo "doYouKnowYourNationalInsuranceNumber.error.required = Select yes if doYouKnowYourNationalInsuranceNumber" >> ../conf/messages.en
echo "doYouKnowYourNationalInsuranceNumber.change.hidden = DoYouKnowYourNationalInsuranceNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouKnowYourNationalInsuranceNumberUserAnswersEntry: Arbitrary[(DoYouKnowYourNationalInsuranceNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouKnowYourNationalInsuranceNumberPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouKnowYourNationalInsuranceNumberPage: Arbitrary[DoYouKnowYourNationalInsuranceNumberPage.type] =";\
    print "    Arbitrary(DoYouKnowYourNationalInsuranceNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouKnowYourNationalInsuranceNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouKnowYourNationalInsuranceNumber completed"
