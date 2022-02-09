#!/bin/bash

echo ""
echo "Applying migration DoYouHaveTwoSecondaryDocuments"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHaveTwoSecondaryDocuments                        controllers.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHaveTwoSecondaryDocuments                        controllers.DoYouHaveTwoSecondaryDocumentsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHaveTwoSecondaryDocuments                  controllers.DoYouHaveTwoSecondaryDocumentsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHaveTwoSecondaryDocuments                  controllers.DoYouHaveTwoSecondaryDocumentsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHaveTwoSecondaryDocuments.title = doYouHaveTwoSecondaryDocuments" >> ../conf/messages.en
echo "doYouHaveTwoSecondaryDocuments.heading = doYouHaveTwoSecondaryDocuments" >> ../conf/messages.en
echo "doYouHaveTwoSecondaryDocuments.checkYourAnswersLabel = doYouHaveTwoSecondaryDocuments" >> ../conf/messages.en
echo "doYouHaveTwoSecondaryDocuments.error.required = Select yes if doYouHaveTwoSecondaryDocuments" >> ../conf/messages.en
echo "doYouHaveTwoSecondaryDocuments.change.hidden = DoYouHaveTwoSecondaryDocuments" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveTwoSecondaryDocumentsUserAnswersEntry: Arbitrary[(DoYouHaveTwoSecondaryDocumentsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHaveTwoSecondaryDocumentsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveTwoSecondaryDocumentsPage: Arbitrary[DoYouHaveTwoSecondaryDocumentsPage.type] =";\
    print "    Arbitrary(DoYouHaveTwoSecondaryDocumentsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHaveTwoSecondaryDocumentsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouHaveTwoSecondaryDocuments completed"
