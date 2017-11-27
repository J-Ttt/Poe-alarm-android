import pyrebase
import json
import time
import numpy as np

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
    return db.child(league).child(item).child("Day").get()


def getMonth(league, item, db):
    return db.child(league).child(item).child("Month").get()

def setData(db, childname, data):
    db.child(childname).set(data)

def updateData(db, childname, data):
    db.child(childname).update(data)

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
            if mean == 0:
                avrg[league][item] = mean
            else:
                final = [x for x in chaos if (x > mean - 1.5 * std)]
                final = [x for x in final if (x < mean + 1.5 * std)]
                avrg[league][item] = (np.mean(np.array(final)), len(final))

    return avrg

if __name__ == "__main__":
    firebase = configure()
    db = firebase.database()
    date = time.strftime("%d-%m-%Y")

    with open("22-11-2017_16-56-33_poe.json", "r") as kohde:
        data = json.load(kohde)

    avr = avarages(data)
    harb = avr["Harbinger"]
    for item in harb:
        for i in range(31):
            db.child("test").child(item).child("Month").child(i).set((0, 0))
        for i in range(24):
            db.child("test").child(item).child("Day").child(i).set((0, 0))
        db.child("test").child(item).child("Year").child(time.strftime("%Y")).set((0, 0))
