#!/bin/bash

echo ""
echo "Applying migration WhichAlternativeDocuments"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whichAlternativeDocuments                        controllers.WhichAlternativeDocumentsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whichAlternativeDocuments                        controllers.WhichAlternativeDocumentsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhichAlternativeDocuments                  controllers.WhichAlternativeDocumentsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhichAlternativeDocuments                  controllers.WhichAlternativeDocumentsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whichAlternativeDocuments.title = whichAlternativeDocuments" >> ../conf/messages.en
echo "whichAlternativeDocuments.heading = whichAlternativeDocuments" >> ../conf/messages.en
echo "whichAlternativeDocuments.one = one" >> ../conf/messages.en
echo "whichAlternativeDocuments.two = two" >> ../conf/messages.en
echo "whichAlternativeDocuments.checkYourAnswersLabel = whichAlternativeDocuments" >> ../conf/messages.en
echo "whichAlternativeDocuments.error.required = Select whichAlternativeDocuments" >> ../conf/messages.en
echo "whichAlternativeDocuments.change.hidden = WhichAlternativeDocuments" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichAlternativeDocumentsUserAnswersEntry: Arbitrary[(WhichAlternativeDocumentsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhichAlternativeDocumentsPage.type]";\
    print "        value <- arbitrary[WhichAlternativeDocuments].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichAlternativeDocumentsPage: Arbitrary[WhichAlternativeDocumentsPage.type] =";\
    print "    Arbitrary(WhichAlternativeDocumentsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhichAlternativeDocuments: Arbitrary[WhichAlternativeDocuments] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(WhichAlternativeDocuments.values)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhichAlternativeDocumentsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhichAlternativeDocuments completed"
