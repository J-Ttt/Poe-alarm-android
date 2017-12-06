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

def getYear(league, item, db):
    year = db.child(league).child(item).child("Year").get()
    return year.val()

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
    print("got db")
    date = time.strftime("%d-%m-%Y")

    with open("22-11-2017_16-56-33_poe.json", "r") as kohde:
        data = json.load(kohde)

    """tester = {"0": "Tämä on stringi", 0:"Tämä on integer"}
    db.child("test").child("intString").set(tester)"""

    avr = avarages(data)
    harb = avr["Harbinger"]
    for x in range(3):
        for item in avr["Harbinger"]:
            day = getday("test", item, db)

            month = getMonth("test", item, db)
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
            #print(day)
            day[0] = avr["Harbinger"][item]
            now = datetime.datetime.now()
            if now.day == 1 and now.hour == 1:
                year = getYear("test", item, db)
                if year == None:
                    year = {}
                total = 0
                if now.month == 1:
                    lastDays = abs((datetime.date(now.year-1, 12, now.day) - datetime.date(now.year, now.month, now.day)).days)
                else:
                    lastDays = abs((datetime.date(now.year, now.month-1, now.day) - datetime.date(now.year, now.month, now.day)).days)
                for dates in range(lastDays):
                    total += month[str(dates)][0]
                totalAvr = total/lastDays
                print(lastDays, total)
                monthMark = time.strftime("%m-%Y")
                year[monthMark] = totalAvr
                db.child("test").child(item).child("Year").set(year)
            if True:
                temp = month["0"]
                for dayM in range(30):
                    temp2 = month[str(dayM +1)]
                    month[str(dayM + 1)] = temp
                    temp = temp2
                month[0] = avr["Harbinger"][item]
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

            db.child("test").child(item).child("Day").set(day)
            db.child("test").child(item).child("Month").set(month)
