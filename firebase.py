import pyrebase
import json
import requests
import time
from threading import Thread, RLock, Event
import numpy as np






values = {}
exit = Event()



def authData():
    data = open("auths.json")
    jdata = json.load(data)
    data.close()
    return jdata

def configure():
    config = authData()
    firebase = pyrebase.initialize_app(config)
    return firebase

def getAll(db):
    base = db.get()
    return base.val()

def setData(db, childname, data):
    db.child(childname).set(data)

def updateData(db, childname, data):
    db.child(childname).update(data)

def getPrice(note):
    note = note.split(" ")
    if note[-1] in followed and len(note) > 2:
        price = note[-2]
        if price.isdigit():
            return (int(price), note[-1])
        elif len(price.split("/")) == 2:
            price = price.split("/")
            numerator = price[0]
            denominator = price[1]
            if numerator.isdigit() and denominator.isdigit():
                return (int(numerator)/int(denominator), note[-1])
    return None

def saver(exit):
    print("Saver on")
    global values
    while not exit.is_set():
        name = "{}_poe.json".format(time.strftime("%d-%m-%Y_%H-%M-%S"))
        print("wrote to file")
        with open(name, 'w') as fp:
            json.dump(values, fp)
        lock.acquire()
        try:
            values = empty()
        finally:
            lock.release()

        exit.wait(3600)
    print("saver dead")

def empty():
    curs = {
                "times" : 0,
                'chaos' : [],
                'exa' : [],
                'alch' : [],
                'chisel' : [],
                'fuse': [],
                'alt' : [],
                'vaal' : [],
                'exalted' : [],
                'jew' : []
    }
    everything = {
        "Orb of Alteration" : curs.copy(),
        "Orb of Fusing" : curs.copy(),
        "Orb of Alchemy" : curs.copy(),
        "Chaos Orb" : curs.copy(),
        "Gemcutter's Prism" : curs.copy(),
        "Exalted Orb" : curs.copy(),
        "Chromatic Orb" : curs.copy(),
        "Jeweller's Orb" : curs.copy(),
        "Orb of Chance" : curs.copy(),
        "Cartographer's Chisel" : curs.copy(),
        "Orb of Scouring" : curs.copy(),
        "Blessed Orb" : curs.copy(),
        "Orb of Regret" : curs.copy(),
        "Regal Orb" : curs.copy(),
        "Divine Orb" : curs.copy(),
        "Vaal Orb" : curs.copy(),
        "Orb of Augmentation" : curs.copy(),
        "Eternal Orb" : curs.copy(),
        "Scroll of Wisdom" : curs.copy(),
        "Scroll of Portal" : curs.copy(),
        "Mirror of Kalandra" : curs.copy(),
        "Silver Coin" : curs.copy(),
        "Blacksmith's Whetstone" : curs.copy(),
        "Armourer's Scrap" : curs.copy(),
        "Glassblower's Bauble" : curs.copy(),
        "Orb of transmutation" : curs.copy(),
        "Apprentice Cartographer's Sextant" : curs.copy(),
        "Journeyman Cartographer's Sextant" : curs.copy(),
        "Master Cartographer's Sextant" : curs.copy()

    }
    leagues = ["Standard", "Hardcore", "SSF Harbinger HC", "Hardcore Harbinger", "SSF Harbinger", "Harbinger"]
    finale = {}
    for name in leagues:
        finale[name] = everything.copy()
    return finale




followed = ['chaos', 'exa', 'alch', 'chisel', 'fuse', 'alt', 'vaal', 'exalted', 'jew']
converted = {
            'chaos' : "Chaos Orb",
            'exa' : "Exalted Orb",
            'alch' : "Orb of Alchemy",
            'chisel' : "Cartographer's Chisel",
            'fuse': "Orb of Fusing",
            'alt' : "Orb of Alteration",
            'vaal' : "Vaal Orb",
            'exalted' : "Exalted Orb",
            'jew' : "Jeweller's Orb"

}
lock = RLock()
def main(exit):
    numb = "109120935-114428894-107376749-123741778-115720700"
    id = "?id=" + numb
    sites = 0
    stashes = 0
    timeR = 0
    timeS = time.time()
    global values




    while not exit.is_set():
        try:
            resp = requests.get("http://api.pathofexile.com/public-stash-tabs" + id)
            print("got some")
        except requests.exceptions.RequestException as e:  # This is the correct syntax
            print("ERROR OCCURED REQUESTING THE SITE, TRYING AGAIN IN 2 SECONDS")
            time.sleep(2)
            continue
        data = json.loads(resp.text)
        if not "next_change_id" in data.keys():
            print("HIT ROCK BOTTOM, TRYING AGAIN")
            continue
        stashes += len(data["stashes"])
        for stash in data["stashes"]:
            if stash["public"]:
                for item in stash["items"]:
                    if "note" in item.keys() and item["typeLine"] in values["Harbinger"].keys():
                        #print("note: {}".format(item["note"]))
                        line = item["note"]
                        price = getPrice(line)
                        if price != None:
                            lock.acquire()
                            try:
                                values[item["league"]][item["typeLine"]][price[1]].append(price[0])
                                values[item["league"]][item["typeLine"]]["times"] += 1
                            except KeyError:
                                with open("errors.txt", "a") as kohde:
                                    kohde.write("league: {}\ntypeline: {}\nprice1: {}".format(item["league"], item["typeLine"], price[1]))
                                lock.release()
                                continue
                            finally:
                                lock.release()
                            print("{} is prized at {} {}".format(item["typeLine"], price[0], price[1]))


        id = "?id=" + data["next_change_id"]
        sites += 1


        print(data["next_change_id"])
        print("Stashes {}".format(len(data["stashes"])))
        timeR = time.time() - timeS
        print("Pages visited {}\nStashes mined: {}\nTime run: {}\n".format(sites, stashes, timeR))

def findleague(exit):
    numb = "109116329-114424267-107372105-123736579-115715272"
    id = "?id=" + numb
    sites = 0
    stashes = 0
    leagues = {}
    while not exit.is_set():
        try:
            resp = requests.get("http://api.pathofexile.com/public-stash-tabs" + id)
            print("got some")
        except requests.exceptions.RequestException as e:  # This is the correct syntax
            print("ERROR OCCURED REQUESTING THE SITE, TRYING AGAIN IN 2 SECONDS")
            time.sleep(2)
            continue
        data = json.loads(resp.text)
        if not "next_change_id" in data.keys():
            print("HIT ROCK BOTTOM, TRYING AGAIN")
            continue
        for stash in data["stashes"]:
            if stash["public"]:
                for item in stash["items"]:
                    if item["league"] not in leagues.keys():
                        leagues[item["league"]] = 1
                    else:
                        leagues[item["league"]] += 1

        stashes += len(data["stashes"])
        id = "?id=" + data["next_change_id"]
        sites += 1
        print(data["next_change_id"])
        print("Stashes {}".format(len(data["stashes"])))
        for i in leagues:
            print("League: {}\n\t{}".format(i, leagues[i]))
        print("Pages visited {}\nStashes mined: {}\n".format(sites, stashes))

def avarages(data):
    avrg = {}
    for league in data:
        avrg[league] = {}
        for item in data[league]:
            chaos = data[league][item]["chaos"]
            chaos = np.array(chaos)
            if len(chaos) != 0:
                mean = np.mean(chaos)
                std = np.std(chaos)
            else:
                mean = 0
            if mean == 0:
                avrg[league][item] = mean
            else:
                final = [x for x in chaos if (x > mean - 1.5 * std)]
                final = [x for x in final if (x < mean + 1.5 * std)]
                avrg[league][item] = np.mean(np.array(final))

    return avrg

def postData(data):
    pass

if __name__ == "__main__":

    """values = empty()
    t = Thread(target=main,  args=(exit,))
    t2 = Thread(target=saver,  args=(exit,))
    t.start()
    t2.start()
    while 1:
        try:
            time.sleep(1)
        except KeyboardInterrupt:
            exit.set()
            break"""
    data = json.load(open("22-11-2017_16-56-33_poe.json", "r"))
    data = avarages(data)
    with open("tulokset.json", 'w') as fp:
        json.dump(data, fp)
    firebase = configure()
    db = firebase.database()
    db.set({"Harbinger" : data["harbinger"]})
