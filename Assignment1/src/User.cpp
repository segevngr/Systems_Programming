#include "../include/User.h"
#include "../include/Watchable.h"
#include "../include/Session.h"
#include <vector>

using namespace std;
//region User

//Constructor
User::User(const std::string &name):history() ,indexHistory(0) ,name(name) {}

User::~User() {
    clear();
}
void User::clear() {}

std::string User::getName() const {
    return name;
}

std::vector<Watchable *> User::get_history() const {
    return history;
}

void User::setHistory(Watchable* getInHistory) {
     history.push_back(getInHistory);
}

void User::setName(const string &name) {
    User::name = name;
}

std::vector<Watchable *>& User::getHistoryByReference() {
    return history;
}






//endregion

//region LengthReccomenderUser

LengthRecommenderUser::LengthRecommenderUser(const std::string &name):User(name){}

Watchable *LengthRecommenderUser::getRecommendation(Session &s) {
    vector<Watchable *> history = this->get_history();
    int avgLength = 0;
    int sum = 0;
    for (int unsigned i = 0; i < history.size(); ++i) {
        sum = sum + history[i]->getLength();
    }
    avgLength = sum / history.size();
    int returnIndex = -1;
    int min = 10000;
    for (int unsigned j = 0; j < s.getContent().size(); ++j) {
        int tmp = abs(s.getContent()[j]->getLength() - avgLength);
        if (tmp < min) {
            if (!isInHistory(s.getContent()[j])) {
                min = tmp;
                returnIndex = j;
            }
        }
    }
    if (returnIndex == -1) {
        return nullptr;
    }
    return s.getContent()[returnIndex];
}

bool LengthRecommenderUser::isInHistory(Watchable* toCheck) {
    bool ifHistory = false;
    for (int unsigned i = 0; i < ((this->get_history().size()) & !ifHistory); ++i) {
        if(this->get_history()[i] == toCheck){
            ifHistory = true;
        }
    }
    return ifHistory;
}

User *LengthRecommenderUser::clone() const {
    LengthRecommenderUser* toReturn = new LengthRecommenderUser(getName());
    toReturn->indexHistory = indexHistory;
    return toReturn;
}

//endregion

//region RerunReccomender

RerunRecommenderUser::RerunRecommenderUser(const std::string &name) : User(name) {}

Watchable *RerunRecommenderUser::getRecommendation(Session &s) {
    Watchable *recommendation;
    recommendation = this->get_history()[indexHistory];
    indexHistory = (indexHistory + 1) % history.size();
    return recommendation;
}


User *RerunRecommenderUser::clone() const {
    RerunRecommenderUser* toReturn = new RerunRecommenderUser(getName());
    toReturn->indexHistory = indexHistory;
    return toReturn;
}
//endregion

//region GenereReccomenderUser

GenreRecommenderUser::GenreRecommenderUser(const std::string &name) : User(name) {}

Watchable *GenreRecommenderUser::getRecommendation(Session &s) {
    string tag, popTag;
    Watchable *con;
    bool tagFound = false;

    // Store and sort tags view count in a vector of pairs:
    vector<pair<int, string> > tagsViewCount;
    for (int unsigned i = 0; i < this->get_history().size(); i++) {
        for (int unsigned j = 0; j < this->get_history()[i]->getTags().size(); j++) {
            tag = this->get_history()[i]->getTags()[j];
            tagFound = false;
            for (int unsigned k = 0; k < tagsViewCount.size(); k++) {
                if (tagsViewCount[k].second == tag) {
                    tagsViewCount[k].first--; // Sort is in ascending order and needs to be reversed
                    tagFound = true;
                }
            }
            if (!tagFound) {
                pair<int, string> newPair(0, tag);
                tagsViewCount.push_back(newPair);
            }
        }
    }
    sort(tagsViewCount.begin(), tagsViewCount.end());

    // Looks for popular tag content which isn't in history
    for (int unsigned i = 0; i < tagsViewCount.size(); i++) {
        popTag = tagsViewCount[i].second;
        for (int unsigned j = 0; j < s.getContent().size(); j++) {   //content iterator
            con = s.getContent()[j];
            for (int unsigned k = 0; k < con->getTags().size(); k++) { //tags iterator
                if (con->getTags()[k] == popTag)
                    if (!isInHistory(con))
                        return con;
            }
        }
    }
    return nullptr;
}


bool GenreRecommenderUser::isInHistory(Watchable* toCheck) {
    bool ifHistory = false;
    for (int unsigned i = 0; i < ((this->get_history().size()) & !ifHistory); ++i) {
        if(this->get_history()[i] == toCheck){
            ifHistory = true;
        }
    }
    return ifHistory;
}


User *GenreRecommenderUser::clone() const {
    GenreRecommenderUser* toReturn = new GenreRecommenderUser(getName());
    toReturn->indexHistory = indexHistory;
    return toReturn;
}
//endregion
