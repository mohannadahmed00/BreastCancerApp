import pyrebase
import datetime

x=0
config = {
    "apiKey": "AIzaSyD0h93yNNgfyMcsIH9pTChBBiq8nWPi5Ro",
    "authDomain": "breast-cancer-199344.firebaseapp.com",
    "databaseURL": "https://breast-cancer-199344.firebaseio.com",
    "projectId": "breast-cancer-199344",
    "storageBucket": "breast-cancer-199344.appspot.com",
    "messagingSenderId": "1021414509817",
    "appId": "1:1021414509817:web:ea25ed6614b7bb2468a628",
    "measurementId": "G-FSN4V2BWLZ"
}

firebase = pyrebase.initialize_app(config)



db = firebase.database()

def upload_img(img, folder):
    current_time = datetime.datetime.now()
    date = current_time.hour + current_time.minute + current_time.second + current_time.microsecond + current_time.day + current_time.month + current_time.year
    name = str(date) + 'jpg'
    storage = firebase.storage()
    img_url = storage.child(folder).child(name).put(img )
    url = storage.child(folder).child(name).get_url(img_url['downloadTokens'])
    return url

while True:
    if db.child("Queue").get().each() != None:
        all_samples = db.child("Queue").get()
        for user in all_samples.each():
            key = user.key()
            sample_id = db.child("Queue").child(key).get().val()
            user_id = db.child("Cancer Sample").child(sample_id).child("user id").get().val()
            link = db.child("Cancer Sample").child(sample_id).child("cancer image").get().val()

            enhancement = upload_img('enhanced.jpg', "Enhancement")  #
            segmenation = upload_img('segmented.jpg', "Segmentation")  #

            db.child("Cancer Sample").child(sample_id).child("diagnosis").set("1" )
            db.child("Queue").child(key).remove()
            print(
                "\n.\n.\n.\nqueue key: " + key + "\n" + "sample id: " + sample_id + "\n" + "user id: " + user_id + "\n" + "image result: " + "1" + "\n.\n.\n.\n")

            x = 0

    else:
        x = x + 1
        s = 'empty' + str(x)
        print(s)