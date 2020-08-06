#include "../include/Session.h"
#include "../include/Action.h"
#include "../include/Watchable.h"
#include <sstream>


using namespace std;
//region BaseAction

BaseAction::BaseAction():errorMsg(""),status(PENDING) {}

BaseAction::~BaseAction() {}

ActionStatus BaseAction::getStatus() const {
    return status;
}

void BaseAction::complete() {
    status = COMPLETED;
}

void BaseAction::error(const std::string &errorMsg) {
    status = ERROR;
    this->errorMsg = errorMsg;
}

std::string BaseAction::getErrorMsg() const {
    return this->errorMsg;
}

std::string BaseAction::statusToString(ActionStatus AS) const {
    if(AS == COMPLETED){
        return "COMPLETED";
    }
    else{
        if(AS == ERROR){
            return "ERROR: " + this->getErrorMsg();
        }
        else{
            return "PENDING";
        }
    }
}



//endregion

//region CreateUser

void CreateUser::act(Session &sess) {
    string userName = sess.getWord(2);
    string recAlg = sess.getWord(3);
    unordered_map<string,User*>::const_iterator findName = sess.getUserMap().find(userName);
    if(findName != sess.getUserMap().end()){
        error("The new user name is already taken");
        cout <<this->toString()<<endl;
    }
    else {
        if (((recAlg != "len") & (recAlg != "rer") & (recAlg != "gen")) || recAlg.size() != 3) {
            error("Three letter code is invalid");
            cout <<this->toString()<<endl;
        }
        else{
            User* newUser;
            if(recAlg == "len"){
                newUser = new LengthRecommenderUser(userName);
            }
            if(recAlg == "rer"){
                newUser = new RerunRecommenderUser(userName);
            }
            if(recAlg == "gen"){
                newUser = new GenreRecommenderUser(userName);
            }
            sess.getUserMap()[userName] = newUser;
            complete();}
    }
    sess.getActionsLog().push_back(this);
}

std::string CreateUser::toString() const {
    string out = "CreateUser: ";
    return out + statusToString(this->getStatus());
}

BaseAction *CreateUser::clone() const {
    BaseAction* CU = new CreateUser(*this);
    return CU;
}

//endregion

//region ChangeActiveUser

void ChangeActiveUser::act(Session &sess) {
    string userName = sess.getWord(2);
    unordered_map<string,User*>::const_iterator findName = sess.getUserMap().find(userName);
    if(findName == sess.getUserMap().end()){
        error("User name " + userName + " doesn't exist");
        cout <<this->toString()<<endl;
    }
    else{
        sess.setActiveUser(findName->second);
        sess.getActiveUser()->setName(findName->first);
        complete();
    }
    sess.getActionsLog().push_back(this);
}

std::string ChangeActiveUser::toString() const {
    string out = "ChangeActiveUser: ";
    return  out + statusToString(getStatus());
}

BaseAction *ChangeActiveUser::clone() const {
    BaseAction* CAU = new ChangeActiveUser(*this);
    return CAU;
}

//endregion

//region DeleteUser

void DeleteUser::act(Session &sess) {
    string userName = sess.getWord(2);
    unordered_map<string,User*>::const_iterator findName = sess.getUserMap().find(userName);
    if(findName == sess.getUserMap().end()){
        error("User name " + userName + " doesn't exist");
        cout <<this->toString()<<endl;
    }
    else{
        sess.getUserMap().erase(findName);
        complete();
    }
    sess.getActionsLog().push_back(this);
}

std::string DeleteUser::toString() const {
    string out = "DeleteUser: ";
    return out + statusToString(this->getStatus());
}

BaseAction *DeleteUser::clone() const {
    BaseAction* DU = new DeleteUser(*this);
    return DU;
}

//endregion

//region DuplicateUser

void DuplicateUser::act(Session &sess) {
    string localUserName = sess.getWord(2);
    string dupUserName = sess.getWord(3);
    unordered_map<string,User*>::const_iterator findLocalName = sess.getUserMap().find(localUserName);
    unordered_map<string,User*>::const_iterator findDupName = sess.getUserMap().find(dupUserName);
    if(findLocalName == sess.getUserMap().end()){
       error("Original user " + localUserName + " doesn't exist");
        cout <<this->toString()<<endl;
    }
    else{
        if(findDupName != sess.getUserMap().end()){
            error("New user " + dupUserName + " is already taken");
            cout <<this->toString()<<endl;
        }
        else{
            User* dup(findLocalName->second->clone());
            dup->setName(dupUserName);
            sess.getUserMap()[dupUserName] = dup;
            complete();
        }
    }
    sess.getActionsLog().push_back(this);
}

std::string DuplicateUser::toString() const {
    string out = "DuplicateUser: ";
    return out + statusToString(this->getStatus());
}

BaseAction *DuplicateUser::clone() const {
    BaseAction* DU = new DuplicateUser(*this);
    return DU;
}

//endregion

//region PrintContentList

void PrintContentList::act(Session &sess) {
    vector<Watchable*> print = sess.getContent();
    long id;
    int length;
    vector<string> tags;
    string tagsPrint;
    for (int unsigned i = 0; i < print.size(); ++i) {
        id = print[i]->getId();
        length = print[i]->getLength();
        tags = print[i]->getTags();
        tagsPrint = tags[0];
        for (int unsigned j = 1; j <tags.size() ; ++j) {
            tagsPrint = tagsPrint +  "," + tags[j] ;
        }
        cout << to_string(id) + ". " + print[i]->toString() + " " + to_string(length) + " minutes [" + tagsPrint + "]" <<endl;
    }
    complete();
    sess.getActionsLog().push_back(this);
}

std::string PrintContentList::toString() const {
    string out = "PrintContentList: ";
    return out + statusToString(this->getStatus());
}

BaseAction *PrintContentList::clone() const {
    BaseAction* PCL = new PrintContentList(*this);
    return PCL;
}


//endregion

//region PrintWatchHistory

void PrintWatchHistory::act(Session &sess) {
    if(sess.getActiveUser()->get_history().size()!=0){
        cout << "Watch history for " + sess.getActiveUser()->getName() <<endl;
        for (int unsigned i = 0; i < sess.getActiveUser()->get_history().size(); ++i) {
            stringstream index;
            index << i+1;
            string outIndex = index.str();
            cout <<  outIndex + ". " + sess.getActiveUser()->get_history()[i]->toString() <<endl;
        }
        complete();
    }
    else{
        error("There is no watch history");
        this->toString();
    }
    sess.getActionsLog().push_back(this);
}

std::string PrintWatchHistory::toString() const {
    string out = "PrintWatchHistory: ";
    return out + statusToString(this->getStatus());
}

BaseAction *PrintWatchHistory::clone() const {
    BaseAction* PWH = new PrintWatchHistory(*this);
    return PWH;
}

//endregion

//region Watch

void Watch::act(Session &sess) {
    if(unsigned (sess.id)>unsigned (sess.getContent().size())){
        error("Invalid index input");
        cout <<this->toString()<<endl;
    }
    else{
        Watchable* now = sess.getContent()[sess.getId()-1];
        cout << "Watching " + now->toString() <<endl;
        sess.getActiveUser()->setHistory(now);
        sess.getActionsLog().push_back(this);
        Watchable* next = now->getNextWatchable(sess);
        cout << "We recommend watching " + next->toString() + ", continue watching? [y/n]" <<endl;
        string yorn;
        cin>>yorn;
        if(yorn=="y"){
            sess.setId(next->getId());
            BaseAction* BS = new Watch();
            BS->act(sess);
        }
    }
    complete();
}

std::string Watch::toString() const {
    string out = "Watch: ";
    return out + statusToString(this->getStatus());
}

BaseAction *Watch::clone() const {
    BaseAction* W = new Watch(*this);
    return W;
}

//endregion

//region PrintActionsLog

void PrintActionsLog::act(Session &sess) {
    for (BaseAction* B : sess.getActionsLog()) {
        cout << B->toString() <<endl;
    }
    complete();
    sess.getActionsLog().push_back(this);
}

std::string PrintActionsLog::toString() const {
    string out = "PrintActionsLog: ";
    return out + statusToString(this->getStatus());
}

BaseAction *PrintActionsLog::clone() const {
    BaseAction* PAL = new PrintActionsLog(*this);
    return PAL;
}

//endregion

//region Exit

void Exit::act(Session &sess) {
    complete();
}

std::string Exit::toString() const {
    string out = "Exit: ";
    return out + statusToString(this->getStatus());
}

BaseAction *Exit::clone() const {
    BaseAction* E = new Exit(*this);
    return E;
}
//endregion


