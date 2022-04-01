#!/bin/bash

echo ""
echo "Applying migration InsufficientDocuments"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /insufficientDocuments                       controllers.InsufficientDocumentsController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "insufficientDocuments.title = insufficientDocuments" >> ../conf/messages.en
echo "insufficientDocuments.heading = insufficientDocuments" >> ../conf/messages.en

echo "Migration InsufficientDocuments completed"
