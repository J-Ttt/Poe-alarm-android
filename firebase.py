import pyrebase
import json
import requests
import time
from threading import Thread, RLock


values = {}
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

def saver():
    print("Saver on")
    global values
    while 1:
        name = "{}_poe.json".format(time.strftime("%d.%m.%Y_%H:%M:%S"))
        with open(name, 'w') as fp:
            json.dump(values, fp)
        lock.acquire()
        try:
            values = empty()
        finally:
            lock.release()
        time.sleep(3600)

def empty():
    values = {
        "Orb of Alteration" : {
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
        },
        "Orb of Fusing" : {
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
        },
        "Orb of Alchemy" : {
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
        },
        "Chaos Orb" : {
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
        },
        "Gemcutter's Prism" : {
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
        },
        "Exalted Orb" : {
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
        },
        "Chromatic Orb" : {
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
        },
        "Jeweller's Orb" : {
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
        },
        "Orb of Chance" : {
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
        },
        "Cartographer's Chisel" : {
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
        },
        "Orb of Scouring" : {
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
        },
        "Blessed Orb" : {
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
        },
        "Orb of Regret" : {
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
        },
        "Regal Orb" : {
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
        },
        "Divine Orb" : {
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
        },
        "Vaal Orb" : {
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
        },
        "Orb of Augmentation" : {
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
        },
        "Eternal Orb" : {
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
        },
        "Scroll of Wisdom" : {
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
        },
        "Scroll of Portal" : {
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
        },
        "Mirror of Kalandra" : {
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
        },
        "Silver Coin" : {
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
        },
        "Blacksmith's Whetstone" : {
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
        },
        "Armourer's Scrap" : {
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
        },
        "Glassblower's Bauble" : {
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
        },
        "Orb of transmutation" : {
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
        },
        "Apprentice Cartographer's Sextant" : {
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
        },
        "Journeyman Cartographer's Sextant" : {
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
        },
        "Master Cartographer's Sextant" : {
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
        },

    }
    return values




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
def main():
    numb = "109019874-114329213-107281917-123618169-115619434"
    id = "?id=" + numb
    sites = 0
    stashes = 0
    timeR = 0
    timeS = time.time()
    global values




    while 1:
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
                    if "note" in item.keys() and item["typeLine"] in values.keys():
                        #print("note: {}".format(item["note"]))
                        line = item["note"]
                        price = getPrice(line)
                        if price != None:
                            lock.acquire()
                            try:
                                values[item["typeLine"]][price[1]].append(price[0])
                                values[item["typeLine"]]["times"] += 1
                            finally:
                                lock.release()
                            print("{} is prized at {} {}".format(item["typeLine"], price[0], price[1]))


        id = "?id=" + data["next_change_id"]
        sites += 1

        print(data["next_change_id"])
        print("Stashes {}".format(len(data["stashes"])))
        timeR = time.time() - timeS
        print("Pages visited {}\nStashes mined: {}\nTime run: {}\n".format(sites, stashes, timeR))


if __name__ == "__main__":
    values = empty()
    t = Thread(target=main)
    time.sleep(2)
    t2 = Thread(target=saver)
    t.start()
    t2.start()
