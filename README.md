# App List Manager (Android Library)

App List Manager is easy to use Android library, which minimizes developing time when working on applications and activities lists. You no longer have to worry about asynchronous tasks, memory leaks and intent receivers. This library provides a simple way to receive applications and activities lists as they change.

To receive application and activities lists you must implement listeners and invoke methods. In addition, to receive these lists automatically you must also register a receiver (in the manifest and code). All listeners must be registered, and all unfinished tasks must be destroyed. Guide below explains exactly how to do all that and you can also inspect the included sample app that uses most of the features.

## Table of Contents

1. How to use - basic features
    1. Getting apps
    2. Getting activities
    3. Registering listeners
    4. Destroying unfinished tasks
    5. Registering a receiver
2. How to use - advanced features
    1. Sorting
    2. Comparing
    3. Checking application flags
3. More on each method and listener
4. [Other Information](https://github.com/LayoutXML/AppListManager#other-information)
    1. [Donate](https://github.com/LayoutXML/AppListManager#donate)
    2. [Author](https://github.com/LayoutXML/AppListManager#author)
    3. [License](https://github.com/LayoutXML/AppListManager#license)

---

## How to use - basic features

### Getting apps

 | Method | Listener
--- | --- | ---
Get all apps | AppList.getAllApps(...) | appListener(...)
Get some apps (filtered list) | AppList.getSomeApps(...) | appListener(...)
Get all new apps | AppList.getAllNewApps(...) | newAppListener(...)
Get some new apps (filtered list) | AppList.getSomeNewApps(...) | newAppListener(...)
Get all uninstalled apps | AppList.getAllUninstalledApps(...) | uninstalledAppListener(...)

newAppListener and uninstalledAppListener are also invoked automatically in foreground (on all Android versions) and in background (on Android versions 7.1.1 and lower).

### Getting activities

| Method | Listener
--- | --- | ---
Get all<sup>1</sup> activities | AppList.getAllActivities(...) | activitiesListener(...)
Get some activities (filtered list) | AppList.getSomeActivities(...) | activitiesListener(...)
Get all<sup>1</sup> new activities | AppList.getAllNewActivities(...) | newActivitiesListener(...)
Get some new activities (filtered list) | AppList.getSomeNewActivities(...) | newActivitiesListener(...)
Get all<sup>1</sup> uninstalled activities | AppList.getAllUninstalledActivities(...) | uninstalledActivitiesListener(...)

<sup>1</sup> - all activities with the intent.

newActivitiesListener and uninstalledActivitiesListener are also invoked automatically in foreground (on all Android versions) and in background (on Android versions 7.1.1 and lower).

### Registering listeners

You must register all listeners that are implemented in your application by using `AppList.registerListeners(...)` and adding listeners names (or classes names if classes implement listeners) in this order:<br>`appListener, activitiesListener, newAppListener, newActivitiesListener, uninstalledAppListener, uninstalledActivitiesListener, sortListener`.

Registering listeners can be done only once if listeners (or classes) do not change.

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

---

## How to use - advanced features

### Sorting

App List Manager library provides a method and listener to sort your applications and activities lists.

Method `AppList.sort` takes 4 arguments - app list (what to sort), 

### Comparing

Because we can not get app names, icons and other data of already uninstalled apps, `AppData` `.equals()` is overridden to compare by package names. This includes `.contains(AppData)`, `.remove(AppData)`, `.removeAll(List<AppData>)` and others.

### Checking application flags

It is possible to filter app lists received by `appListener` and `newAppListener` by any combination of [flags](https://developer.android.com/reference/android/content/pm/ApplicationInfo#flags) and their opposites (if `FLAG_SYSTEM` filter returns a list of not updated systems apps, the opposite would be all user apps and updated system apps). You can also check individual `AppData`'s flags and check if it contains any combination of them.

##### Setting Flags

Description on how to filter app lists is in the "Invoking Listeners" section.

To set multiple flags you can use any combination of these operators:
* & - AND operator. Adds apps that have all filters.
* | - OR operator. Adds apps that have any of the given filters.
* ^ - XOR (exlcusive or) operator. Adds apps that have one but not the other filter.

##### Checking Flags

You can check if individual app has (or does not have) flags by using `ApList.checkFlags(AppData, Integer Boolean)`, where `AppData` is a single app, `Integer` is flags, `Boolean` is to check whether or not the app has flags (`true`) or quite the opposite (`false`). Alternatively, you can check flags  yourself by accessing app's flags using `.getFlags`.

---

## Other Information

### Donate
You can now donate to me (LayouXML) on **[Google Play](https://play.google.com/store/apps/details?id=com.layoutxml.support)** or **[PayPal](https://www.paypal.me/RJankunas)**.

### Author
More information about me: https://rokasjankunas.com

More information about my projects: https://layoutxml.com

### License
Twelveish is licensed under "MIT" license. Copyright laws apply.

Copyright © 2018 Rokas Jankūnas (LayoutXML)
