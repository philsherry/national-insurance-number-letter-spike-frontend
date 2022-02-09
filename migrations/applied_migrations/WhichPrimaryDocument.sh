#!/bin/bash

echo ""
echo "Applying migration WhichPrimaryDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whichPrimaryDocument                        controllers.WhichPrimaryDocumentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whichPrimaryDocument                        controllers.WhichPrimaryDocumentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhichPrimaryDocument                  controllers.WhichPrimaryDocumentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhichPrimaryDocument                  controllers.WhichPrimaryDocumentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whichPrimaryDocument.title = whichPrimaryDocument" >> ../conf/messages.en
echo "whichPrimaryDocument.heading = whichPrimaryDocument" >> ../conf/messages.en
echo "whichPrimaryDocument.passport = Passport" >> ../conf/messages.en
echo "whichPrimaryDocument.drivingLicense = Driving license" >> ../conf/messages.en
echo "whichPrimaryDocument.checkYourAnswersLabel = whichPrimaryDocument" >> ../conf/messages.en
echo "whichPrimaryDocument.error.required = Select whichPrimaryDocument" >> ../conf/messages.en
echo "whichPrimaryDocument.change.hidden = WhichPrimaryDocument" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichPrimaryDocumentUserAnswersEntry: Arbitrary[(WhichPrimaryDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhichPrimaryDocumentPage.type]";\
    print "        value <- arbitrary[WhichPrimaryDocument].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichPrimaryDocumentPage: Arbitrary[WhichPrimaryDocumentPage.type] =";\
    print "    Arbitrary(WhichPrimaryDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichPrimaryDocument: Arbitrary[WhichPrimaryDocument] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(WhichPrimaryDocument.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhichPrimaryDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhichPrimaryDocument completed"
