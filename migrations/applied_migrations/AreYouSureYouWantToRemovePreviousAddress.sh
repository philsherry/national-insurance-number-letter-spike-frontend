#!/bin/bash

echo ""
echo "Applying migration AreYouSureYouWantToRemovePreviousAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouSureYouWantToRemovePreviousAddress                        controllers.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouSureYouWantToRemovePreviousAddress                        controllers.AreYouSureYouWantToRemovePreviousAddressController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouSureYouWantToRemovePreviousAddress                  controllers.AreYouSureYouWantToRemovePreviousAddressController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouSureYouWantToRemovePreviousAddress                  controllers.AreYouSureYouWantToRemovePreviousAddressController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousAddress.title = areYouSureYouWantToRemovePreviousAddress" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousAddress.heading = areYouSureYouWantToRemovePreviousAddress" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousAddress.checkYourAnswersLabel = areYouSureYouWantToRemovePreviousAddress" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousAddress.error.required = Select yes if areYouSureYouWantToRemovePreviousAddress" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousAddress.change.hidden = AreYouSureYouWantToRemovePreviousAddress" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousAddressUserAnswersEntry: Arbitrary[(AreYouSureYouWantToRemovePreviousAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouSureYouWantToRemovePreviousAddressPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousAddressPage: Arbitrary[AreYouSureYouWantToRemovePreviousAddressPage.type] =";\
    print "    Arbitrary(AreYouSureYouWantToRemovePreviousAddressPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouSureYouWantToRemovePreviousAddressPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouSureYouWantToRemovePreviousAddress completed"
