# Digital Doctor
This is a project of Group T01/01-3 for COMP 90018, Sem 2, 2021.
Digital Doctor is designed for users to remotely consult a doctor about their symptoms without physically going to the clinic.

**Demonstration On YouTube:** https://youtu.be/GjS66ZTT-VA

## Build and Run
### Compilation and build tools
* Android Studio
* JDK 8+
* Gradle
* Android SDK 24+

### Build and Run Application
Please install the tools required. Download the code and open it with Android Studio. The project need to be sychronized by Gradle. Once sychronized, Gradle will download all the necessary dependencies automatically. After that, you can build and run the app as follow:
1. In the toolbar, select your app from the run configurations drop-down menu.
2. From the target device drop-down menu, select the device that you want to run your app on.
  ![image](https://user-images.githubusercontent.com/58505249/140388523-cfe2834d-df14-421b-82a5-102ed6d7744f.png)
3. Click the button ![image](https://user-images.githubusercontent.com/58505249/140389513-90dbdd7d-9967-47c8-9685-8066a19a82ea.png) to build and run the app. The build process may cost several minutes. After successfully build, Android Studio will install the app on the selected device, and now you can use it on your device.

**Note:** You can run the app either on a physical device or a emulator. If you run on a emulator, please create a vitural device with API level 29 + using AVD manager. Alternatively, if you run on a physical device, please connect the device via USB and ensure that the device can use service provided by **Google Play**. The app will require some permissions such as GPS permission and Camera permission. To use the app, please confirm to grant permissions to the app. 

### Testing Accounts
For the purpose of testing, a user can be registered to use the normal user interface.

To test the doctor side request and response, please use:
* **Email Id:** optoqq@doctor.com
* **Password:** 12345678

This doctor can be searched and added by:
* **Clinic:** Paramount Medical Clinic
* **Department:** Optometrist
* **Name:** Eddie Watson

## Overview of Digital Doctor
### Sensors Used
* **Camera** for testing heart rate
* **GPS** for searching nearby hospitals and clinics
### External API Used
* Google Map API
* Covid-19 Case Tracker API: https://corona.lmao.ninja/v2/
### Screenshots
***
* **Login Page**

<img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/login.jpg" width="240">

***
* **Dashboards of User Client and Doctor Client**  

<img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/userCli.jpg" width="200">  &nbsp;  &nbsp;  &nbsp; <img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/doctorCli.jpg" width="200">

&nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; **Client for User** &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp;  &nbsp;  &nbsp; **Client for Doctors**

***
* **Modules**

<img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/finddoctor.jpg" width="200">  &nbsp; &nbsp; <img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/covidTracker.jpg" width="200">  &nbsp;  &nbsp; <img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/ClinicFnder.jpg" width="200">  &nbsp;  &nbsp; <img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/heartrate.jpg" width="200">

***
* **Chat and Message Page**

<img src="https://github.com/ArthurLhy/DigitalDoctor/blob/main/ScreenShot/message.jpg" width="240"> 

***

## Authors

```
Shiyi Xu
xus3@student.unimelb.edu.au
```

```
Yicheng Zhang
yichezhang3@student.unimelb.edu.au
```

```
Hangyu Liu 
hangyu@student.unimelb.edu.au
```

```
Ruowen Yao 
ruoweny@student.unimelb.edu.au
```

```
Rui Chen
rcchen1@student.unimelb.edu.au
```

```
Nan Bai 
banb@student.unimelb.edu.au
```

