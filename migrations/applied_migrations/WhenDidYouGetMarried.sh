#!/bin/bash

echo ""
echo "Applying migration WhenDidYouGetMarried"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouGetMarried                  controllers.WhenDidYouGetMarriedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouGetMarried                  controllers.WhenDidYouGetMarriedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouGetMarried                        controllers.WhenDidYouGetMarriedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouGetMarried                        controllers.WhenDidYouGetMarriedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouGetMarried.title = WhenDidYouGetMarried" >> ../conf/messages.en
echo "whenDidYouGetMarried.heading = WhenDidYouGetMarried" >> ../conf/messages.en
echo "whenDidYouGetMarried.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouGetMarried.checkYourAnswersLabel = WhenDidYouGetMarried" >> ../conf/messages.en
echo "whenDidYouGetMarried.error.required.all = Enter the whenDidYouGetMarried" >> ../conf/messages.en
echo "whenDidYouGetMarried.error.required.two = The whenDidYouGetMarried" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouGetMarried.error.required = The whenDidYouGetMarried must include {0}" >> ../conf/messages.en
echo "whenDidYouGetMarried.error.invalid = Enter a real WhenDidYouGetMarried" >> ../conf/messages.en
echo "whenDidYouGetMarried.change.hidden = WhenDidYouGetMarried" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouGetMarriedUserAnswersEntry: Arbitrary[(WhenDidYouGetMarriedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouGetMarriedPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouGetMarriedPage: Arbitrary[WhenDidYouGetMarriedPage.type] =";\
    print "    Arbitrary(WhenDidYouGetMarriedPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouGetMarriedPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouGetMarried completed"
