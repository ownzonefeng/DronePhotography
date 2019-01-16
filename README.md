# Project for Drone Photography

**I. How to set up our project:**
 - Transfer mobile.apk into the tablet, and install the application via mobile.apk
 - It's better to install wear.apk into watch directly from Android Studio
 

**II. How to test our project:**
1. Select Model.  
Open our app; you can see the interface of DeviceListActvity(). Open "settings" and find the corresponding Bebop Drone hotspot, click on it and connect with Bebop Drone.

2. Main Interface.   
You can see the name of the Bebop Drone from the list "Please Select from the Following Drones:," click on it, and then you enter BebopActivity() interface.

**Buttons inside the Main Interface:**

3. Take Off.  
Click "Take off" to fly the drone.   

4. Move parallel.   
You can see 8 fly direction buttons on the control interface. On left, those 4 directions control the drone to fly parallelly, "forward", "backward", "parallel left", "parallel right".

5. Yaw.    
On right, those 4 directions control the drone to yaw, including "up", "down", "turn left", "turn right". If you want the drone to turn left/right, click on the button continuously for a while, as it takes time for the drone to turn a specific angle.

6. Take photos.    
The photo-taking button is located at the bottom-left corner. Once clicking on it, a hint "Photo is taken" appears and the photo will be stored in local automatically(you can see from the notification whether the download is complete).

7. Record video.   
The video-recording button is located in the bottom-right corner. Once clicking on it, the button will turn grey, and a green timer will appear on the screen just below the "Emergency" button. You need to press on it again to end recording.

8. Zoom in/out.   
Those two buttons are located at the bottom-right corner, just beside recording. 

9. Gallery  
Press the button on top-right to display all photos and videos stored in local tablet memory.

10. Emergency    
Be careful not to touch the "Emergency" button in a random situation; it will cause the drone to stop flying and drop to the ground directly(in this way the drone will get damaged).


**III.Firebase Connection**
1. Once you finish taking photos and recording videos on the tablet, go back to the interface of **DeviceListActvity**. 

2. Now since you are still connected with Bebop, you can only check your local Gallery(by pressing the "Gallery" button), the "Firebase" button is disabled.

3. Then switch from Bebop Drone wifi to normal wifi, and open the app again. You can see that some icons change here, and synchronization with Firebase starts. After a few seconds, click on the "Firebase" icon at the bottom-left corner, you can see a list of photo names, which means those new photos have been uploaded to Firebase.
   
4. If you click the photo name, you can see the photo on a tablet.


**IV. Connect with the watch:**
1. Open Bluetooth of both tablet and watch, let watch connect to the Bluetooth of the tablet. Pay attention here that the watch has to maintain **a close distance** with the tablet. Otherwise, the Bluetooth connection may not work.

2. Once Bluetooth connection is built, make sure you get into the **Main Interface** of the tablet first. Then press the second side button of the watch if you want to take a picture, wait for a few seconds, the photo will be then transferred to the watch via Bluetooth. You can see the preview of the photo on watch now.


