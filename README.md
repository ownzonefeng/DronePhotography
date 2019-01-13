# Readme

**I. How to set up our project:**
 - Transfer mobile.apk into tablet, and install the application via mobile.apk
 - It's better to install wear.apk into watch directly from Android Studio
 

**II. How to test our project:**
1. Select Model.  
Open our app, you will see the interface of DeviceListActvity(). Open "settings" and find the corresponding Bebop Drone hotspot, click on it and connect with Bebop Drone.

2. Main Interface.   
You can see the name of the Bebop Drone from the list "Please Select from the Following Drones:", click on it and then you will enter BebopActivity() interface.

**Buttons inside the Main Interface:**

3. Take Off.  
Click "Take off" to fly the drone.   

4. Move parallely.   
You can see 8 fly direction buttons on the control interface. On left, those 4 directions control the drone to fly parallelly, "forward", "backward", "parallel left", "parallel right".

5. Yaw.    
On right, those 4 directions control the drone to yaw, including "up", "down", "turn left", "turn right". If you want the drone to turn left/right, click on the button continuously for a while, as it takes time for drone to turn a specific angle.

6. Take photos.    
Photo-taking button is located at the bottom-left corner. Once clicking on it, a hint "Photo is taken" will appear and the photo will be stored in local automatically(you can see from the notification whether the download is complete).

7. Record video.   
Video-recording button is located at the bottom-right corner. Once clicking on it, the button will turn grey, and a green timer will appear on the screen just below "Emergency" button. You need to press on it again to end recording.

8. Zoom in/out.   
Those two button are located at the bottom-right corner, just beside recording. 

9. Gallery  
Press the button on top-right to display all photos nd videos stored in local tablet memory.

10. Emergency    
Be carefull not to touch "Emergency" button in a random situation, it will cause the drone to stop flying and drop to the ground directly(in this way the drone will get damaged).


**III.Firebase Connection**
1. Once you finish taking photos and recording videos on tablet, go back to the interface of DeviceListActvity. 

2. Now since you are still connected with Bebop, you can only check your local Gallery(by pressing the "Gallery" button), the "Firebase" button is disabled.

3.Then switch from Bebop Drone wifi to a normal wifi, and open the app again.
You can see that some icons change here, and synchronization with Firebase starts. After a few seconds, click on the "Firebase" icon at bottom-left corner, you can see a list of photo names, which means those new photos have been uploaded to Firebase.

**IV.Connect with watch:**
1. Open Bluetooth of both tablet and watch, let watch connect to the Bluetooth of tablet. Pay attention here that the watch has to maintain **a close distance** with the tablet, otherwise the Bluetooth connetion may not work.

2. Once Bluetooth connection is built, make sure you get into the **Main Interface** of the tablet first. Then press the second side button of the watch if you want to take a picture, wait for a few seconds, the photo will be then transferred to the watch via Bluetooth. You can see the preview of the photo on watch now.

