#!/bin/bash

echo ""
echo "Applying migration AreYouSureYouWantToRemovePreviousEmployer"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouSureYouWantToRemovePreviousEmployer                        controllers.AreYouSureYouWantToRemovePreviousEmployerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouSureYouWantToRemovePreviousEmployer                        controllers.AreYouSureYouWantToRemovePreviousEmployerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouSureYouWantToRemovePreviousEmployer                  controllers.AreYouSureYouWantToRemovePreviousEmployerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouSureYouWantToRemovePreviousEmployer                  controllers.AreYouSureYouWantToRemovePreviousEmployerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousEmployer.title = areYouSureYouWantToRemovePreviousEmployer" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousEmployer.heading = areYouSureYouWantToRemovePreviousEmployer" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousEmployer.checkYourAnswersLabel = areYouSureYouWantToRemovePreviousEmployer" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousEmployer.error.required = Select yes if areYouSureYouWantToRemovePreviousEmployer" >> ../conf/messages.en
echo "areYouSureYouWantToRemovePreviousEmployer.change.hidden = AreYouSureYouWantToRemovePreviousEmployer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousEmployerUserAnswersEntry: Arbitrary[(AreYouSureYouWantToRemovePreviousEmployerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouSureYouWantToRemovePreviousEmployerPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousEmployerPage: Arbitrary[AreYouSureYouWantToRemovePreviousEmployerPage.type] =";\
    print "    Arbitrary(AreYouSureYouWantToRemovePreviousEmployerPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouSureYouWantToRemovePreviousEmployerPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouSureYouWantToRemovePreviousEmployer completed"
