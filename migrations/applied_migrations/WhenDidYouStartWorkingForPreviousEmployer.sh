#!/bin/bash

echo ""
echo "Applying migration WhenDidYouStartWorkingForPreviousEmployer"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouStartWorkingForPreviousEmployer                  controllers.WhenDidYouStartWorkingForPreviousEmployerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouStartWorkingForPreviousEmployer                  controllers.WhenDidYouStartWorkingForPreviousEmployerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouStartWorkingForPreviousEmployer                        controllers.WhenDidYouStartWorkingForPreviousEmployerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouStartWorkingForPreviousEmployer                        controllers.WhenDidYouStartWorkingForPreviousEmployerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.title = WhenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.heading = WhenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.checkYourAnswersLabel = WhenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.error.required.all = Enter the whenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.error.required.two = The whenDidYouStartWorkingForPreviousEmployer" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.error.required = The whenDidYouStartWorkingForPreviousEmployer must include {0}" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.error.invalid = Enter a real WhenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStartWorkingForPreviousEmployer.change.hidden = WhenDidYouStartWorkingForPreviousEmployer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStartWorkingForPreviousEmployerUserAnswersEntry: Arbitrary[(WhenDidYouStartWorkingForPreviousEmployerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouStartWorkingForPreviousEmployerPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStartWorkingForPreviousEmployerPage: Arbitrary[WhenDidYouStartWorkingForPreviousEmployerPage.type] =";\
    print "    Arbitrary(WhenDidYouStartWorkingForPreviousEmployerPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouStartWorkingForPreviousEmployerPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouStartWorkingForPreviousEmployer completed"
