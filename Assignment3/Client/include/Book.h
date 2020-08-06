//
// Created by omrigil@wincs.cs.bgu.ac.il on 09/01/2020.
//

#ifndef ASSIGNMENT3CLIENT_BOOK_H
#define ASSIGNMENT3CLIENT_BOOK_H

#include <string>

using namespace std;

class Book {
private:

    string name;
    string topic;
    bool exists;
    string borrowedFrom;



public:
    Book(string name, string topic, string borrowedFrom);
    Book();

    string getBorrowerName();
    string getBookName();
    void bookBorrowed();
    void bookReturned();
    string getBookTopic();
    bool isInInventory();
};


#endif //ASSIGNMENT3CLIENT_BOOK_H
