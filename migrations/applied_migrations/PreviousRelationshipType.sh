#!/bin/bash

echo ""
echo "Applying migration PreviousRelationshipType"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /previousRelationshipType                        controllers.PreviousRelationshipTypeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /previousRelationshipType                        controllers.PreviousRelationshipTypeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePreviousRelationshipType                  controllers.PreviousRelationshipTypeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePreviousRelationshipType                  controllers.PreviousRelationshipTypeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "previousRelationshipType.title = Is this relationship a marriage or civil partnership?" >> ../conf/messages
echo "previousRelationshipType.heading = Is this relationship a marriage or civil partnership?" >> ../conf/messages
echo "previousRelationshipType.marriage = Marriage" >> ../conf/messages
echo "previousRelationshipType.civilPartnership = Civil Partnership" >> ../conf/messages
echo "previousRelationshipType.checkYourAnswersLabel = Is this relationship a marriage or civil partnership?" >> ../conf/messages
echo "previousRelationshipType.error.required = Select previousRelationshipType" >> ../conf/messages
echo "previousRelationshipType.change.hidden = PreviousRelationshipType" >> ../conf/messages

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousRelationshipTypeUserAnswersEntry: Arbitrary[(PreviousRelationshipTypePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PreviousRelationshipTypePage.type]";\
    print "        value <- arbitrary[PreviousRelationshipType].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousRelationshipTypePage: Arbitrary[PreviousRelationshipTypePage.type] =";\
    print "    Arbitrary(PreviousRelationshipTypePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousRelationshipType: Arbitrary[PreviousRelationshipType] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(PreviousRelationshipType.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PreviousRelationshipTypePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration PreviousRelationshipType completed"
