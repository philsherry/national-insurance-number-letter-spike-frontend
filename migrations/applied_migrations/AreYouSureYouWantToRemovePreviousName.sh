#!/bin/bash

echo ""
echo "Applying migration AreYouSureYouWantToRemovePreviousName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouSureYouWantToRemovePreviousName                        controllers.AreYouSureYouWantToRemovePreviousNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouSureYouWantToRemovePreviousName                        controllers.AreYouSureYouWantToRemovePreviousNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouSureYouWantToRemovePreviousName                  controllers.AreYouSureYouWantToRemovePreviousNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouSureYouWantToRemovePreviousName                  controllers.AreYouSureYouWantToRemovePreviousNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousName.title = areYouSureYouWantToRemovePreviousName" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousName.heading = areYouSureYouWantToRemovePreviousName" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousName.checkYourAnswersLabel = areYouSureYouWantToRemovePreviousName" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousName.error.required = Select yes if areYouSureYouWantToRemovePreviousName" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousName.change.hidden = AreYouSureYouWantToRemovePreviousName" >> ../conf/messages

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousNameUserAnswersEntry: Arbitrary[(AreYouSureYouWantToRemovePreviousNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouSureYouWantToRemovePreviousNamePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousNamePage: Arbitrary[AreYouSureYouWantToRemovePreviousNamePage.type] =";\
    print "    Arbitrary(AreYouSureYouWantToRemovePreviousNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouSureYouWantToRemovePreviousNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouSureYouWantToRemovePreviousName completed"
