import pyrebase
import json
import time
import numpy as np
import datetime

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
    return db.child(league).child(item).child("Day").get().val()


def getMonth(league, item, db):
    return db.child(league).child(item).child("Month").get().val()

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
                avrg[league][item] = (mean, 0 )
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
    for item in avr["Harbinger"]:
        day = getday("test", item, db)
        month = getMonth("test", item, db)
        if day == None or len(day) != 72:
            day = {}
            month = {}
            for i in range(72):
                day[i] = (0,0)
            for a in range(31):
                month[a] = (0,0)
        for hour in range(71):
            temp = day[hour + 1]
            day[hour + 1] = day[hour]

        day[0] = avr["Harbinger"][item]
        now = datetime.datetime.now()
        if now.hour == 1:
            for dayM in range(30):
                temp = month[dayM + 1]
                month[dayM + 1] = month[dayM]
            month[0] = avr["Harbinger"][item]
        elif now.hour != 0:
            avrg = 0
            kpl = 0
            for i in range(24):
                avrg += day[i][0]
                kpl += day[i][1]
            avrg = avrg/now.hour
            month[0] = (avrg, kpl)
        else:
            avrg = 0
            for i in range(24):
                avrg += day[i][0]
            avrg = avrg/24
            month[0] = avrg
        dailyA = 0
        monthlyA = 0
        dayH = -float("Inf")
        dayL = float("Inf")
        monthH = -float("Inf")
        monthL = float("Inf")
        for i in day:
            dailyA += day[i][0]
            if day[i][0] < dayL:
                dayL = day[i][0]

            if day[i][0] > dayH:
                dayH = day[i][0]

        for i in month:
            monthlyA += month[i][0]
            if month[i][0] < monthL:
                monthL = month[i][0]
            if month[i][0] > monthH:
                monthH = month[i][0]
        dailyA = dailyA/72
        monthlyA = monthlyA/31
        day["avrg"] = dailyA
        day["Highest"] = dayH
        day["Lowest"] = dayL
        month["avrg"] = monthlyA
        month["Highest"] = monthH
        month["Lowest"] = monthL

        db.child("test").child(item).child("Day").set(day)
        db.child("test").child(item).child("Month").set(month)

    """for item in avr["Harbinger"]:
        day = {}
        month = {}
        for i in range(24):
            day[i] = (0,0)
        for a in range(31):
            month[a] = (0,0)
        db.child("test").child(item).child("Day").set(day)
        db.child("test").child(item).child("Month").set(month)"""
