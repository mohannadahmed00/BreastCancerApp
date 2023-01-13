import numpy as np
import urllib.request
import cv2
import pyrebase
import math as mt
import math
import imutils
import pandas as pd
import skimage.io as io
from matplotlib import pyplot as plt
import datetime

enhancement = ""
segmenation = ""
x = 0


#bluring function
def blurfun(data):
    new_image = data.copy()
    image2 = cv2.cvtColor(new_image, cv2.COLOR_BGR2GRAY)
    figure_size = 9
    image2 = cv2.GaussianBlur(image2, (figure_size, figure_size),0)
    return image2


# #bluring function
# def blurfun(data):
#     new_image = data.copy()
#     image = cv2.cvtColor(np.array(new_image), cv2.COLOR_BGR2GRAY)
#     figure_size = 9
#     image2 = cv2.blur(image,(figure_size, figure_size))
#     return image2
# times roman
# 16
# 14

# Speckle Noise removing function
def crimmins(data):
    data = blurfun(data)
    new_image = data.copy()
    nrow = len(data)
    ncol = len(data[0])

    # Dark pixel adjustment

    # First Step
    # N-S
    for i in range(1, nrow):
        for j in range(ncol):
            if data[i - 1, j] >= (data[i, j] + 2):
                new_image[i, j] += 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(ncol - 1):
            if data[i, j + 1] >= (data[i, j] + 2):
                new_image[i, j] += 1
    data = new_image
    # NW-SE
    for i in range(1, nrow):
        for j in range(1, ncol):
            if data[i - 1, j - 1] >= (data[i, j] + 2):
                new_image[i, j] += 1
    data = new_image
    # NE-SW
    for i in range(1, nrow):
        for j in range(ncol - 1):
            if data[i - 1, j + 1] >= (data[i, j] + 2):
                new_image[i, j] += 1
    data = new_image
    # Second Step
    # N-S
    for i in range(1, nrow - 1):
        for j in range(ncol):
            if (data[i - 1, j] > data[i, j]) and (data[i, j] <= data[i + 1, j]):
                new_image[i, j] += 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol - 1):
            if (data[i, j + 1] > data[i, j]) and (data[i, j] <= data[i, j - 1]):
                new_image[i, j] += 1
    data = new_image
    # NW-SE
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i - 1, j - 1] > data[i, j]) and (data[i, j] <= data[i + 1, j + 1]):
                new_image[i, j] += 1
    data = new_image
    # NE-SW
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i - 1, j + 1] > data[i, j]) and (data[i, j] <= data[i + 1, j - 1]):
                new_image[i, j] += 1
    data = new_image
    # Third Step
    # N-S
    for i in range(1, nrow - 1):
        for j in range(ncol):
            if (data[i + 1, j] > data[i, j]) and (data[i, j] <= data[i - 1, j]):
                new_image[i, j] += 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol - 1):
            if (data[i, j - 1] > data[i, j]) and (data[i, j] <= data[i, j + 1]):
                new_image[i, j] += 1
    data = new_image
    # NW-SE
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i + 1, j + 1] > data[i, j]) and (data[i, j] <= data[i - 1, j - 1]):
                new_image[i, j] += 1
    data = new_image
    # NE-SW
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i + 1, j - 1] > data[i, j]) and (data[i, j] <= data[i - 1, j + 1]):
                new_image[i, j] += 1
    data = new_image
    # Fourth Step
    # N-S
    for i in range(nrow - 1):
        for j in range(ncol):
            if (data[i + 1, j] >= (data[i, j] + 2)):
                new_image[i, j] += 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol):
            if (data[i, j - 1] >= (data[i, j] + 2)):
                new_image[i, j] += 1
    data = new_image
    # NW-SE
    for i in range(nrow - 1):
        for j in range(ncol - 1):
            if (data[i + 1, j + 1] >= (data[i, j] + 2)):
                new_image[i, j] += 1
    data = new_image
    # NE-SW
    for i in range(nrow - 1):
        for j in range(1, ncol):
            if (data[i + 1, j - 1] >= (data[i, j] + 2)):
                new_image[i, j] += 1
    data = new_image

    # Light pixel adjustment

    # First Step
    # N-S
    for i in range(1, nrow):
        for j in range(ncol):
            if (data[i - 1, j] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(ncol - 1):
            if (data[i, j + 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # NW-SE
    for i in range(1, nrow):
        for j in range(1, ncol):
            if (data[i - 1, j - 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # NE-SW
    for i in range(1, nrow):
        for j in range(ncol - 1):
            if (data[i - 1, j + 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # Second Step
    # N-S
    for i in range(1, nrow - 1):
        for j in range(ncol):
            if (data[i - 1, j] < data[i, j]) and (data[i, j] >= data[i + 1, j]):
                new_image[i, j] -= 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol - 1):
            if (data[i, j + 1] < data[i, j]) and (data[i, j] >= data[i, j - 1]):
                new_image[i, j] -= 1
    data = new_image
    # NW-SE
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i - 1, j - 1] < data[i, j]) and (data[i, j] >= data[i + 1, j + 1]):
                new_image[i, j] -= 1
    data = new_image
    # NE-SW
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i - 1, j + 1] < data[i, j]) and (data[i, j] >= data[i + 1, j - 1]):
                new_image[i, j] -= 1
    data = new_image
    # Third Step
    # N-S
    for i in range(1, nrow - 1):
        for j in range(ncol):
            if (data[i + 1, j] < data[i, j]) and (data[i, j] >= data[i - 1, j]):
                new_image[i, j] -= 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol - 1):
            if (data[i, j - 1] < data[i, j]) and (data[i, j] >= data[i, j + 1]):
                new_image[i, j] -= 1
    data = new_image
    # NW-SE
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i + 1, j + 1] < data[i, j]) and (data[i, j] >= data[i - 1, j - 1]):
                new_image[i, j] -= 1
    data = new_image
    # NE-SW
    for i in range(1, nrow - 1):
        for j in range(1, ncol - 1):
            if (data[i + 1, j - 1] < data[i, j]) and (data[i, j] >= data[i - 1, j + 1]):
                new_image[i, j] -= 1
    data = new_image
    # Fourth Step
    # N-S
    for i in range(nrow - 1):
        for j in range(ncol):
            if (data[i + 1, j] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # E-W
    for i in range(nrow):
        for j in range(1, ncol):
            if (data[i, j - 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # NW-SE
    for i in range(nrow - 1):
        for j in range(ncol - 1):
            if (data[i + 1, j + 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    # NE-SW
    for i in range(nrow - 1):
        for j in range(1, ncol):
            if (data[i + 1, j - 1] <= (data[i, j] - 2)):
                new_image[i, j] -= 1
    data = new_image
    return new_image.copy()  # output of Speckle


#Segmentation function
def watershed(data):
    data = blurfun(data)
    #data = crimmins(data)
    plt.imsave('enhanced.jpg', data)
    new_image = data.copy()
    ret, thresh1 = cv2.threshold(new_image, 120, 255, cv2.THRESH_BINARY_INV)
    #noise removal
    kernal = np.ones((3,3),np.uint8)
    opening = cv2.morphologyEx(thresh1, cv2.MORPH_OPEN, kernal, iterations = 2)

    #sure background area
    sure_bg = cv2.dilate(opening, kernal, iterations = 350)
    #plt.figure()
    plt.imsave('test.jpg', sure_bg)

    #finding sure foreground
    dist_transform = cv2.distanceTransform(opening, cv2.DIST_L2, 5)
    ret, sure_fg = cv2.threshold(dist_transform, 0.01*dist_transform.max(), 255, 0)
    #plt.figure()

    #finding unkown region
    sure_fg = np.uint8(sure_fg)
    unkown = cv2.subtract(sure_bg, sure_fg)
    #plt.figure()

    #load in your original image
    originalImage = new_image # cv2.imread(imagepath, 0)

    #load in your mask
    mask = cv2.imread('test.jpg', 0)

    #Get rid of quantization artifacts
    mask[mask == 0] = 0
    mask[mask > 216] = 1
    mask[mask < 215] = 0

    #create output image
    outputImg = originalImage * (mask == 1)
    plt.show()
    outputImg[mask == 0] = 220
    ret, thresh2 = cv2.threshold(outputImg,0,255, cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)
    cv2.imwrite('LastVer.jpg',thresh2)
    imaage=cv2.imread('LastVer.jpg')
    ttry=imaage
    height, width, depth = imaage.shape
    for i in range(0,height):
      for j in range(0,width):
          if (imaage[i, j] == 0).all():
              ttry[i,j]=originalImage[i,j]
          else:
              ttry[i,j]=0
          j=j+1
      i=i-1
    return ttry


#Feature extraction function
class GLCM:
    def __init__(self, image, dy, dx):
        self.image = image
        self.dy = dy
        self.dx = dx
        self.glcm = self.GLCMcount()
        self.kontras, self.meanI, self.meanJ, self.energy, self.homogenity = self.contrast()
        self.taoI, self.taoJ = self.tao()
        self.korelasion = self.correlation()
        #self.entropyy=self.Calentropy()

    def GLCMcount(self):
        height, width = self.image.shape[:2]
        glcm = np.zeros((256, 256, 3), np.double)
        x = 0
        for i in range(height):
            for j in range(width):
                if i + self.dy in range(height) and j + self.dx in range(width):
                    glcm[self.image[i, j, 0], self.image[i +
                                                         self.dy, j + self.dx, 0], 0] += 1
                    glcm[self.image[i, j, 1], self.image[i +
                                                         self.dy, j + self.dx, 1], 1] += 1
                    glcm[self.image[i, j, 2], self.image[i +
                                                         self.dy, j + self.dx, 2], 2] += 1
                    # print str(image[i,j,0]) + " " + str(image[i+dy,j+dx,0]) + "
                    # " + str(glcm[image[i,j,0],image[i+dy,j+dx,0],0])
                    x += 1
        glcm = glcm / x
        return glcm
    #def Calentropy(self):
        #grayImg = img_as_ubyte(color.rgb2gray(self.image))
        #entropyy=entropy(grayImg)
        #return entropyy
    def contrast(self):
        contrast = np.zeros(3, np.float)
        meanI = np.zeros(3, np.float)
        meanJ = np.zeros(3, np.float)
        energy = np.zeros(3, np.float)
        homogenity = np.zeros(3, np.float)
        for i in range(256):
            for j in range(256):
                contrast[0] += pow(i - j, 2) * self.glcm[i, j, 0]
                contrast[1] += pow(i - j, 2) * self.glcm[i, j, 1]
                contrast[2] += pow(i - j, 2) * self.glcm[i, j, 2]
                meanI[0] += i * self.glcm[i, j, 0]
                meanI[1] += i * self.glcm[i, j, 1]
                meanI[2] += i * self.glcm[i, j, 2]
                meanJ[0] += j * self.glcm[i, j, 0]
                meanJ[1] += j * self.glcm[i, j, 1]
                meanJ[2] += j * self.glcm[i, j, 2]
                energy[0] += pow(self.glcm[i, j, 0], 2)
                energy[1] += pow(self.glcm[i, j, 1], 2)
                energy[2] += pow(self.glcm[i, j, 2], 2)
                homogenity[0] += self.glcm[i, j, 0] / (1 + abs(i - j))
                homogenity[1] += self.glcm[i, j, 1] / (1 + abs(i - j))
                homogenity[2] += self.glcm[i, j, 2] / (1 + abs(i - j))
        return contrast, meanI, meanJ, energy, homogenity

    def correlation(self):
        correlation = np.zeros(3, np.float)
        for i in range(256):
            for j in range(256):
                correlation[0] += ((i - self.meanI[0]) * (j - self.meanJ[0])
                                   * self.glcm[i, j, 0]) / (self.taoI[0] * self.taoJ[0])
                correlation[1] += ((i - self.meanI[1]) * (j - self.meanJ[1])
                                   * self.glcm[i, j, 1]) / (self.taoI[1] * self.taoJ[1])
                correlation[2] += ((i - self.meanI[2]) * (j - self.meanJ[2])
                                   * self.glcm[i, j, 2]) / (self.taoI[2] * self.taoJ[2])
                # print correlation;
        return correlation

    def tao(self):
        taoI = np.zeros(3, np.float)
        taoJ = np.zeros(3, np.float)
        for i in range(256):
            for j in range(256):
                taoI[0] += pow(i - self.meanI[0], 2) * self.glcm[i, j, 0]
                taoI[1] += pow(i - self.meanI[1], 2) * self.glcm[i, j, 1]
                taoI[2] += pow(i - self.meanI[2], 2) * self.glcm[i, j, 2]
                taoJ[0] += pow(j - self.meanJ[0], 2) * self.glcm[i, j, 0]
                taoJ[1] += pow(j - self.meanJ[1], 2) * self.glcm[i, j, 1]
                taoJ[2] += pow(j - self.meanJ[2], 2) * self.glcm[i, j, 2]
                # print str(taoJ[2])+" = "+ str(pow(j-meanJ[2],2)) + " x " + str(glcm[i,j,2])
        # print "taoi = "
        # print taoI
        # print "taoj = "
        # print taoJ
        for i in range(3):
            taoI[i] = math.sqrt(taoI[i])
            taoJ[i] = math.sqrt(taoJ[i])
        return taoI, taoJ

    def printglcm(self):

        print ("meanI = " + str(rgb2gs(self.meanI)))
        print ("meanJ = " + str(rgb2gs(self.meanJ)))
        print ("taoI = " + str(rgb2gs(self.taoI)))
        print ("taoJ = " + str(rgb2gs(self.taoJ)))
        print ("kontras = " + str(rgb2gs(self.kontras)))
        print ("Energy = " + str(rgb2gs(self.energy)))
        print ("Homogenitas = " + str(rgb2gs(self.homogenity)))
        print ("Correlation = " + str(rgb2gs(self.korelasion)))

    def writeglcm(self):
        with open("test.txt", "a") as myfile:
            myfile.write(str(rgb2gs(self.kontras)) + " " + str(rgb2gs(self.energy)) + " " +
                         str(rgb2gs(self.homogenity)) + " " + str(rgb2gs(self.korelasion)) + "\n")
        with open("type.txt", "a") as myfile:
            myfile.write(str(1) + ",\n")


def rgb2gs(rgb):
    val = 0.114 * (rgb[0]) + 0.587 * (rgb[1]) + 0.299 * (rgb[2])
    return val


def main_test(img_link):
    dfeature = pd.read_csv('ALL11.csv')
    testImage = imutils.url_to_image(img_link)
    plt.imsave('orginal.jpg', testImage)


    # applay algorithms
    segmented = watershed(testImage)  # Segmented image print in app
    plt.imsave('segmented.jpg', segmented)

    glcm = GLCM(segmented, 0, 1)
    imglcm = glcm.glcm.astype(np.uint8)

    testMeanI = glcm.meanI[1]
    testMeanj = glcm.meanJ[1]
    testTaoI = glcm.taoI[1]
    testTaoJ = glcm.taoJ[1]
    testKontars = glcm.kontras[1]
    testEnergy = glcm.energy[1]
    testHomogenitas = glcm.homogenity[1]
    testCorrelation = glcm.korelasion[1]
    testImageFe = [testMeanI, testMeanj, testTaoI, testTaoJ, testKontars, testEnergy, testHomogenitas, testCorrelation]
    distance_list = []
    for i in range(len(dfeature)):  # 322
        z1 = (testImageFe[0] - dfeature['meanI'][i]) ** 2
        z2 = (testImageFe[1] - dfeature['meanJ'][i]) ** 2
        z3 = (testImageFe[2] - dfeature['taoI'][i]) ** 2
        z4 = (testImageFe[3] - dfeature['taoJ'][i]) ** 2
        z5 = (testImageFe[4] - dfeature['kontras'][i]) ** 2
        z6 = (testImageFe[5] - dfeature['Energy'][i]) ** 2
        z7 = (testImageFe[6] - dfeature['Homogenitas'][i]) ** 2
        z8 = (testImageFe[7] - dfeature['Correlation'][i]) ** 2
        z9 = [dfeature['Class'][i]]
        distance = [mt.sqrt(z1 + z2 + z3 + z4 + z5 + z6 + z7 + z8)]
        distance_list.append(distance + z9)

    distance_list.sort()
    return distance_list[0][1]






config = {
    # "apiKey": "AIzaSyD0h93yNNgfyMcsIH9pTChBBiq8nWPi5Ro",
    # "authDomain": "breast-cancer-199344.firebaseapp.com",
    # "databaseURL": "https://breast-cancer-199344.firebaseio.com",
    # "projectId": "breast-cancer-199344",
    # "storageBucket": "breast-cancer-199344.appspot.com",
    # "messagingSenderId": "1021414509817",
    # "appId": "1:1021414509817:web:ea25ed6614b7bb2468a628",
    # "measurementId": "G-FSN4V2BWLZ"



    "apiKey": "AIzaSyBHKIKYQYf2Le2y6jNbEMYp5n53aMEyRA4",
    "authDomain": "my-breast-cancer.firebaseapp.com",
    "databaseURL": "https://my-breast-cancer.firebaseio.com",
    "projectId": "my-breast-cancer",
    "storageBucket": "my-breast-cancer.appspot.com",
    "messagingSenderId": "135114226463",
    "appId": "1:135114226463:web:260d8b64f60e01fada8e2a",
    "measurementId": "G-X9HQ8Q2ZM9"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()


#auth = firebase.auth()

#user_u = auth.sign_in_with_email_and_password('mohannad199344@outlook.com', '12345678')


def url_to_image(url):
    images = io.imread(url, 0)
    return images

def upload_img(img, folder):
    current_time = datetime.datetime.now()
    date = current_time.hour + current_time.minute + current_time.second + current_time.microsecond + current_time.day + current_time.month + current_time.year
    name = str(date) + 'jpg'
    storage = firebase.storage()
    img_url = storage.child(folder).child(name).put(img) #user_u['idToken']
    url = storage.child(folder).child(name).get_url(img_url['downloadTokens'])
    return url


while True:
    #user_u = auth.refresh(user_u['refreshToken'])
    if db.child("Queue").get().each() != None:
        all_samples = db.child("Queue").get()
        for user in all_samples.each():
            key = user.key()
            sample_id = db.child("Queue").child(key).get().val()
            user_id = db.child("Cancer Sample").child(sample_id).child("user id").get().val()
            link = db.child("Cancer Sample").child(sample_id).child("cancer image").get().val()

            image_result = str(main_test(link))

            current_time1 = datetime.datetime.now()#
            time=str(current_time1.hour)+':'+str(current_time1.minute)#

            enhancement = upload_img('enhanced.jpg', "Enhancement")
            segmenation = upload_img('segmented.jpg', "Segmentation")

            db.child("Cancer Sample").child(sample_id).child("enhancement").set(enhancement)
            db.child("Cancer Sample").child(sample_id).child("segmentation").set(segmenation)

            db.child("Cancer Sample").child(sample_id).child("diagnosis").set(image_result)
            db.child("Cancer Sample").child(sample_id).child("upload date").set(time)#
            db.child("Queue").child(key).remove()
            print(
                "\n.\n.\n.\nqueue key: " + key + "\n" + "sample id: " + sample_id + "\n" + "user id: " + user_id + "\n" + "image result: " + image_result + "\n.\n.\n.\n")

            x = 0

    else:
        x = x + 1
        s = 'empty' + str(x)
        print(s)
