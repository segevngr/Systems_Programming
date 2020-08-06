#include <iostream>
#include <string>
#include <vector>
#include <boost/algorithm/string.hpp>

#include "../include/Listener.h"
#include "../include/Converter.h"

using namespace std;

Listener::Listener(ConnectionHandler *handler, User *user, bool *online) : handler(handler), user(user),
                                                                           online(online) {}

void Listener::run() {
    while (*online) {
        string message;
        StompFrame *answerFrame = new StompFrame;
        Converter *converter = new Converter;

        if (handler->getLine(message)) {

            cout << "Listener: " << endl;
            cout << message << endl << endl;

            StompFrame *frame = converter->stringToStompFrame(message);

            string body = frame->getFrameBody();

            if (frame->getCommand() == "CONNECTED")
                cout << "Login successful \n";

            // TODO ~Start of Borrow Flow~:

            // Bob wish to borrow Harry Potter
            if (body.find("wish to borrow") != string::npos) {
                string destination = frame->getHeaders().at("destination");
                string before = "borrow";
                int bookNamePos = body.find(before) + before.length() + 1;
                string bookName = body.substr(bookNamePos);

                if (user->hasBook(bookName)) {
                    answerFrame->setCommand("SEND");
                    answerFrame->addHeader("destination", destination);
                    answerFrame->setFrameBody(user->getName() + " has " + bookName);
                    string answerString = converter->StompFrameToString(*answerFrame);

                    if (!handler->sendLine(answerString)) {
                        cerr << "Disconnected. Existing...\n" << endl;
                        break;
                    }
                }
            }

            // Bob has Harry Potter
            if (body.find("has") != string::npos)
                if (!(body.find("has added") != string::npos)) {
                    string destination = frame->getHeaders().at("destination");
                    string bookName = body.substr(body.find("has")+4);
                    string bookOwner = body.substr(0, body.find("has")-1);

                    if (user->isInWishlist(bookName)) {
                        user->addBookToInventory(bookName, destination, bookOwner);
                        user->removeFromWishlist(bookName);

                        answerFrame->setCommand("SEND");
                        answerFrame->addHeader("destination", destination);
                        answerFrame->setFrameBody("Taking " + bookName + " from " + bookOwner);
                        string answerString = converter->StompFrameToString(*answerFrame);
                        if (!handler->sendLine(answerString)) {
                            cerr << "Disconnected. Existing...\n" << endl;
                            break;
                        }

                    } else // "has added"
                        cout << body << endl;
                }

            // Taking Harry Potter from Bob
            if (body.find("Taking") != string::npos) {   // updating the borrower data and the one who borrowed
                string destination = frame->getHeaders().at("destination");
                string str = body.substr(7);
                string bookName = str.substr(0, str.find("from")-1);
                string borrowedFrom = body.substr(body.find("from") + 5, body.length() - 1);

                cout << body << endl;

                // The borrower:
                if (user->getName() == borrowedFrom)
                    user->giveBook(bookName);
            }

            // TODO ~End of Borrow Flow~:

            // Returning Harry Potter to Bob
            if (body.find("Returning") != string::npos) {
                string destination = frame->getHeaders().at("destination");
                string str = body.substr(10);
                string bookName = str.substr(0, str.find("to")-1);
                string bookReciever = body.substr(body.find("to")+3);
                if (user->getName() == bookReciever)
                    user->returnBook(bookName);
            }

            // book status
            if (body.find("book status") != string::npos) {
                string toSend = "";
                string destination = frame->getHeaders().at("destination");

                if (user->isTopicInInventory(destination)) {
                    string books = "";
                    vector<string> booksVec = user->getBooksByTopic(destination);
                    for (int i = 0; i < booksVec.size(); i++)
                        books += booksVec[i] +", ";
                    if(books != "")
                        books = books.substr(0, books.length()-2);

                    answerFrame->setCommand("SEND");
                    answerFrame->addHeader("destination", destination);
                    answerFrame->setFrameBody(user->getName() + ":" + books);
                }

                string answerString = converter->StompFrameToString(*answerFrame);
                if (!handler->sendLine(answerString)) {
                    cerr << "Disconnected. Existing...\n" << endl;
                    break;
                }
            }

            // handle the second status case
            if (body.find(":") != string::npos) {
                cout << body << endl;
            }

            if (frame->getCommand() == "RECEIPT") {
                int curReceiptId = stoi(frame->getHeaders().at("receipt-id"));
                if (user->isReceiptExist(curReceiptId)) {
                    string topic = user->getTopicByReceiptId(curReceiptId);
                    if (topic == "LOGOUT") {
                        cout << "User has been logged out" << endl;
                        online = new bool(false);
                        handler->close();
                    } else if (topic.find("EXIT") != string::npos) {
                        string topicToUnsubscribe = topic.substr(5);
                        user->removeSubId(topicToUnsubscribe);
                        user->removeBooksUnderTopic(topicToUnsubscribe);
                        cout << "exit club " << topic;
                    } else
                        cout << "Joined club " << topic << endl;
                }
            }

            if (frame->getCommand() == "ERROR") {
                cerr << frame->getFrameBody();
                online = new bool(false);
                handler->close();
            }

        }
    }
}