# SETU HDip Computer Science  Mobile Development Assignment 02

![version](https://img.shields.io/badge/version-1.01.23356p-blue.svg)

![logo](https://wit-hdip-comp-sci-2022-mobile-app-dev.netlify.app/topic---orientation/topic.png)

---

## Data View Logger Readme File

---

## Originator

- David Roche

## Student ID  

- 93521243

---

## A Plant Data View Logger for android UI enhancements and Firebase integration

---

## Description

Based around a large industrial site or number of remote sites. On these sites, key pieces of equipment have metrics recorded,  hours running, temperature tank level etc.
On a walk around an operator logged into the app can scan a QR code attached to that piece of equipment.  
The key metrics from the instruments associated with that piece of equipment would then be shown on the app.  

---

## UML and Class Diagrams

> The Following diagram shows the data model for Site-Data as originally designed.

![UML Site Data Model][image1]

> Test Data in Firebase.

![Firebase data ][image2]

> Test Data in Firebase.

![Kotlin Site Data Model][image3]

---

## UX /DX Approach Adopted

The UX uses the the Model Vew View Model approach using fragments from a root home page. The Nav Drawer allows for easy access to settings with a Toolbar allowing the user filter selected sites.
The settings page is mainly for demonstration "although the Firebase/Jason Switch does work", is written using jetpack compose as an embedded element in the settings resource layout.

The DX is based around JASON using both a flat file JSON for local testing and a Firebase version for remote testing.
I had planned on using an API driven backend database but I ran out of time for that.

---

## Git Approach

The git approach was to take a branch from the assignment 1 project and work directly on that, refactoring and remaking along the way to integrate the existing into a new more modern layout

- Assignment II Github [Link](https://github.com/RocheDJ/DataViewLogger/tree/Assignment-II)

---

## Personal Statement

A project of highs and lows in every way.

-  I did enjoy getting the Firebase up and running and making changes on the Firebase server and seeing them appear on the phone was great.

-  The Live data takes a lot of tracking and planning ang maybe refracturing the assignment 1 app not the best plan.

-  Jetpack compose was great to mess round with and the logical routing that that can be use appears to be clearer than the navigation I used here.

-  I would prefer Kotlin over Java for the programming, the Android studio IDE is good but there are way too many files to take care of and not of and plugins that to be enabled.

-  All that said I wouldn't mind doing more if and when time allows.


---

## References

As well as the developer docs and Class Notes some of the other sites referenced during this assignment were,

- IOT Icons downloaded from [Flat-icon](https://www.flaticon.com/free-icons/iot)

- Android Menu Icons From [Google](https://fonts.google.com/icons)

- Scanning a QR code in Kotlin [Medium](https://harshitabambure.medium.com/barcode-scanner-and-qr-code-scanner-android-kotlin-b911b1299f65)

- Adding A Splash screen in android [Medium](https://medium.com/geekculture/implementing-the-perfect-splash-screen-in-android-295de045a8dc)

- Adding Filter to site List [geeks for geeks](https://www.geeksforgeeks.org/android-searchview-with-recyclerview-using-kotlin/)

- Using a launcher theme to add
a Splash screen in android Kotlin  [Medium](https://proandroiddev.com/splash-screen-in-android-3bd9552b92a5)

- Jetpack Navigation hints [Code Magic](https://blog.codemagic.io/android-navigation-introduction/)

- Using Compose inside fragments [Stack overflow Post](https://stackoverflow.com/questions/59368360/how-to-use-compose-inside-fragment)


[image1]: ./umlSiteDataModel.png
[image2]: ./dataFirebase.png
[image3]: ./siteDataClass.png
[image4]: ./nav_main.png
