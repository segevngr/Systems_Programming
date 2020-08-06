#include "../include/Watchable.h"
#include "../include/Session.h"
//region Watchable

//Constructor
Watchable::Watchable(long id, int length, const std::vector<std::string> &tags): id(id), length(length), tags(tags){}
//Destructor
Watchable::~Watchable() {}

const long Watchable::getId() const {
    return id;
}

const int Watchable::getLength() const {
    return length;
}

const std::vector <std::string> &Watchable::getTags() const {
    return tags;
}

//endregion

//region Movie

Movie::Movie(long id, const std::string &name, int length, const std::vector<std::string> &tags):
        Watchable(id,length,tags), name(name) {}

std::string Movie::toString() const {
    return name;
}

Watchable *Movie::getNextWatchable(Session &s) const {
    return s.getActiveUser()->getRecommendation(s);
}

std::string Movie::getName() const{
    return name;
}

Watchable *Movie::clone() const {
    return new Movie(*this);
}

//endregion

//region Episode

using namespace std;

Episode::Episode(long id, const std::string &seriesName, int length, int season, int episode,
                 const std::vector<std::string> &tags): Watchable(id,length,tags), seriesName(seriesName), season(season), episode(episode), nextEpisodeId(){}

std::string Episode::toString() const {
    return seriesName + " S" + to_string(season) + "E" + to_string(episode);
}


Watchable *Episode::getNextWatchable(Session &s) const {
    if(this->getId() < unsigned (s.getContent().size()-1) && this->seriesName == s.getContent()[s.getId()+1]->getName()){
            return s.getContent()[s.getId()];
        }
    return s.getActiveUser()->getRecommendation(s);
}
std::string Episode::getName() const{
    return seriesName;
}

Watchable *Episode::clone() const {
    return new Episode(*this);
}

//endregion

