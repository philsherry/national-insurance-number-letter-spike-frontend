#!/bin/bash

echo ""
echo "Applying migration DoYouHaveAnyPreviousAddresses"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHaveAnyPreviousAddresses                        controllers.DoYouHaveAnyPreviousAddressesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHaveAnyPreviousAddresses                        controllers.DoYouHaveAnyPreviousAddressesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHaveAnyPreviousAddresses                  controllers.DoYouHaveAnyPreviousAddressesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHaveAnyPreviousAddresses                  controllers.DoYouHaveAnyPreviousAddressesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHaveAnyPreviousAddresses.title = doYouHaveAnyPreviousAddresses" >> ../conf/messages.en
echo "doYouHaveAnyPreviousAddresses.heading = doYouHaveAnyPreviousAddresses" >> ../conf/messages.en
echo "doYouHaveAnyPreviousAddresses.checkYourAnswersLabel = doYouHaveAnyPreviousAddresses" >> ../conf/messages.en
echo "doYouHaveAnyPreviousAddresses.error.required = Select yes if doYouHaveAnyPreviousAddresses" >> ../conf/messages.en
echo "doYouHaveAnyPreviousAddresses.change.hidden = DoYouHaveAnyPreviousAddresses" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAnyPreviousAddressesUserAnswersEntry: Arbitrary[(DoYouHaveAnyPreviousAddressesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHaveAnyPreviousAddressesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAnyPreviousAddressesPage: Arbitrary[DoYouHaveAnyPreviousAddressesPage.type] =";\
    print "    Arbitrary(DoYouHaveAnyPreviousAddressesPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHaveAnyPreviousAddressesPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouHaveAnyPreviousAddresses completed"
