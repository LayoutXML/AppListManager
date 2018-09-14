# App List Manager (Android Library)

App List Manager is easy to use Android library, which minimizes developing time when working on application or activity lists. You no longer have to worry about asynchronous tasks, memory leaks and intent receivers. This library provides a simple way to receive application and activity lists as they change.

To receive application and activity lists you must implement listeners and invoke methods. Additionally, to receive these lists automatically you must also register a receiver (in the manifest file and code). All listeners must be registered, and all unfinished tasks must be destroyed. Guide below explains exactly how to do all that. You can also inspect the included sample app that uses most of the features.

## Table of Contents

1. [How to use - basic features](https://github.com/LayoutXML/AppListManager#how-to-use---basic-features)
    1. [Getting apps](https://github.com/LayoutXML/AppListManager#getting-apps)
    2. [Getting activities](https://github.com/LayoutXML/AppListManager#getting-activities)
    3. [Registering listeners](https://github.com/LayoutXML/AppListManager#registering-listeners)
    4. [Destroying unfinished tasks](https://github.com/LayoutXML/AppListManager#destroying-unfinished-tasks)
    5. [Registering a receiver](https://github.com/LayoutXML/AppListManager#registering-a-receiver)
2. [How to use - advanced features](https://github.com/LayoutXML/AppListManager#how-to-use---advanced-features)
    1. [Sorting](https://github.com/LayoutXML/AppListManager#sorting)
    2. [Comparing](https://github.com/LayoutXML/AppListManager#comparing)
    3. [Checking and filtering applications with flags](https://github.com/LayoutXML/AppListManager#checking-and-filtering-applications-with-flags)
    4. [Filtering activities with flags](https://github.com/LayoutXML/AppListManager#filtering-activities-with-flags)
    5. [More on flags](https://github.com/LayoutXML/AppListManager#more-on-flags)
3. [More on each method and listener](https://github.com/LayoutXML/AppListManager#more-on-each-method-and-listener)
    1. [Methods](https://github.com/LayoutXML/AppListManager#methods)
    2. [Listeners](https://github.com/LayoutXML/AppListManager#listeners)
4. [Other Information](https://github.com/LayoutXML/AppListManager#other-information)
    1. [Sample app](https://github.com/LayoutXML/AppListManager#sample-app)
    2. [Donate](https://github.com/LayoutXML/AppListManager#donate)
    3. [Author](https://github.com/LayoutXML/AppListManager#author)
    4. [License](https://github.com/LayoutXML/AppListManager#license)

## How to use - basic features

### Getting apps

-| Method | Listener
--- | --- | ---
Get all apps | AppList.getAllApps(...) | appListener(...)
Get some apps (filtered list) | AppList.getSomeApps(...) | appListener(...)
Get all new apps | AppList.getAllNewApps(...) | newAppListener(...)
Get some new apps (filtered list) | AppList.getSomeNewApps(...) | newAppListener(...)
Get all uninstalled apps | AppList.getAllUninstalledApps(...) | uninstalledAppListener(...)
Get some uninstalled apps | AppList.getSomeUninstalledApps(...) | uninstalledAppListener(...)

newAppListener and uninstalledAppListener are also invoked automatically in the foreground (on all Android versions) and in the background (on Android versions 7.1.1 and lower).

### Getting activities

-| Method | Listener
--- | --- | ---
Get all<sup>1</sup> activities | AppList.getAllActivities(...) | activityListener(...)
Get some activities (filtered list) | AppList.getSomeActivities(...) | activityListener(...)
Get all<sup>1</sup> new activities | AppList.getAllNewActivities(...) | newActivityListener(...)
Get some new activities (filtered list) | AppList.getSomeNewActivities(...) | newActivityListener(...)
Get all<sup>1</sup> uninstalled activities | AppList.getAllUninstalledActivities(...) | uninstalledActivityListener(...)
Get some uninstalled activities | AppList.getSomeUninstalledActivities(...) | uninstalledActivityListener(...)

<sup>1</sup> - all activities with the intent.

newActivityListener and uninstalledActivityListener are also invoked automatically in the foreground (on all Android versions) and in the background (on Android versions 7.1.1 and lower).

### Registering listeners

You must register all listeners that are implemented in your application by using `AppList.registerListeners(...)` and adding listeners names (or classes names if classes implement listeners) in this order:<br>`appListener, activityListener, newAppListener, newActivityListener, uninstalledAppListener, uninstalledActivityListener, sortListener`.

You can register listeners only once if listeners do not change but if you have multiple receivers across different classes feel free to re-register every time you want to change it.

### Destroying unfinished tasks

You must destroy all unfinished tasks when the activity is being closed, changes or is restarted by using `AppList.destroy()` to not create memory leaks.

For example, you can destroy unfinished tasks in activity's onPause method.

### Registering a receiver

If your application supports Android versions 7.1.1 or lower (not necessarily limited to these versions), you must add this to your AndroidManifest.xml file between application tags:
```
<receiver
    android:name="com.layoutxml.applistmanagerlibrary.AppList"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <action android:name="android.intent.action.PACKAGE_ADDED" />
        <action android:name="android.intent.action.PACKAGE_REMOVED" />
        <data android:scheme="package" />
    </intent-filter>
</receiver>
```

If your application supports Android versions 8.0 and higher (not necessarily limited to these versions), you must add this to your application, for example to onCreate method:
```
if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    registerReceiver(new AppList(),AppList.intentFilter);
```

## How to use - advanced features

### Sorting

App List Manager library provides a method and a listener to sort your application and activity lists.

Method `AppList.sort` takes 4 arguments - app list (what to sort), two integer arguments that describe how to sort and a unique identifier:
1. Second argument describes by what - it can be `AppList.BY_APPNAME` or `AppList.BY_PACKAGENAME`.
2. Third argument describes in what order - it can be `AppList.IN_ASCENDING` or `AppList.IN_DESCENDING`.

### Comparing

Because we can not get app names, icons and other data of already uninstalled apps, method `.equals()` is overridden to compare by package names. This includes `.contains()`, `.remove()`, `.removeAll()` and others.

### Checking and filtering applications with flags

It is possible to filter applications lists received by `appListener`, `newAppListener`, `activityListener`, `newActivityListener` with any combination of [these flags](https://developer.android.com/reference/android/content/pm/ApplicationInfo#flags) and their opposites (if `FLAG_SYSTEM` filter returns a list of not updated systems apps, the opposite would be all user apps and updated system apps). You can also check individual application's flags and check if it contains any combination of them.

Additionally, you can check whether an individual application has flags by using `AppList.checkFlags(...)` with 3 arguments - application (or activity) list, flags ([these](https://developer.android.com/reference/android/content/pm/ApplicationInfo#flags)) and whether to match them or not. Alternatively, you can check flags  yourself by accessing app's flags using `.getFlags`. When checking an activity, method checks its application flags and not activity flags.

### Filtering activities with flags

It is also possible to filter activity lists received by `activityListener`, `newActivityListener` with any combination of [these flags](https://developer.android.com/reference/android/content/pm/PackageManager#queryIntentActivities(android.content.Intent,%20int)).

### More on flags

To set multiple flags you can use any combination of these operators:
* & - AND operator. Adds apps that have all filters.
* | - OR operator. Adds apps that have any of the given filters.
* ^ - XOR (exclusive or) operator. Adds apps that have one but not the other filter.

## More on each method and listener

### Methods

Methods for applications:
1. **getAllApps**. Takes 2 arguments: *Context* and *Integer* (unique identifier for your personal use). Used to receive all installed application list (unfiltered and unsorted).
2. **getSomeApps.** Takes 4 arguments: *Context*, *Integer* (application flags), *Boolean* (whether to find applications that match the flags or not), and *Integer* (unique identifier). Used to receive filtered installed application list (unsorted).
3. **getAllNewApps**. Takes 3 arguments: *Context*, *List<AppData>* (current application list), and *Integer* (unique identifier). Used to receive all application list that are not in the given list (unfiltered and unsorted).
4. **getSomeNewApps**. Takes 5 arguments: *Context*, *List<AppData>* (current application list), *Integer* (application flags), *Boolean* (whether to find applications that match the flags or not), and *Integer* (unique identifier). Used to receive filtered application list that are not in the given list (unsorted). Does not remove applications that do not have given flags from the given list.
5. **getAllUninstalledApps**. Takes 3 arguments: *Context*, *List<AppData>* (current application list), and *Integer* (unique identifier). Used to receive all application list that are no longer installed (unfiltered and unsorted).
6. **getSomeUninstalledApps**. Takes 5 arguments: *Context*, *List<AppData>* (current application list), *Integer* (application flags), *Boolean* (whether to find applications that match the flags or not), and *Integer* (unique identifier). Used to receive a list of applications that are not in the filtered currently installed application list.

Methods for activities:
1. **getAllActivities**. Takes 3 arguments: *Context*, *Intent*, and *Integer* (unique identifier). Used to receive all activity list with the intent (unfiltered and unsorted).
2. **getSomeActivities**. Takes 6 arguments: *Context*, *Intent*, *Integer* (activity flags), *Integer* (application flags), *Boolean* (whether to find **applications** that match the flags or not), and *Integer* (unique identifier). Used to receive filtered activity list (unsorted). If "activity flags" not applicable - write 0, if "application flags" not applicable - write null and false for "match".
3. **getAllNewActivities**. Takes 4 arguments: *Context*, *List<AppData>* (current activity list), *Intent*, and *Integer* (unique identifier). Used to receive all activity lists that are not in the given list with the intent (unfiltered and unsorted).
4. **getSomeNewActivities**. Takes 7 arguments: *Context*, *List<AppData>* (current activity list), *Intent*, *Integer* (activity flags), *Integer* (application flags), *Boolean* (whether to find **applications** that match the flags or not), and *Integer* (unique identifier). Used to receive filtered activity list that are not in the given list (unsorted). Does not remove activities that do not have given flags from the given list.
5. **getAllUninstalledActivities**. Takes 4 arguments: *Context*, *List<AppData>* (current application list), *Intent*, and *Integer* (unique identifier). Used to receive all activity list that are no longer installed (unfiltered and unsorted).
6. **getSomeUninstalledActivities**. Takes 7 arguments: *Context*, *List<AppData>* (current application list), *Intent*, *Integer* (activity flags), *Integer* (application flags), *Boolean* (whether to find **applications** that match the flags or not), and *Integer* (unique identifier).

Other methods:
1. **registerListeners**. Takes 7 arguments: *AppListener, ActivityListener, NewAppListener, NewActivityListener, UninstalledAppListener, UninstalledActivityListener, SortListener*. Must be listener names or classes names that implement these listeners. If listener is not used then write null. Explained in more detail in "Registering listeners" section [here](https://github.com/LayoutXML/AppListManager#registering-a-receiver).
2. **checkApplicationFlags**. Takes 3 arguments: *AppData* (single application or activity), *Integer* (**application** flags), and *Boolean* (whether the **application** must match the flags or not). Explained in more detail in "Checking and filtering applications with flags" section [here](https://github.com/LayoutXML/AppListManager#checking-and-filtering-applications-with-flags).
3. **sort**. Takes 4 arguments: *List<AppData>* (application or activity lists that you want to be sorted), *Integer* (explains how to sort), *Integer* (explains how to sort), and *Integer* (unique identifier). Explained in more detail in "Sorting" section [here](https://github.com/LayoutXML/AppListManager#sorting).

Note: New application/activity list methods could be used to get application/activity list with different filters - method compares given (filtered) list with currently installed application/activity list and returns applications/activities that are in a currently installed application/activity list but not in the given (filtered) list.

Note: Uninstalled application/activity list methods could be used to get application/activity list with different filters - method compares given list with currently installed (filtered) application/activity list and returns applications/activities that are in the given list but not in the currently installed (filtered) application/activity list.

### Listeners

Listeners for applications:
1. **appListener**. Receives 4 arguments: *List<AppData>* (your application list), *Integer* (application flags; if getAllApps was called, then it's null), *Boolean* (whether flags match applications; if getAllApps was called then it's false), and *Integer* (unique identifier).
2. **newAppListener**. Receives 5 arguments: *List<AppData>* (your new application list), *Integer* (application flags; if getAllNewApps was called or from a broadcast receiver, then it's null), *Boolean* (whether flags match applications; if getAllNewApps was called or from a broadcast receiver, then it's false), *Boolean* (true when from broadcast receiver, otherwise false), and *Integer* (unique identifier);
3. **uninstalledAppListener**. Receives 5 arguments: *List<AppData>* (your uninstalled application lists), *Boolean* (true when from broadcast receiver, otherwise false), *Integer* (application flags; if getAllUninstalledApps was called or from a broadcast receiver, then it's null), *Boolean* (whether flags match applications; if getAllUninstalledApps was called or from a broadcast receiver, then it's false), and *Integer* (unique identifier; if from broadcast receiver, it's -1); If received from broadcast receiver, only package name is set to the AppData but applications can still be removed with `.removeAll(...)`.

Listeners for activities:
1. **activityListener**. Receives 4 arguments: *List<AppData>* (your activity list), *Intent*, *Integer* (activity flags), *Integer* (application flags), *Boolean* (whether **applications** match the flags or not), and *Integer* (unique identifier).
2. **newActivityListener**. Receives 7 arguments: *List<AppData>* (your new activity list), *Intent*, *Integer* (activity flags; if getAllNewActivities was called or from a broadcast receiver, then it's 0), *Integer* (application flags; if getAllNewActivities was called or from a broadcast receiver, then it's null), *Boolean* (whether flags match applications; if getAllNewActivities was called or from broadcast receiver, then it's false), *Boolean* (true when from broadcast receiver, otherwise false), and *Integer* (unique identifier).
3. **uninstalledActivityListener**. Receives 7 arguments: *List<AppData>* (your uninstalled activity list), *Intent*, *Integer* (activity flags; if getAllUninstalledActivities was called or from a broadcast receiver, then it's 0), *Integer* (application flags; if getAllUninstalledActivities was called or from a broadcast receiver, then it's null), *Boolean* (whether flags match applications; if getAllUninstalledActivities was called or from broadcast receiver, then it's false), *Boolean* (true when from broadcast receiver, otherwise false), and *Integer* (unique identifier; if from broadcast receiver, it's -1). If received from broadcast receiver, received list contains only 1 activity for every uninstalled application (with only package name set) but if the application has 2 activities that are in the list, they can still be removed with `.removeAll(...)`.

Other listeners:
1. **sortListener**. Receives 4 arguments: *List<AppData>* (your sorted activities list), *Integer* (explains how to sort), *Integer* (explains how to sort), *Integer* (unique identifier). Explained in more detail in "Sorting" section [here](https://github.com/LayoutXML/AppListManager#sorting).

## Other Information

### Sample app

Sample app that showcases most of the features can be found in ["app" folder in this repository](https://github.com/LayoutXML/AppListManager/tree/master/app). Application contains two activities:
1. [MainActivity](https://github.com/LayoutXML/AppListManager/blob/master/app/src/main/java/com/layoutxml/applistmanager/MainActivity.java) demonstrates these features:
    1. Receiving all applications (getAllApps).
    2. Receiving new applications (getAllNewApps and broadcast receiver).
    3. Receiving uninstalled applications (getAllUninstalledApps and broadcast receiver).
    4. Receiving some (system) applications (getSomeApps) with flags.
    5. Receiving all activities (with launcher intent) (getAllActivities).
    6. Receiving new activities (with launcher intent) (getAllNewActivities and broadcast receiver).
    7. Receiving uninstalled activities (with launcher intent) (getAllUninstalledActivities and broadcast receiver).
    8. Sorting (sort).
2. [ListActivity](https://github.com/LayoutXML/AppListManager/blob/master/app/src/main/java/com/layoutxml/applistmanager/ListActivity.java) demonstrates these features:
    1. Showing spinning progress bar (loading) when waiting for application/activity list.
    2. Receiving all applications (getAllApps).
    3. Receiving all activities (with launcher intent) (getAllActivities).
    4. Showing application/activity list on screen.
    5. Receiving new applications (getAllNewApps and broadcast receiver).
    6. Receiving new activities (with launcher intent) (getAllNewActivities and broadcast receiver).
    7. Updating the list on screen with new applications/activities.
    8. Receiving uninstalled applications (getAllUninstalledApps and broadcast receiver).
    9. Receiving uninstalled activities (with launcher intent) (getAllUninstalledActivities and broadcast receiver).
    10. Updating the list on screen without uninstalled applications/activities.
    11. Sorting (sort).


### Donate
You can now donate to me (LayouXML) on **[Google Play](https://play.google.com/store/apps/details?id=com.layoutxml.support)** or **[PayPal](https://www.paypal.me/RJankunas)**.

### Author
More information about me: https://rokasjankunas.com

More information about my projects: https://layoutxml.com

### License
AppListManager and a sample app are licensed under "MIT" license. Copyright laws apply.

Copyright © 2018 Rokas Jankūnas (LayoutXML)
