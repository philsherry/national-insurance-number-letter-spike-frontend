#!/bin/bash

echo ""
echo "Applying migration CurrentRelationshipType"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /currentRelationshipType                        controllers.CurrentRelationshipTypeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /currentRelationshipType                        controllers.CurrentRelationshipTypeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeCurrentRelationshipType                  controllers.CurrentRelationshipTypeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeCurrentRelationshipType                  controllers.CurrentRelationshipTypeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "currentRelationshipType.title = Is your current relationship a marriage or civil partnership?" >> ../conf/messages
echo "currentRelationshipType.heading = Is your current relationship a marriage or civil partnership?" >> ../conf/messages
echo "currentRelationshipType.marriage = Marriage" >> ../conf/messages
echo "currentRelationshipType.civilPartnership = ivil Partnership" >> ../conf/messages
echo "currentRelationshipType.checkYourAnswersLabel = Is your current relationship a marriage or civil partnership?" >> ../conf/messages
echo "currentRelationshipType.error.required = Select currentRelationshipType" >> ../conf/messages
echo "currentRelationshipType.change.hidden = CurrentRelationshipType" >> ../conf/messages

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCurrentRelationshipTypeUserAnswersEntry: Arbitrary[(CurrentRelationshipTypePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CurrentRelationshipTypePage.type]";\
    print "        value <- arbitrary[CurrentRelationshipType].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCurrentRelationshipTypePage: Arbitrary[CurrentRelationshipTypePage.type] =";\
    print "    Arbitrary(CurrentRelationshipTypePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCurrentRelationshipType: Arbitrary[CurrentRelationshipType] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(CurrentRelationshipType.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CurrentRelationshipTypePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CurrentRelationshipType completed"
