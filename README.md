# App List Manager (Android Library)

App List Manager is easy to use Android library, which minimizes developing time when working on application lists. You no longer have to worry about asynchronous tasks, memory leaks and intent receivers. This library provides a simple way to receive application lists as they change.

## Table of Contents

1. Quick Overview
    1. Listeners
    2. "AppData"
    3. Other Functionality
2. Usage
    1. Adding Dependency
    2. Registering Receiver
    3. Starting Functionality
    4. Stopping Functionality
    5. Adding Listeners
        1. "allListener"
        2. "newListener"
        3. "uninstalledListener"
    6. Invoking Listeners
        1. "allListener"
        2. "newListener"
        3. "uninstalledListener"
    7. Accessing "AppData" Contents
    8. Other Functionality
        1. Sorting
        2. Comparing
        3. Flags
            1. Setting Flags
            1. Checking Flags

---

## Quick Overview

### Listeners

Library has 3 listeners:

Function Name | How invoked | Parameters
--- | --- | --- | ---
`allListener` | 1. `AppList.getAll(Context)`<br>2. `AppList.getAll(Context, Integer, Boolean)` | 1. `List<AppData>`<br>2. `Integer filterFlags`
`newListener` | 1. `AppList.getNew(Context, List<AppData>)`<br>2. `AppList.getNew(Context, List<AppData>, Integer, Boolean)`<br>3. Automatically when new app installed* | 1. `List<AppData>`<br>2. `Integer filterFlags`<br>3. `Boolean fromReceiver`
`uninstalledListener` | 1. `AppList.getUninstalled(Context, List<AppData>)`<br>2. Automatically when any app uninstalled* | 1. `List<AppData>`<br>2. `Boolean fromReceiver`

\* - On Android versions >=8.0 only when application opened. This means that you should periodically check for new apps or in onResume. Works in background on lower Android versions.

### "AppData"

All receivers receive a list of `AppData` objects. `AppData` contains this info about single application:

Information | Data Type | How to get | How to set
--- | --- | --- | ---
App Name | `String` | `.getAppName()` | `.setAppName(String)`
Package Name | `String` | `.getAppPackageName()` | `.setAppPackageName(String)`
Icon | `Drawable` | `.getAppIcon()` | `.setAppIcon(Drawable)`
Flags | `Integer` | `.getFlags()` | `.setFlags(Integer)`

### Other Functionality

Library also provides functions to quickly sort, compare and check application flags. More detailed information and examples can be found below.

---

## Usage

It is important that you review the overview section above before using this library to get acquainted with the basic functionality. Do not skim through the usage below because a tiny mistake might reduce functionality, make your application crash or create a memory leak.

### Adding Dependency

Add the dependency to your app's `build.gradle`:

```
    TODO: Adding Dependency
```

### Registering Receiver

1. If your app supports Android versions 7.1.1 or lower, you will be using `newListener` or `uninstalledListener`, and you want know what apps have been uninstalled immediately, you will have to register receiver in your app's manifest file `AndroidManifest.xml` by adding this in your application tag:
```
<receiver
    android:name="com.layoutxml.applistmanagerlibrary.AppList"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <action android:name="android.intent.action.PACKAGE_ADDED"  />
        <action android:name="android.intent.action.PACKAGE_REMOVED" />
        <data android:scheme="package" />
    </intent-filter>
</receiver>
```

2. If your apps supports Android versions 8.0 and greater you will also have to register receiver in your code (if your app only supports Android versions 8.0 and greater you will only have to register it in your code). Add this line of code to your `onCreate` method:
```
registerReceiver(new AppList(),AppList.intentFilter);
```
**Note: this line of code has to be under `Applist.start` which you will add in the next section called "Starting Functionality".**

### Starting Functionality

Decide whether your class will implement listeners or will create them in `onCreate` method.

1. If you have decided to implement listeners to your class, add this line of code to your `onCreate` method:
```
AppList.start(<yourClassName>.this,<yourClassName>.this,<yourClassName>.this);
```
You have to replace <yourClassName> to your class name that will implement listeners. If your class will implement only some of the listeners, replace `<yourClassName>.this` with `null`.

2. If you have decided to create listeners in your `onCreate` method, add this line of code to your `onCreate` method:
```
AppList.start(<allListenerName>,<newListenerName>,<uninstallListenerName>);
```
You have to replace listener names with your actual listener names that you will use. If you will use only some of the listeners, replace listener name with `null`. **Note: this line of code has to be under your listeners in `onCreate` method.**

### Stopping Functionality

In your `onPause` method add this line of code to not create any memory leaks:
```
AppList.stop();
```

### Adding Listeners

You can choose to use only some of the listeners. You must use `allListener` or create `List<AppData>` with your application list manually so that other listeners could work as well. If you want to receive a list of new installed applications (invoked either manually or automatically), you have to use `newListener`. If you want to receive a list of uninstalled applications (invoked either manually or automatically), you have to use `uninstalledListener`.

#### "allListener"

`allListener` receives 2 parameters - `List<AppData> appDataList`, which is a new list of all installed apps, and `Integer filterFlags`, which informs what filters had been used when generating a list. This is useful when you store multiple lists of different apps. When no filter was used, `filterFlags` is `null`. More about filters and flags in "Flags" section.

1. If you have decided to implement listeners to your class, override `allListener` method like this:
```
Override
public void allListener(List<AppData> appDataList, Integer filterFlags) {
    //Your code replacing main List<AppData> with a new one. Notify adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `allListener` like this:
```
AllListener allListener = new AllListener() {
    @Override
    public void allListener(List<AppData> appDataList, Integer filterFlags) {
          //Your code replacing main List<AppData> with a new one. Notify adapters about dataset changing etc...      
    }
};
```

#### "newListener"

`newListener` receives 3 parameters - `List<AppData> appDataList`, which is a list of new apps, `Integer filterFlags`, which informs what filters had been used when generating a list, and `Boolean fromReceiver`, which is `true` when a `BroadcastReceiver` invoked this listener - this means a list contains only one application and it did not check for filters (you may want to check application filters as described in "Checking Flags" section or manually).

1. If you have decided to implement listeners to your class, override `newListener` method like this:
```
@Override
public void newListener(List<AppData> appDataList, Integer filterFlags, Boolean fromReceiver) {
    //Your code adding new list to the main List<AppData>. Notify adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `newListener` like this:
```
AllListener allListener = new AllListener() {
    @Override
    public void allListener(List<AppData> appDataList, Integer filterFlags) {
        //Your code adding new list to the main List<AppData>. Notify adapters about dataset changing etc...  
    }
};
```

#### "uninstalledListener"

`uninstalledListener` receives 2 parameters - `List<AppData> appDataList`, which is a list of new apps, and `Boolean fromReceiver`, which is `true` when a `BroadcastReceiver` invoked this listener - this means a list contains only one application and only package name is correct (other data is `null`).

1. If you have decided to implement listeners to your class, override `uninstalledListener` method like this:
```
@Override
public void uninstalledListener(List<AppData> appDataList, Boolean fromReceiver) {
    //Your code removing apps from the main List<AppData>. Notify adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `uninstalledListener` like this:
```
UninstalledListener uninstalledListener = new UninstalledListener() {
    @Override
    public void uninstalledListener(List<AppData> appDataList, Boolean fromReceiver) {
        //Your code removing apps from the main List<AppData>. Notify adapters about dataset changing etc...
    }
};
```

### Invoking Listeners

#### "allListener"

There are 2 ways to invoke `allListener`:
1. `AppList.getAll(Context);`<br>`Context` is your context.<br>For example: `AppList.getAll(getApplicationContext());`.
2. `AppList.getAll(Context, Integer, Boolean);`<br>`Context` is your context, `Integer` is your flag (flags), and `Boolean` is whether to return applications that match the given flags or those that do not match.<br>For example, `AppList.getAll(getApplicationContext(), ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP,true);` will return (to your `allListener`) all system apps. Changing `true` to `false` will return only user apps. To learn more about flags, go to "Flags" section.

It is recommended to invoke `allListener` when application is opened (`onCreate`) and when user refreshes app list (if possible).

#### "newListener"

There are 3 ways to invoke `newListener`:
1. `AppList.getNew(Context, List<AppData>);`<br>`Context` is your context, and `List<AppData>` is a list with your current applications (application list will be compared to this list).<br>For example: `AppList.getNew(getApplicationContext(), AllAppsList);`.
2. `AppList.getNew(Context, List<AppData>, Integer, Boolean);`<br>`Context` is your context, `Integer` is your flag (flags), and `Boolean` is whether to return applications that match the given flags or those that do not match.<br>For example, `AppList.getNew(getApplicationContext(), AllAppsList, ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP,true);` will return (to your `newListener`) all new system apps. Changing `true` to `false` will return only user apps. To learn more about flags, go to "Flags" section.
3. It is invoked automatically when a new app is installed.<br>Limitations: on Android versions >=8.0 works only when application is opened, on lower Android versions it works in background .

It is recommended to invoke `newListener` periodically and when the app is reopened (`onResume`).

#### "uninstalledListener"

There are 2 ways to invoke 'uninstalledListener':
1. `AppList.getUninstalled(Context, List<AppData>);`<br>`Context` is your context, and `List<AppData>` is a list with your current applications (application list will be compared to this list).<br>For example: `AppList.getUninstalled(getApplicationContext(), AllAppsList);`.
2. It is invoked automatically when any app is uninstalled.<br>Limitations: on Android versions >=8.0 works only when application is opened, on lower Android versions it works in background .

It is recommended to invoke `uninstalledListener` periodically and when the app is reopened (`onResume`).

### Accessing "AppData" Contents

`AppData` is an object with 4 values that can be read using getters and locally changed (in a list) using setters.

Information | Data Type | How to get | How to set
--- | --- | --- | ---
App Name | `String` | `.getAppName()` | `.setAppName(String)`
Package Name | `String` | `.getAppPackageName()` | `.setAppPackageName(String)`
Icon | `Drawable` | `.getAppIcon()` | `.setAppIcon(Drawable)`
Flags | `Integer` | `.getFlags()` | `.setFlags(Integer)`

**Note: App name, icon and flags can be `null`**. This is the case when `AppData`s are received in `uninstalledListener`.

### Other Functionality

#### Sorting

Any given `AppData` list is unsorted, as given by the `PackageManager`.

You can sort any `AppData` list using `AppList.sort(List<AppData>, Integer, Integer);` First `Integer` is sort type, second `Integer` is order. It is `List<AppData>` type so it will return a sorted list from where it was called. You can also choose to sort list manually (by creating your own function).

Sort types:
1. `AppList.BY_APPNAME` (equals to 0). Sorts `AppData` list by app names.
2. `AppList.BY_PACKAGENAME` (equals to 1). Sorts `AppData` list by package names.

Order:
1. `AppList.IN_ASCENDING` (equals to 0). Sorts `AppData` list in ascending order (A to Z).
2. `AppList.IN_DESCENDING` (equals to 1). Sorts `AppData` list in descending order (Z to A).

#### Comparing

Because we can not get app names, icons and other data of already uninstalled apps, `AppData` `.equals()` is overridden to compare by package names. This includes `.contains(AppData)`, `.remove(AppData)`, `.removeAll(List<AppData>)` and others.

#### Flags

It is possible to filter app lists received by `allListener` and `newListener` by any combination of [flags](https://developer.android.com/reference/android/content/pm/ApplicationInfo#flags) and their opposites (if `FLAG_SYSTEM` filter returns a list of not updated systems apps, the opposite would be all user apps and updated system apps). You can also check individual `AppData`'s flags and check if it contains any combination of them.

##### Setting Flags

Description on how to filter app lists is in the "Invoking Listeners" section.

To set multiple flags you have to use these operators:
* & - AND operator. Adds apps that have all filters.
* | - OR operator. Adds apps that have any of the given filters.
* ^ - XOR (exlusive or) operator. Adds apps that have one but not the other filter.

##### Checking Flags

You can check if individual app has (or does not have) flags by using `ApList.checkFlags(AppData, Integer Boolean)`, where `AppData` is a single app, `Integer` is flags, `Boolean` is to check whether or not the app has flags (`true`) or quite the opposite (`false`). Alternatively, you can check flags  yourself by accessing app's flags using `.getFlags`.
