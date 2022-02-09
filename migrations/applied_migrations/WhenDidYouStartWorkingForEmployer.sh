#!/bin/bash

echo ""
echo "Applying migration WhenDidYouStartWorkingForEmployer"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouStartWorkingForEmployer                  controllers.WhenDidYouStartWorkingForEmployerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouStartWorkingForEmployer                  controllers.WhenDidYouStartWorkingForEmployerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouStartWorkingForEmployer                        controllers.WhenDidYouStartWorkingForEmployerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouStartWorkingForEmployer                        controllers.WhenDidYouStartWorkingForEmployerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.title = WhenDidYouStartWorkingForEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.heading = WhenDidYouStartWorkingForEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.checkYourAnswersLabel = WhenDidYouStartWorkingForEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.error.required.all = Enter the whenDidYouStartWorkingForEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.error.required.two = The whenDidYouStartWorkingForEmployer" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.error.required = The whenDidYouStartWorkingForEmployer must include {0}" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.error.invalid = Enter a real WhenDidYouStartWorkingForEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForEmployer.change.hidden = WhenDidYouStartWorkingForEmployer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStartWorkingForEmployerUserAnswersEntry: Arbitrary[(WhenDidYouStartWorkingForEmployerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouStartWorkingForEmployerPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStartWorkingForEmployerPage: Arbitrary[WhenDidYouStartWorkingForEmployerPage.type] =";\
    print "    Arbitrary(WhenDidYouStartWorkingForEmployerPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouStartWorkingForEmployerPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouStartWorkingForEmployer completed"
