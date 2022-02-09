#!/bin/bash

echo ""
echo "Applying migration WhenDidYouStopWorkingForPreviousEmployer"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouStopWorkingForPreviousEmployer                  controllers.WhenDidYouStopWorkingForPreviousEmployerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouStopWorkingForPreviousEmployer                  controllers.WhenDidYouStopWorkingForPreviousEmployerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouStopWorkingForPreviousEmployer                        controllers.WhenDidYouStopWorkingForPreviousEmployerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouStopWorkingForPreviousEmployer                        controllers.WhenDidYouStopWorkingForPreviousEmployerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.title = WhenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.heading = WhenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.checkYourAnswersLabel = WhenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.error.required.all = Enter the whenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.error.required.two = The whenDidYouStopWorkingForPreviousEmployer" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.error.required = The whenDidYouStopWorkingForPreviousEmployer must include {0}" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.error.invalid = Enter a real WhenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en
echo "whenDidYouStopWorkingForPreviousEmployer.change.hidden = WhenDidYouStopWorkingForPreviousEmployer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStopWorkingForPreviousEmployerUserAnswersEntry: Arbitrary[(WhenDidYouStopWorkingForPreviousEmployerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouStopWorkingForPreviousEmployerPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouStopWorkingForPreviousEmployerPage: Arbitrary[WhenDidYouStopWorkingForPreviousEmployerPage.type] =";\
    print "    Arbitrary(WhenDidYouStopWorkingForPreviousEmployerPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouStopWorkingForPreviousEmployerPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouStopWorkingForPreviousEmployer completed"
