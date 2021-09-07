# Android_POC

This is an android based application through which users will be able to retrieve Contact Details, Call History details and transaction related messages. POC will retrive contact and logs information from the device by asking for different permissions.It will retrive the messages which are coming from an **ICICI** bank and have **credited** or **debited** keywords into the subject line. 

## Tech/Framework used
 
- MVC framework has been used to develop the application.
- The application will work as per intended into device having the android version 5.0 and above. 
- Application has been developed into JAVA language. 

## Important functions to be used 
  
  - ```app/src/main/java/com.poc``` is the main directory of the application. 
  - Fragments are placed under ```app/src/main/java/com.poc.fragment```
  
**1. CallsFragment** - It is used to retrive data from ```CallLog.Calls.CONTENT_URI``` through the curser loader.

**2. ContactsFragment** - It is used to retrive data from ```ContactsContract.CommonDataKinds.Phone.CONTENT_URI``` through the curser loader.

**3. MessagesFragment** -  It is used to retrive messaging data from ```Uri.parse("content://sms/")``` through curser loader. Specific messages are retrived by using following keywords:
- ICICIB 
- Credited
- Debited 

**4. Fetch Amount** - To get specific amount form the message body, ```[rR][sS]\.?\s[,\d]+\.?\d{0,2}|[iI][nN][rR]\.?\s*[,\d]+\.?\d{0,2}``` pattern is used. 
      
## Installation

Install the APK file for the application from authorized source and give below mentioned permissions to access all the application related functionality. 
Before running the app, e.g. from Android Studio set properties which can be found 

- **READ_CONTACTS** - This permission is used to fetch contacts.
- **READ_CALL_LOG** - This permission is used to fetch call logs.
- **READ_SMS** - This permission is used to fetch SMS conversations.
- **ACCESS_COARSE_LOCATION** - This permission is used to access network provider's location. 
- **ACCESS_FINE_LOCATION** - This permission is used to access GPS and network provider locations. 
- **INTERNET** - This permission is needed to acccess internet connection. 
- **ACCESS_BACKGROUND_LOCATION** - This permission is used to get device location into higher versions. 
- **FusedLocationProviderClient** - It is used to get current location of a device. 
- **Geocoder** - It helps application to fetch address with the help of Latitude and Longitude. 

## Screenshots
