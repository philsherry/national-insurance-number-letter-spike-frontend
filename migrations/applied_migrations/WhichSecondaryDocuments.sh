#!/bin/bash

echo ""
echo "Applying migration WhichSecondaryDocuments"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whichSecondaryDocuments                        controllers.WhichSecondaryDocumentsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whichSecondaryDocuments                        controllers.WhichSecondaryDocumentsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhichSecondaryDocuments                  controllers.WhichSecondaryDocumentsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhichSecondaryDocuments                  controllers.WhichSecondaryDocumentsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whichSecondaryDocuments.title = whichSecondaryDocuments" >> ../conf/messages.en
echo "whichSecondaryDocuments.heading = whichSecondaryDocuments" >> ../conf/messages.en
echo "whichSecondaryDocuments.option1 = Option 1" >> ../conf/messages.en
echo "whichSecondaryDocuments.option2 = Option 2" >> ../conf/messages.en
echo "whichSecondaryDocuments.checkYourAnswersLabel = whichSecondaryDocuments" >> ../conf/messages.en
echo "whichSecondaryDocuments.error.required = Select whichSecondaryDocuments" >> ../conf/messages.en
echo "whichSecondaryDocuments.change.hidden = WhichSecondaryDocuments" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichSecondaryDocumentsUserAnswersEntry: Arbitrary[(WhichSecondaryDocumentsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhichSecondaryDocumentsPage.type]";\
    print "        value <- arbitrary[WhichSecondaryDocuments].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichSecondaryDocumentsPage: Arbitrary[WhichSecondaryDocumentsPage.type] =";\
    print "    Arbitrary(WhichSecondaryDocumentsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichSecondaryDocuments: Arbitrary[WhichSecondaryDocuments] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(WhichSecondaryDocuments.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhichSecondaryDocumentsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhichSecondaryDocuments completed"
