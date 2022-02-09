#!/bin/bash

echo ""
echo "Applying migration DoYouHaveAnyPreviousEmployers"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHaveAnyPreviousEmployers                        controllers.DoYouHaveAnyPreviousEmployersController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHaveAnyPreviousEmployers                        controllers.DoYouHaveAnyPreviousEmployersController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHaveAnyPreviousEmployers                  controllers.DoYouHaveAnyPreviousEmployersController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHaveAnyPreviousEmployers                  controllers.DoYouHaveAnyPreviousEmployersController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHaveAnyPreviousEmployers.title = doYouHaveAnyPreviousEmployers" >> ../conf/messages.en
echo "doYouHaveAnyPreviousEmployers.heading = doYouHaveAnyPreviousEmployers" >> ../conf/messages.en
echo "doYouHaveAnyPreviousEmployers.checkYourAnswersLabel = doYouHaveAnyPreviousEmployers" >> ../conf/messages.en
echo "doYouHaveAnyPreviousEmployers.error.required = Select yes if doYouHaveAnyPreviousEmployers" >> ../conf/messages.en
echo "doYouHaveAnyPreviousEmployers.change.hidden = DoYouHaveAnyPreviousEmployers" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAnyPreviousEmployersUserAnswersEntry: Arbitrary[(DoYouHaveAnyPreviousEmployersPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHaveAnyPreviousEmployersPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAnyPreviousEmployersPage: Arbitrary[DoYouHaveAnyPreviousEmployersPage.type] =";\
    print "    Arbitrary(DoYouHaveAnyPreviousEmployersPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHaveAnyPreviousEmployersPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouHaveAnyPreviousEmployers completed"
