import pyttsx3
engine = pyttsx3.init() # object creation
def speaker(d):
    engine.setProperty('rate', 135)     # setting up new voice rate

    voices = engine.getProperty('voices')       #getting details of current voice
    #engine.setProperty('voice', voices[0].id)  #changing index, changes voices. o for male
    engine.setProperty('voice', voices[3].id)   #changing index, changes voices. 1 for female


    engine.say(d)
    engine.runAndWait()
    engine.stop()

speaker("Rishabh")