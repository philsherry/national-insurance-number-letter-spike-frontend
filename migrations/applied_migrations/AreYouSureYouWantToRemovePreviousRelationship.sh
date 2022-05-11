#!/bin/bash

echo ""
echo "Applying migration AreYouSureYouWantToRemovePreviousRelationship"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /areYouSureYouWantToRemovePreviousRelationship                        controllers.AreYouSureYouWantToRemovePreviousRelationshipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /areYouSureYouWantToRemovePreviousRelationship                        controllers.AreYouSureYouWantToRemovePreviousRelationshipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAreYouSureYouWantToRemovePreviousRelationship                  controllers.AreYouSureYouWantToRemovePreviousRelationshipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAreYouSureYouWantToRemovePreviousRelationship                  controllers.AreYouSureYouWantToRemovePreviousRelationshipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousRelationship.title = areYouSureYouWantToRemovePreviousRelationship" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousRelationship.heading = areYouSureYouWantToRemovePreviousRelationship" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousRelationship.checkYourAnswersLabel = areYouSureYouWantToRemovePreviousRelationship" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousRelationship.error.required = Select yes if areYouSureYouWantToRemovePreviousRelationship" >> ../conf/messages
echo "areYouSureYouWantToRemovePreviousRelationship.change.hidden = AreYouSureYouWantToRemovePreviousRelationship" >> ../conf/messages

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousRelationshipUserAnswersEntry: Arbitrary[(AreYouSureYouWantToRemovePreviousRelationshipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AreYouSureYouWantToRemovePreviousRelationshipPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAreYouSureYouWantToRemovePreviousRelationshipPage: Arbitrary[AreYouSureYouWantToRemovePreviousRelationshipPage.type] =";\
    print "    Arbitrary(AreYouSureYouWantToRemovePreviousRelationshipPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AreYouSureYouWantToRemovePreviousRelationshipPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AreYouSureYouWantToRemovePreviousRelationship completed"
