import pyrebase
import json

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


if __name__ == "__main__":
    firebase = configure()
    db = firebase.database()
    data = {"Olvi" : {"name" : "Olvi", "price" : "4.48e/l", "taste" : "is Okay"}, "Karjala" : {"name" : "karjala", "price" : "2.26e/l", "taste" : "wtf"}}
    setData(db, "kaljaa", data)
