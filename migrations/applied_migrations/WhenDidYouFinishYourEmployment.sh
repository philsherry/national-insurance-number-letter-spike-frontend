#!/bin/bash

echo ""
echo "Applying migration WhenDidYouFinishYourEmployment"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /whenDidYouFinishYourEmployment                  controllers.WhenDidYouFinishYourEmploymentController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /whenDidYouFinishYourEmployment                  controllers.WhenDidYouFinishYourEmploymentController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWhenDidYouFinishYourEmployment                        controllers.WhenDidYouFinishYourEmploymentController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWhenDidYouFinishYourEmployment                        controllers.WhenDidYouFinishYourEmploymentController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.title = WhenDidYouFinishYourEmployment" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.heading = WhenDidYouFinishYourEmployment" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.hint = For example, 12 11 2007" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.checkYourAnswersLabel = WhenDidYouFinishYourEmployment" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.error.required.all = Enter the whenDidYouFinishYourEmployment" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.error.required.two = The whenDidYouFinishYourEmployment" must include {0} and {1} >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.error.required = The whenDidYouFinishYourEmployment must include {0}" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.error.invalid = Enter a real WhenDidYouFinishYourEmployment" >> ../conf/messages.en
echo "whenDidYouFinishYourEmployment.change.hidden = WhenDidYouFinishYourEmployment" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouFinishYourEmploymentUserAnswersEntry: Arbitrary[(WhenDidYouFinishYourEmploymentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WhenDidYouFinishYourEmploymentPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWhenDidYouFinishYourEmploymentPage: Arbitrary[WhenDidYouFinishYourEmploymentPage.type] =";\
    print "    Arbitrary(WhenDidYouFinishYourEmploymentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WhenDidYouFinishYourEmploymentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration WhenDidYouFinishYourEmployment completed"
