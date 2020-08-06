#include <iostream>
#include "../include/Book.h"

Book::Book(string name, string topic, string borrowedFrom) {
    this->name = name;
    this->topic = topic;
    this->exists = true;    // True: book in inventory: False: book is currently borrowed to another user
    this->borrowedFrom = borrowedFrom;
}

string Book::getBookName() {
    return this->name;
}

string Book::getBorrowerName() {
    return this->borrowedFrom;
}

void Book::bookBorrowed() {
    exists = false;
}

void Book::bookReturned() {
    exists = true;
}

string Book::getBookTopic() {
    return this->topic;
}

bool Book::isInInventory() {
    return exists;
}