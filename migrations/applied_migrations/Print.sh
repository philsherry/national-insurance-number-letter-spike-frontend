#!/bin/bash

echo ""
echo "Applying migration Print"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /print                       controllers.PrintController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages
echo "print.title = print" >> ../conf/messages
echo "print.heading = print" >> ../conf/messages

echo "Migration Print completed"
