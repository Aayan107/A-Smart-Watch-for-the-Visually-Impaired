from gpiozero import DistanceSensor
from gpiozero import Buzzer
from time import sleep
import time
buzzer = Buzzer(5)
sensor = DistanceSensor(echo=17, trigger=4)
while True:
    print('Distance: ', sensor.distance * 100)
    sleep(1)
    if sensor.distance*100 <= 40:
            print("Alert")
            buzzer.on()
            sleep(1)
            buzzer.off()
            sleep(1)
