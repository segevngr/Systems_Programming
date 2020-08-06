//
// Created by segevnag@wincs.cs.bgu.ac.il on 15/01/2020.
//

#include <stdlib.h>
#include <sstream>
#include <vector>
#include <string>
#include <iostream>
#include <boost/algorithm/string.hpp>

#include "../include/StompFrame.h"
#include "../include/User.h"
#include "../include/connectionHandler.h"
#include "../include/Keyboard.h"
#include "../include/Converter.h"

using namespace std;

Keyboard::Keyboard(ConnectionHandler *handler, User *user, bool *online) : handler(handler), user(user), online(online), waitForInput(true) {}

void Keyboard::run() {
    while (*online && waitForInput) {
        StompFrame* frame = new StompFrame;
        Converter *converter = new Converter;
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);

        vector<string> input = converter->stringToVec(line, ' ');

        if (input[0] == "login") {
            frame->setCommand("CONNECT");
            frame->addHeader("accept-version", "1.2");
            frame->addHeader("host", "stomp.cs.bgu.ac.il");
            frame->addHeader("login", input[2]);
            frame->addHeader("passcode", input[3]);
            string frameToString = converter->StompFrameToString(*frame);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
            //TODO Socket error?
        }
        if (input[0] == "join") {
            frame->setCommand("SUBSCRIBE");
            frame->addHeader("destination", input[1]);
            frame->addHeader("id", to_string(user->getSubscriptionId()));
            frame->addHeader("receipt", to_string(user->getReceiptId()));

            user->addReceiptId(user->getReceiptId(), input[1]);
            user->addSubId(user->getSubscriptionId(), input[1]);
            user->incrementReceiptId();
            user->incrementSubcriptionId();

            string frameToString = converter->StompFrameToString(*frame);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
        }
        if (input[0] == "add") {//add sci-fi Foundation
            user->addBookToInventory(input[2], input[1],"");
            frame->setCommand("SEND");
            frame->addHeader("destination", input[1]);
            frame->setFrameBody(user->getName() + " has added the book " + input[2]);
            string frameToString = converter->StompFrameToString(*frame);

            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
        }
        if (input[0] == "borrow") {
            user->addToWishlist(input[2]); // here we insert the relevant book to the borrow waiting list
            frame->setCommand("SEND");
            frame->addHeader("destination", input[1]);
            frame->setFrameBody(user->getName() + " wish to borrow " + input[2]);
            string frameToString = converter->StompFrameToString(*frame);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
        }
        if (input[0] == "return") {
            string returnTo = user->borrowedFrom(input[2]);
            user->removeBook(input[2]);
            frame->setCommand("SEND");
            frame->addHeader("destination", input[1]);
            frame->setFrameBody("Returning " + input[2] + " to " +returnTo);
            string frameToString = converter->StompFrameToString(*frame);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }

        }
        if (input[0] == "status") {
            frame->setCommand("SEND");
            frame->addHeader("destination", input[1]);
            frame->setFrameBody("book status");
            string frameToString = converter->StompFrameToString(*frame);

            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
        }
        if (input[0] == "exit") {
            frame->setCommand("UNSUBSCRIBE");
            frame->addHeader("id", to_string(user->getSubIdByTopic(input[1])));
            frame->addHeader("receipt", to_string(user->getReceiptId()));
            string frameToString = converter->StompFrameToString(*frame);
            user->addReceiptId(user->getReceiptId(), "EXIT " + input[1]);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }
        }

        if (input[0] == "logout") {
            waitForInput = false;
            frame->setCommand("DISCONNECT");
            frame->addHeader("receipt", to_string(user->getReceiptId()));
            user->addReceiptId(user->getReceiptId(), "LOGOUT");    // Added logout

            string frameToString = converter->StompFrameToString(*frame);
            if(!handler->sendLine(frameToString)) {
                cerr << "Disconnected. Existing...\n" << endl;
                break;
            }

        }
    }
}