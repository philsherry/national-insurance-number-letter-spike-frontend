#!/bin/bash

echo ""
echo "Applying migration DoYouHavePrimaryDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHavePrimaryDocument                        controllers.DoYouHavePrimaryDocumentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHavePrimaryDocument                        controllers.DoYouHavePrimaryDocumentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHavePrimaryDocument                  controllers.DoYouHavePrimaryDocumentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHavePrimaryDocument                  controllers.DoYouHavePrimaryDocumentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHavePrimaryDocument.title = doYouHavePrimaryDocument" >> ../conf/messages.en
echo "doYouHavePrimaryDocument.heading = doYouHavePrimaryDocument" >> ../conf/messages.en
echo "doYouHavePrimaryDocument.checkYourAnswersLabel = doYouHavePrimaryDocument" >> ../conf/messages.en
echo "doYouHavePrimaryDocument.error.required = Select yes if doYouHavePrimaryDocument" >> ../conf/messages.en
echo "doYouHavePrimaryDocument.change.hidden = DoYouHavePrimaryDocument" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHavePrimaryDocumentUserAnswersEntry: Arbitrary[(DoYouHavePrimaryDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHavePrimaryDocumentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHavePrimaryDocumentPage: Arbitrary[DoYouHavePrimaryDocumentPage.type] =";\
    print "    Arbitrary(DoYouHavePrimaryDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHavePrimaryDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouHavePrimaryDocument completed"
