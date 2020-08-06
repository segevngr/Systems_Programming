//
// Created by segevnag@wincs.cs.bgu.ac.il on 15/01/2020.
//

#ifndef ASSIGNMENT3CLIENT_CONVERTER_H
#define ASSIGNMENT3CLIENT_CONVERTER_H


#include "StompFrame.h"

class Converter {
public:
    StompFrame* stringToStompFrame(string msg);
    vector<string> stringToVec(string str, char delim);
    string StompFrameToString(StompFrame frame);
};


#endif //ASSIGNMENT3CLIENT_CONVERTER_H
