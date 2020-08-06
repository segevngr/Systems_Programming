//
// Created by omrigil@wincs.cs.bgu.ac.il on 09/01/2020.
//

#ifndef ASSIGNMENT3CLIENT_USER_H
#define ASSIGNMENT3CLIENT_USER_H

#include <string>
#include <iostream>
#include <list>
#include <vector>
#include "Book.h"

using namespace std;

class User {

private:
    string name;
    vector<Book> inventory;
    vector<string> wishlist;
    vector<pair<int, string>> receiptIdTopic;
    int receiptId;
    int subscriptionId;

public:
    User(string name);
    vector<pair<int, string>> subIdByTopic;

    string borrowedFrom(string bookName);

    bool hasBook(string bookName);

    string getName();

    void giveBook(string bookName);

    void addToWishlist(string bookName);

    bool isInWishlist(string bookName);

    void removeFromWishlist(string bookName);

    void addBookToInventory(string bookName, string topic, string borrowedFrom);

    void addReceiptId(int receiptId, string topic);

    bool isReceiptExist(int receiptId);

    string getTopicByReceiptId(int receiptId);

    void removeBook(string bookName);

    void returnBook(string bookName);

    bool isTopicInInventory(string topic);

    vector<string> getBooksByTopic(string topic);

    int getReceiptId();

    int getSubscriptionId();

    void incrementReceiptId();

    void incrementSubcriptionId();

    void removeSubId(string topic);

    void removeBooksUnderTopic(string topic);

    vector<pair<int, string>> getSubIdTopic();

    void addSubId(int receiptId, string topic);

    string getSubIdByTopic(int subId);

    vector<pair<int, string>> getSubIdTopicVector();

    vector<pair<int, string>> getReceiptIdTopicVector();

    int getSubIdByTopic(string topic);
};


#endif //ASSIGNMENT3CLIENT_USER_H
