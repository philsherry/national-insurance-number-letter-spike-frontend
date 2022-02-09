#!/bin/bash

echo ""
echo "Applying migration HaveYouEverWorkedInUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /haveYouEverWorkedInUk                        controllers.HaveYouEverWorkedInUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /haveYouEverWorkedInUk                        controllers.HaveYouEverWorkedInUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHaveYouEverWorkedInUk                  controllers.HaveYouEverWorkedInUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHaveYouEverWorkedInUk                  controllers.HaveYouEverWorkedInUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "haveYouEverWorkedInUk.title = haveYouEverWorkedInUk" >> ../conf/messages.en
echo "haveYouEverWorkedInUk.heading = haveYouEverWorkedInUk" >> ../conf/messages.en
echo "haveYouEverWorkedInUk.checkYourAnswersLabel = haveYouEverWorkedInUk" >> ../conf/messages.en
echo "haveYouEverWorkedInUk.error.required = Select yes if haveYouEverWorkedInUk" >> ../conf/messages.en
echo "haveYouEverWorkedInUk.change.hidden = HaveYouEverWorkedInUk" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverWorkedInUkUserAnswersEntry: Arbitrary[(HaveYouEverWorkedInUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HaveYouEverWorkedInUkPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHaveYouEverWorkedInUkPage: Arbitrary[HaveYouEverWorkedInUkPage.type] =";\
    print "    Arbitrary(HaveYouEverWorkedInUkPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HaveYouEverWorkedInUkPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration HaveYouEverWorkedInUk completed"
