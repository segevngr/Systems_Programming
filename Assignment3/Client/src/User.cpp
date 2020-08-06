//
// Created by omrigil@wincs.cs.bgu.ac.il on 09/01/2020.
//

#include "../include/User.h"
#include "../include/Book.h"
#include <vector>

User::User(string name) {
    this->name = name;
    receiptId = 0;
    subscriptionId = 0;
    this->inventory = vector<Book>();
    this->wishlist = vector<string>();
    this->receiptIdTopic = vector<pair<int, string>>();    // receipt|Topic
    this->subIdByTopic = vector<pair<int, string>>();
}

// Getters:

string User::getName() {
    return this->name;
}

int User::getReceiptId() {
    return receiptId;
}

int User::getSubscriptionId() {
    return subscriptionId;
}

string User::getTopicByReceiptId(int receiptId) {
    for (vector<pair<int, string>>::iterator it = receiptIdTopic.begin(); it != receiptIdTopic.end(); ++it)
        if (it->first == receiptId)
            return it->second;
}

int User::getSubIdByTopic(string topic) {
    for (vector<pair<int, string>>::iterator it = subIdByTopic.begin(); it != subIdByTopic.end(); ++it)
        if (it->second == topic)
            return it->first;
}


void User::incrementReceiptId() {
    receiptId++;
}

void User::incrementSubcriptionId() {
    subscriptionId++;
}


bool User::hasBook(string bookName) {
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookName() == bookName)
            return true;
    }
    return false;
}

string User::borrowedFrom(string bookName) {
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookName() == bookName)
            return inventory[i].getBorrowerName();
    }
    return nullptr;
}

void User::giveBook(string bookName) {
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookName() == bookName && inventory[i].isInInventory())
            inventory[i].bookBorrowed();
    }
}

void User::removeBook(string bookName) {
    vector<Book>::iterator toDelete;
    for (vector<Book>::iterator it = inventory.begin(); it != inventory.end(); ++it) {
        if (it.base()->getBookName() == bookName && it.base()->isInInventory())
            toDelete = it;
    }
    inventory.erase(toDelete);
}

void User::addToWishlist(string bookName) {
    wishlist.push_back(bookName);
}

void User::addReceiptId(int receiptId, string topic) {
    pair<int, string> tup(receiptId, topic);
    this->receiptIdTopic.push_back(tup);
}

void User::addSubId(int subId, string topic) {
    pair<int, string> tup(subId, topic);
    this->subIdByTopic.push_back(tup);
}

bool User::isReceiptExist(int receiptId) {
    for (vector<pair<int, string>>::iterator it = receiptIdTopic.begin(); it != receiptIdTopic.end(); ++it) {
        if (it->first == receiptId)
            return true;
    }
    return false;
}

bool User::isInWishlist(string bookName) {
    unsigned int i = 0;
    for (i = 0; i < wishlist.size(); i++) {
        if (wishlist[i] == bookName)
            return true;
    }
    return false;
}

void User::removeFromWishlist(string bookName) {
    vector<string>::iterator toDelete;
    for (vector<string>::iterator it = wishlist.begin(); it != wishlist.end(); ++it) {
        if (it.base()->c_str() == bookName)
            toDelete = it;
    }
    wishlist.erase(toDelete);
}

void User::addBookToInventory(string bookName, string topic, string borrowedFrom) {
    this->inventory.push_back(*new Book(bookName, topic, borrowedFrom));
}

void User::returnBook(string bookName) {
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookName() == bookName)
            inventory[i].bookReturned();
    }
}

bool User::isTopicInInventory(string topic) {
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookTopic() == topic && inventory[i].isInInventory())
            return true;
    }
    return false;
}

vector<string> User::getBooksByTopic(string topic) {
    vector<string> books;
    unsigned int i = 0;
    for (i = 0; i < inventory.size(); i++) {
        if (inventory[i].getBookTopic() == topic && inventory[i].isInInventory())
            books.push_back(inventory[i].getBookName());
    }
    return books;
}

void User::removeSubId(string topic) {
    vector<pair<int, string>>::iterator toDelete;
    for (vector<pair<int, string>>::iterator it = subIdByTopic.begin(); it != subIdByTopic.end(); ++it) {
        if (it->second == topic)
            toDelete = it;
    }
    subIdByTopic.erase(toDelete);
}

void User::removeBooksUnderTopic(string topic) {
    vector<Book> newInventory = vector<Book>();
    for (vector<Book>::iterator it = inventory.begin(); it != inventory.end(); ++it) {
        if (it.base()->getBookTopic() != topic)
            newInventory.push_back(*it.base());
    }
    inventory = newInventory;
}
