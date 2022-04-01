import cv2
from pytesseract import image_to_string
import numpy as np


def snap():
    cam = cv2.VideoCapture(0, cv2.CAP_DSHOW)
    s, img = cam.read()
    cv2.destroyAllWindows()
    cv2.imwrite("filename.jpg",img)


def get_text(path):
    image = cv2.imread(path,0)
    #type(image)
    blur = cv2.bilateralFilter(image,10,75,75)
    thresh = cv2.adaptiveThreshold(blur,255,cv2.ADAPTIVE_THRESH_MEAN_C,cv2.THRESH_BINARY,11,8)
    text = image_to_string(thresh)
    return text


def FRmodel(path):
    
    
    weightsPath = "ML models/simple_frozen_graph.pb" #CHECK THE PATH

    model = cv2.dnn.readNetFromTensorflow(weightsPath)
    image = cv2.imread(path)
    blob = cv2.dnn.blobFromImage(image=image, scalefactor=1/255, size=(224, 224))
    model.setInput(blob)
    result = model.forward()
    
    dataset_labels = ['Ripe Apple', 'Ripe Banana', 'Ripe Mango', 'Ripe Orange', 'Ripe Papaya',
        'Ripe Pomegranate', 'Rotten Apple', 'Rotten Banana', 'Rotten Mango',
        'Rotten Orange', 'Rotten Papaya', 'Rotten Pomegranate', 'Unripe Apple',
        'Unripe Banana', 'Unripe Mango', 'Unripe Orange', 'Unripe Papaya',
        'Unripe Pomegranate']
    
    
    result1 = result[0]
    result1 = result1.reshape(18,1)
    predicted_id = np.argmax(result1)
    predicted_label = dataset_labels[predicted_id]
    return predicted_label


def detec(path):

    classNames = []
    classFile = "ML models/coco.names" #CHECK THE PATH
    with open(classFile,"rt") as f:
        classNames = f.read().rstrip("\n").split("\n")

    configPath = "ML models/ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt"  #CHECK THE PATH
    weightsPath = "ML models/frozen_inference_graph.pb" #CHECK THE PATH

    net = cv2.dnn_DetectionModel(weightsPath,configPath)
    net.setInputSize(320,320)
    net.setInputScale(1.0/ 127.5)
    net.setInputMean((127.5, 127.5, 127.5))
    net.setInputSwapRB(True)


    def getObjects(img, thres, nms, draw=True):
        classIds, confs, bbox = net.detect(img,confThreshold=thres,nmsThreshold=nms)
        objectInfo =[]
        if len(classIds) != 0:
            for classId, confidence,box in zip(classIds.flatten(),confs.flatten(),bbox):
                className = classNames[classId - 1]
                objectInfo.append(className)
                str1 = " "
                str1 = str1.join(objectInfo)
                if (draw):
                    cv2.rectangle(img,box,color=(0,255,0),thickness=2)
                    cv2.putText(img,classNames[classId-1].upper(),(box[0]+10,box[1]+30),
                    cv2.FONT_HERSHEY_COMPLEX,1,(0,255,0),2)
                    cv2.putText(img,str(round(confidence*100,2)),(box[0]+200,box[1]+30),
                    cv2.FONT_HERSHEY_COMPLEX,1,(0,255,0),2)

        return img,str1


    img = cv2.imread(path , cv2.IMREAD_COLOR)
    
    result, objectInfo = getObjects(img,0.60,0.2)
    # cv2.imwrite("result.jpg", result)
    return objectInfo
