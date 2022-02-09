#!/bin/bash

echo ""
echo "Applying migration IsYourCurrentAddressInUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isYourCurrentAddressInUk                        controllers.IsYourCurrentAddressInUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isYourCurrentAddressInUk                        controllers.IsYourCurrentAddressInUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsYourCurrentAddressInUk                  controllers.IsYourCurrentAddressInUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsYourCurrentAddressInUk                  controllers.IsYourCurrentAddressInUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isYourCurrentAddressInUk.title = isYourCurrentAddressInUk" >> ../conf/messages.en
echo "isYourCurrentAddressInUk.heading = isYourCurrentAddressInUk" >> ../conf/messages.en
echo "isYourCurrentAddressInUk.checkYourAnswersLabel = isYourCurrentAddressInUk" >> ../conf/messages.en
echo "isYourCurrentAddressInUk.error.required = Select yes if isYourCurrentAddressInUk" >> ../conf/messages.en
echo "isYourCurrentAddressInUk.change.hidden = IsYourCurrentAddressInUk" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsYourCurrentAddressInUkUserAnswersEntry: Arbitrary[(IsYourCurrentAddressInUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IsYourCurrentAddressInUkPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsYourCurrentAddressInUkPage: Arbitrary[IsYourCurrentAddressInUkPage.type] =";\
    print "    Arbitrary(IsYourCurrentAddressInUkPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IsYourCurrentAddressInUkPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration IsYourCurrentAddressInUk completed"
