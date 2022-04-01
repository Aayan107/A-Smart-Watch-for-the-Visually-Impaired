import speech_recognition as sr 
from bs4 import BeautifulSoup
from gtts import gTTS 
import datetime 
import wikipedia
import requests
import pyjokes
import json 
import time
#import subprocess
import os 
import cv2
import pytesseract
from urllib.request import urlopen
import pyttsx3
import socket            
import os
from multiprocessing import Process
from gpiozero import DistanceSensor
from gpiozero import Buzzer
from time import sleep
import RPi.GPIO as GPIO
from mlfunctions import *     
GPIO.setmode(GPIO.BCM)          
GPIO.setup(24, GPIO.OUT) 



engine = pyttsx3.init()

def talk():
    input = sr.Recognizer()
    with sr.Microphone() as source:
        audio = input.listen(source)
        data = ""
        try:
            data = input.recognize_google(audio)
            print("Your question is, " + data)

        except sr.UnknownValueError:
            print("Sorry I did not hear your question, Please repeat again.")
        return data
def talk2():
    input = sr.Recognizer()
    with sr.Microphone() as source:
        audio = input.listen(source)
        data = ""
        try:
            data = input.recognize_google(audio)
            print("Your Name is, " + data)

        except sr.UnknownValueError:
            print("Sorry I did not hear your Name, Please repeat again.")
        return data

def respond(d):
    print(d)
    engine.setProperty('rate', 170)     # setting up new voice rate

    voices = engine.getProperty('voices')       #getting details of current voice
    engine.setProperty('voice', voices[0].id)  #changing index, changes voices. o for male
    #engine.setProperty('voice', voices[7].id)   #changing index, changes voices. 1 for female


    engine.say(d)
    engine.runAndWait()
    engine.stop()


def  fms_socket():          # FMS - Find My Shravan Process

    s = socket.socket()        
    
    port = 6667               
    flag = 1
    s.bind(('', port))
    print ("FMS functionality is online\n")  


    while True:

        s.listen(3)
        print ("FMS Initiated")
        buzzer = Buzzer(5)

        while True:    
        
            c, addr = s.accept()    

            m = c.recv(1024)
            m = m.decode("utf-8")

            if (m=="fms\n"):
                
                # FMS buzzer functionality here

                buzzer.on()
                sleep(10)
                buzzer.off()

                continue

            c.close()
            s.close()
    pass

def ultrasonic():           # Ultrasonic Process

    buzzer = Buzzer(5)
    sensor = DistanceSensor(echo=17, trigger=4)
    while True:
        #print('Distance: ', sensor.distance * 100)
        sleep(1)
        if sensor.distance*100 <= 40:
                print("Alert! Object within distance: ", sensor.distance*100) 
                GPIO.output(24, 1)          
                sleep(1)                 
                GPIO.output(24, 0)       
                sleep(1)
                
                #buzzer output
                buzzer.on()
                sleep(1)
                buzzer.off()
                sleep(1)

    pass

def ocr_method():

    #snap()
    texts = get_text("Sample Images/newspaper.jpeg")
    respond(texts)

    pass

"""def takecommand():
    r=sr.Recognizer()
    with sr.Microphone() as source:
        print("listening...")
        r.pause_threshold =1
        audio = r.listen(source, timeout=1,phrase_time_limit=5)
    try:
        print("Recognizing...")
        query = r.recognize_google(audio, language='en-in')
        print(query)
    except Exception as e:
        respond("Say that again please...")
        return "none"
    return query"""


def obj_detection():

    #snap()
    respons = detec('Sample Images/dog.jpg')
    respond(respons)
    
def fr_method():

    #snap()
    respons = FRmodel('Sample Images/Rotten Papaya.jpg')
    respond(respons)


def  run():  #main_runner_method
    
    #Process Declaration
    fms_socket_process = Process(target=fms_socket)
    ultrasonic_process = Process(target=ultrasonic)
    #ocr_process = Process(target=ocr_method)
    #fr_process = Process(target=fr_method)
    #obj_detection_process = Process(target=obj_detection)

    
    #Process Initializaiton
    fms_socket_process.start()                   #Invoking FindMyShravan Process
    ultrasonic_process.start()                  #Invoking Ultrasonic Process


    while(1):

        s = socket.socket()
        port = 6969
        flag = 1
        s.bind(('', port))
        s.listen(3)
        
        phone_connection=False
        # Connection flag
        # True - Phone is connected
        # False - Phone is not connected
        
        respond("Hi, I am Shravan your personal voice assistant")

        respond("Would you like to connect your mobile phone with Shravan?")
        text = talk().lower()
        
        while True:
            
        
            if "yes" in str(text) or "yeah" in str(text) or "sure" in str(text)or "okay" in str(text) or "ok" in str(text):
                respond('Waiting for your mobile device to connect')
                c, addr = s.accept()
                respond(f'Connection established successfully. Connected Device IP:{addr}')
                phone_connection=True
                
                respond("What should i call you?")
                uname = talk2()

                break
            
            text = talk().lower()

                        
        while(1):

                respond(f"How can I help you {uname}")
                text = talk().lower()

                if text == 0:
                    continue

                if "stop" in str(text) or "exit" in str(text) or "bye" in str(text):
                    respond(f"Ok bye and take care {uname}")
                    s.close()
                    break

                if 'wikipedia' in text:
                    respond('Searching Wikipedia')
                    text = text.replace("wikipedia", "")
                    results = wikipedia.summary(text, sentences=3)
                    respond("According to Wikipedia")
                    print(results)
                    respond(results)

                elif 'time' in text:
                    strTime = datetime.datetime.now().strftime("%H:%M:%S")
                    respond(f"the time is {strTime}")

                elif "weather" in text:
                    # base URL
                    BASE_URL = "https://api.openweathermap.org/data/2.5/weather?"
                    CITY = "Mumbai"
                    API_KEY = "2d676d7a080edcae408b12e7a75a780f&units=metric"
                    # upadting the URL
                    URL = BASE_URL + "q=" + CITY + "&appid=" + API_KEY
                    # HTTP request
                    response = requests.get(URL)
                    # checking the status code of the request
                    if response.status_code == 200:
                    # getting data in the json format
                        data = response.json()
                        # getting the main dict block
                        main = data['main']
                        # getting temperature
                        temperature = main['temp']
                        # getting the humidity
                        humidity = main['humidity']
                        # getting the pressure
                        pressure = main['pressure']
                        # weather report
                        report = data['weather']
                        respond(f"{CITY:-^30}")
                        respond(f"Temperature: {temperature}°C")
                        respond(f"Humidity: {humidity}%")
                        respond(f"Pressure: {pressure}hPa")
                        respond(f"Weather Report: {report[0]['description']}")
                
                elif "what is my name" in text:
                    respond(f"Your name is {uname}")

                elif 'joke' in text:
                    respond(pyjokes.get_joke())

                elif 'news' in text:  
                    url = 'https://newsapi.org/v2/everything?'
                    parameters = {
                            'q': 'technology', # query phrase
                            'pageSize': 5,  # maximum is 100
                            'apiKey': 'c5ca7cb2dabc4f2695c217fe4c1d6594' # your own API key
                        }  
                    response = requests.get(url, params=parameters)
                    response_json = response.json()
                    for i in response_json['articles']:
                        respond(i['title'])       

                elif 'ocr' or 'written' or 'read' in text:
                    #ocr_process.start()
                    #ocr_process.join()
                    #ocr_process.kill()
                    #snap()
                    respond ("Initiating OCR")
                    texts = get_text("Sample Images/newspaper.jpeg")
                    respond(texts)

                elif 'detect' or 'object' in text:
                    #obj_detection_process.start()
                    #obj_detection_process.join()
                    #obj_detection_process.kill()
                    #snap()
                    respond ("Initiating Object Detection Module")
                    respons = detec('Sample Images/dog.jpg')
                    respond(respons)

                elif 'fruit' in text:
                    #fr_process.start()
                    #fr_process.join()
                    #fr_process.kill()
                    #snap()
                    respond ("Initiating Fruit Health Check")
                    respons = FRmodel('Sample Images/Rotten Papaya.jpg')
                    respond(respons)
                    
                    
                elif 'score' in text:
                    url='https://www.cricbuzz.com/'
                    page = requests.get(url)
                    soup = BeautifulSoup(page.text,'html.parser')
                    team_1 = soup.find_all(class_ = "cb-ovr-flo cb-hmscg-tm-nm")[0].get_text()
                    team_2 = soup.find_all(class_ = "cb-ovr-flo cb-hmscg-tm-nm")[1].get_text()
                    team_1_score = soup.find_all(class_ = "cb-ovr-flo")[8].get_text()
                    team_2_score = soup.find_all(class_ = "cb-ovr-flo")[10].get_text()
                    respond(f'{team_1} Score is {team_1_score}')
                    respond(f'{team_2} Score is {team_2_score}')

                elif "sos" in text or "emergency" in text:
                    if phone_connection == True:
                        c.send('{"action": "sos","options": {"op1": "everyone","op2":"dummy"}}\n'.encode())
                        respond(f"Your location is shared with your Emergency contacts, hang on tight, {uname}")
                    else:
                        respond("Sorry, SOS is unavailable at the moment. Reason: Your Phone is not connected with Shravan.")
                
                else:
                    respond("Application not available")

        fms_socket_process.kill()
        ultrasonic_process.kill()
        exit()
     
if __name__ == '__main__':

    _ = run()
