#!/bin/bash

echo ""
echo "Applying migration IsYourPreviousAddressInUk"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /isYourPreviousAddressInUk                        controllers.IsYourPreviousAddressInUkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /isYourPreviousAddressInUk                        controllers.IsYourPreviousAddressInUkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIsYourPreviousAddressInUk                  controllers.IsYourPreviousAddressInUkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIsYourPreviousAddressInUk                  controllers.IsYourPreviousAddressInUkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "isYourPreviousAddressInUk.title = isYourPreviousAddressInUk" >> ../conf/messages.en
echo "isYourPreviousAddressInUk.heading = isYourPreviousAddressInUk" >> ../conf/messages.en
echo "isYourPreviousAddressInUk.checkYourAnswersLabel = isYourPreviousAddressInUk" >> ../conf/messages.en
echo "isYourPreviousAddressInUk.error.required = Select yes if isYourPreviousAddressInUk" >> ../conf/messages.en
echo "isYourPreviousAddressInUk.change.hidden = IsYourPreviousAddressInUk" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsYourPreviousAddressInUkUserAnswersEntry: Arbitrary[(IsYourPreviousAddressInUkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IsYourPreviousAddressInUkPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsYourPreviousAddressInUkPage: Arbitrary[IsYourPreviousAddressInUkPage.type] =";\
    print "    Arbitrary(IsYourPreviousAddressInUkPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IsYourPreviousAddressInUkPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration IsYourPreviousAddressInUk completed"
