#!/bin/bash

echo ""
echo "Applying migration DoYouHaveAPreviousName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /doYouHaveAPreviousName                        controllers.DoYouHaveAPreviousNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /doYouHaveAPreviousName                        controllers.DoYouHaveAPreviousNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDoYouHaveAPreviousName                  controllers.DoYouHaveAPreviousNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDoYouHaveAPreviousName                  controllers.DoYouHaveAPreviousNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "doYouHaveAPreviousName.title = doYouHaveAPreviousName" >> ../conf/messages.en
echo "doYouHaveAPreviousName.heading = doYouHaveAPreviousName" >> ../conf/messages.en
echo "doYouHaveAPreviousName.checkYourAnswersLabel = doYouHaveAPreviousName" >> ../conf/messages.en
echo "doYouHaveAPreviousName.error.required = Select yes if doYouHaveAPreviousName" >> ../conf/messages.en
echo "doYouHaveAPreviousName.change.hidden = DoYouHaveAPreviousName" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAPreviousNameUserAnswersEntry: Arbitrary[(DoYouHaveAPreviousNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DoYouHaveAPreviousNamePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDoYouHaveAPreviousNamePage: Arbitrary[DoYouHaveAPreviousNamePage.type] =";\
    print "    Arbitrary(DoYouHaveAPreviousNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DoYouHaveAPreviousNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DoYouHaveAPreviousName completed"
