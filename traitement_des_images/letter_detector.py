import numpy as np
import cv2
import matplotlib.pyplot as plt
import time

def letter_exists(dis):
	N = len(dis)
	count = 0 
	for i in range(N):
		if dis[i] > 18000:
			count = count + 1

	if count > N*0.6:
		return True
	else:
		return False

cap = cv2.VideoCapture(r'C:\Users\Samuel\Desktop\PACT材料\IMG_5666.mov')
#ret indicates if cap.read() is successful
#set the first frame as reference where there isn't any letter
ret, frame0 = cap.read()
#set dtype to signed int
frame0.dtype = 'int8'
N = 0 #note the number of the current frame

dis = np.zeros(20 * 240) #distance between the current frame and the first frame
dis.shape = 20, 240 #20 letter boxes, 240 frames(about 10 seconds) to be stored
states = np.zeros(20) #we assume that at first, there isn't any letter

room = [713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 813, 814, 815, 816, 817, 818, 819, 820, 821, 822]

while True:

	ret, frame = cap.read()
	if ret == False:
		break
	frame.dtype = 'int8'
	N = N + 1

	#number of current frame in the distance array
	n = N%240
	#update distance 
	for i in range(10):
		blocki0 = frame0[41:535, (52+i*175):(227+i*175)]
		blocki = frame[41:535, (52+i*175):(227+i*175)]
		dis[i, n] = np.linalg.norm(blocki0 - blocki)

	for i in range(10):
		blocki0 = frame0[520:991, (64+i*172):(236+i*172)]
		blocki = frame[520:991, (64+i*172):(236+i*172)]
		dis[i+10, n] = np.linalg.norm(blocki0 - blocki)



	#check if a letter has arrived, 
	#which means 60 percent of 240 frames has a distance above 18000
	for i in range(20):
		if (letter_exists(dis[i])) and (states[i] == 0):
			states[i] = 1
			print ("Time: " + str(N/23.98) + ", room " + str(room[i]) + " has a letter. ")

		if (not(letter_exists(dis[i]))) and (states[i] == 1):
			states[i] = 0

