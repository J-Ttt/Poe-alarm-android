import pyrebase
import json
import requests
import time
from threading import Thread, RLock, Event
import numpy as np
from copy import deepcopy
import datetime






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

def getday(league, item, db):
    day = db.child(league).child(item).child("Day").get()
    return day.val()

def getMonth(league, item, db):
    month = db.child(league).child(item).child("Month").get()
    return month.val()

def getYear(league, item, db):
    year = db.child(league).child(item).child("Year").get()
    return year.val()

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
            if numerator.isdigit() and denominator.isdigit() and int(denominator) != 0:
                return (int(numerator)/int(denominator), note[-1])
    return None

def saver(exit):
    print("Saver on")
    global values
    while not exit.is_set():
        exit.wait(3600)
        if not exit.is_set():
            date = time.strftime("%d-%m-%Y")
            timestamp = time.strftime("%H-%M-%S")
            name = "{}_poe.json".format(time.strftime("%d-%m-%Y_%H-%M-%S"))
            print("Send to firebase")
            """with open(name, 'w') as fp:
                json.dump(values, fp)"""
            kopio = deepcopy(values)
            lock.acquire()
            try:
                values = empty()
            finally:
                lock.release()
            firebase = configure()
            db = firebase.database()
            avr = avarages(kopio)
            now = datetime.datetime.now()
            for league in avr:
                db.child(league).child(date).child(timestamp).set(avr[league])
                print("{} saved".format(league))
                for item in avr[league]:
                    day = getday(league, item, db)
                    month = getMonth(league, item, db)
                    if day == None or len(day) != 75:
                        day = {}
                        month = {}
                        for i in range(72):
                            day[str(i)] = (0,0)
                        for a in range(31):
                            month[str(a)] = (0,0)
                    temp = day["0"]
                    for hour in range(71):
                        temp2 = day[str(hour +1)]
                        day[str(hour +1)] = temp
                        temp = temp2

                    day[0] = avr[league][item]
                    if now.day == 1 and now.hour == 1:
                        year = getYear(league, item, db)
                        if year == None:
                            year = {}
                        total = 0
                        if now.month == 1:
                            lastDays = (datetime.date(now.year-1, 12, now.day) - datetime.date(now.year, now.month, now.day)).days
                        else:
                            lastDays = (datetime.date(now.year, now.month-1, now.day) - datetime.date(now.year, now.month, now.day)).days
                        for day in range(lastDays):
                            total += month[day][0]
                        totalAvr = total/lastDays
                        monthMark = time.strftime("%m-%Y")
                        year[monthMark] = totalAvr
                        db.child(league).child(item).child("Year").set(year)

                    if now.hour == 1:
                        for dayM in range(30):
                            temp = month[dayM + 1]
                            month[dayM + 1] = month[dayM]
                        month[0] = avr[league][item]
                    elif now.hour != 0:
                        avrg = 0
                        kpl = 0
                        for i in range(24):
                            avrg += day[str(i)][0]
                            kpl += day[str(i)][1]
                        avrg = avrg/now.hour
                        month[0] = (avrg, kpl)
                    else:
                        avrg = 0
                        for i in range(24):
                            avrg += day[str(i)][0]
                        avrg = avrg/24
                        month[0] = avrg
                    dailyA = 0
                    monthlyA = 0
                    dayH = -float("Inf")
                    dayL = float("Inf")
                    monthH = -float("Inf")
                    monthL = float("Inf")
                    for i in range(72):
                        dailyA += day[str(i)][0]
                        if day[str(i)][0] < dayL:
                            dayL = day[str(i)][0]

                        if day[str(i)][0] > dayH:
                            dayH = day[str(i)][0]

                    for i in range(31):
                        monthlyA += month[str(i)][0]
                        if month[str(i)][0] < monthL:
                            monthL = month[str(i)][0]
                        if month[str(i)][0] > monthH:
                            monthH = month[str(i)][0]
                    dailyA = dailyA/72
                    monthlyA = monthlyA/31
                    day["avrg"] = dailyA
                    day["Highest"] = dayH
                    day["Lowest"] = dayL
                    month["avrg"] = monthlyA
                    month["Highest"] = monthH
                    month["Lowest"] = monthL

                    db.child(league).child(item).child("Day").set(day)
                    db.child(league).child(item).child("Month").set(month)


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
        "Orb of Alteration" : deepcopy(curs),
        "Orb of Fusing" : deepcopy(curs),
        "Orb of Alchemy" : deepcopy(curs),
        "Chaos Orb" : deepcopy(curs),
        "Gemcutter's Prism" : deepcopy(curs),
        "Exalted Orb" : deepcopy(curs),
        "Chromatic Orb" : deepcopy(curs),
        "Jeweller's Orb" : deepcopy(curs),
        "Orb of Chance" : deepcopy(curs),
        "Cartographer's Chisel" : deepcopy(curs),
        "Orb of Scouring" : deepcopy(curs),
        "Blessed Orb" : deepcopy(curs),
        "Orb of Regret" : deepcopy(curs),
        "Regal Orb" : deepcopy(curs),
        "Divine Orb" : deepcopy(curs),
        "Vaal Orb" : deepcopy(curs),
        "Orb of Augmentation" : deepcopy(curs),
        "Eternal Orb" : deepcopy(curs),
        "Scroll of Wisdom" : deepcopy(curs),
        "Scroll of Portal" : deepcopy(curs),
        "Mirror of Kalandra" : deepcopy(curs),
        "Silver Coin" : deepcopy(curs),
        "Blacksmith's Whetstone" : deepcopy(curs),
        "Armourer's Scrap" : deepcopy(curs),
        "Glassblower's Bauble" : deepcopy(curs),
        "Orb of transmutation" : deepcopy(curs),
        "Apprentice Cartographer's Sextant" : deepcopy(curs),
        "Journeyman Cartographer's Sextant" : deepcopy(curs),
        "Master Cartographer's Sextant" : deepcopy(curs)

    }
    leagues = ["Standard", "Hardcore", "SSF Harbinger HC", "Hardcore Harbinger", "SSF Harbinger", "Harbinger"]
    finale = {}
    for name in leagues:
        finale[name] = deepcopy(everything)
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
    print("Reader on")
    numb = "110246968-115636396-108476299-125029748-116875259"
    id = "?id=" + numb
    sites = 0
    stashes = 0
    timeR = 0
    timeS = time.time()
    global values




    while not exit.is_set():
        try:
            resp = requests.get("http://api.pathofexile.com/public-stash-tabs" + id)
            #print("got some")
        except requests.exceptions.RequestException as e:  # This is the correct syntax
            print("ERROR OCCURED REQUESTING THE SITE, TRYING AGAIN IN 2 SECONDS")
            exit.wait(2)
            continue
        data = json.loads(resp.text)
        if not "next_change_id" in data.keys():
            print("HIT ROCK BOTTOM, TRYING AGAIN")
            exit.wait(2)
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
                                """with open("errors.txt", "a") as kohde:
                                    kohde.write("league: {}\ntypeline: {}\nprice1: {}\n".format(item["league"], item["typeLine"], price[1]))"""
                                #needs checks if the error is new, getting the same error written milion times now
                                continue
                            finally:
                                lock.release()
                            #print("{} is prized at {} {}".format(item["typeLine"], price[0], price[1]))


        id = "?id=" + data["next_change_id"]
        sites += 1

        if sites % 100 == 0:
            timeR = time.time() - timeS
            for i in values:
                print("{}:".format(i))
                for item in values[i]:
                    print("\t{}: {}".format(item, values[i][item]["times"]))
            print(data["next_change_id"])
            print("Stashes {}".format(len(data["stashes"])))
            print("Pages visited {}\nStashes mined: {}\nTime run: {}\n".format(sites, stashes, timeR))
    print("reader dead")

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
            if item == "Chaos Orb":
                avrg[league][item] = (1, len(data[league][item]["chaos"]))

            chaos = data[league][item]["chaos"]
            chaos = np.array(chaos)
            if len(chaos) != 0:
                mean = np.mean(chaos)
                std = np.std(chaos)
            else:
                mean = 0
                avrg[league][item] = (mean, 0)

            final = [x for x in chaos if (x > mean - 1.5 * std)]
            final = [x for x in final if (x < mean + 1.5 * std)]
            if len(final) != 0:
                avrg[league][item] = (np.mean(np.array(final)), len(final))
            else:
                avrg[league][item] = (mean, 0)

    return avrg

def postData(data):
    pass

if __name__ == "__main__":

    values = empty()

    t = Thread(target=main,  args=(exit,))
    t2 = Thread(target=saver,  args=(exit,))
    t.start()
    t2.start()
    while 1:
        try:
            time.sleep(1)
        except KeyboardInterrupt:
            exit.set()
            break
    """data = json.load(open("22-11-2017_16-56-33_poe.json", "r"))
    data = avarages(data)
    with open("tulokset.json", 'w') as fp:
        json.dump(data, fp)
    firebase = configure()
    db = firebase.database()
    db.set({"Harbinger" : data["harbinger"]})"""
