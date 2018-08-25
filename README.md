# App List Manager (Android Library)

App List Manager is easy to use Android library, which minimizes developing time when working on application lists. You no longer have to worry about asynchronous tasks, memory leaks and intent receivers. This library provides a simple way to receive application lists as they change.

## Table of Contents

1. [Quick Overview](https://github.com/LayoutXML/AppListManager#quick-overview)
    1. [Listeners](https://github.com/LayoutXML/AppListManager#1-listeners)
    2. ["AppData"](https://github.com/LayoutXML/AppListManager#2-appdata)
    3. [Other Functionality](https://github.com/LayoutXML/AppListManager#3-other-functionality)
2. [Usage](https://github.com/LayoutXML/AppListManager#usage)
    1. [Adding Dependency](https://github.com/LayoutXML/AppListManager#1-adding-dependency)
    2. [Registering Receiver](https://github.com/LayoutXML/AppListManager#2-registering-receiver)
    3. [Starting Functionality](https://github.com/LayoutXML/AppListManager#3-starting-functionality)
    4. [Stopping Functionality](https://github.com/LayoutXML/AppListManager#4-stopping-functionality)
    5. [Adding Listeners](https://github.com/LayoutXML/AppListManager#5-adding-listeners)
        1. ["allListener"](https://github.com/LayoutXML/AppListManager#alllistener)
        2. ["newListener"](https://github.com/LayoutXML/AppListManager#newlistener)
        3. ["uninstalledListener"](https://github.com/LayoutXML/AppListManager#uninstalledlistener)
        4. ["sortListener"](https://github.com/LayoutXML/AppListManager#sortlistener)
    6. [Invoking Listeners](https://github.com/LayoutXML/AppListManager#6-invoking-listeners)
        1. ["allListener"](https://github.com/LayoutXML/AppListManager#alllistener-1)
        2. ["newListener"](https://github.com/LayoutXML/AppListManager#newlistener-1)
        3. ["uninstalledListener"](https://github.com/LayoutXML/AppListManager#uninstalledlistener-1)
        4. ["sortListener"](https://github.com/LayoutXML/AppListManager#sortlistener-1)
    7. [Accessing "AppData" Contents](https://github.com/LayoutXML/AppListManager#7-accessing-appdata-contents)
    8. [Other Functionality](https://github.com/LayoutXML/AppListManager#8-other-functionality)
        1. [Comparing](https://github.com/LayoutXML/AppListManager#comparing)
        2. [Flags](https://github.com/LayoutXML/AppListManager#flags)
            1. [Setting Flags](https://github.com/LayoutXML/AppListManager#setting-flags)
            1. [Checking Flags](https://github.com/LayoutXML/AppListManager#checking-flags)
3. [Other](https://github.com/LayoutXML/AppListManager#other)
    1. [Donate](https://github.com/LayoutXML/AppListManager#donate)
    2. [Author](https://github.com/LayoutXML/AppListManager#author)
    3. [License](https://github.com/LayoutXML/AppListManager#license)

---

## Quick Overview

### 1. Listeners

Library has 4 listeners:

Function Name | How invoked | Sent Parameters | Received Parameters
--- | --- | --- | ---
`allListener` | \* `AppList.getAll(Context)`<br>\* `AppList.getAll(Context, Integer, Boolean)` | \* `Context context`<br>(\* `Integer flags`<br>\* `Boolean match`) | \* `List<AppData>`<br>\* `Integer filterFlags`<br>\* `Boolean match`
`newListener` | \* `AppList.getNew(Context, List<AppData>)`<br>\* `AppList.getNew(Context, List<AppData>, Integer, Boolean)`<br>\* Automatically when new app installed<sup>1</sup> | \* `Context context`<br>\* `List<AppData> appDataList`<br>(\* `Integer flags`<br>\* `Boolean match`) | \* `List<AppData>`<br>\* `Integer filterFlags`<br>\* `Boolean match`<br>\* `Boolean fromReceiver`
`uninstalledListener` | \* `AppList.getUninstalled(Context, List<AppData>)`<br>\* Automatically when any app uninstalled<sup>1</sup> | \* `Context context`<br>\* `List<AppData> appDataList` | \* `List<AppData>`<br>\* `Boolean fromReceiver`
`sortListener` | \* `AppList.sort(List<AppData>, Integer, Integer, Integer)` | \* `List<AppData> appDataList`<br>\* `Integer sortBy`<br>\* `Integer inOrder`<br>\* `Integer uniqueIdentifier` | \* `List<AppData>`<br>\* `Integer sortBy`<br>\* `Integer inOrder`<br>\* `Integer uniqueIdentifier`

<sup>1</sup> - On Android versions >=8.0 only when application opened. This means that you should periodically check for new apps or in onResume. Works in background on lower Android versions.

### 2. "AppData"

All receivers receive a list of `AppData` objects. `AppData` contains this info about single application:

Information | Data Type | How to get | How to set
--- | --- | --- | ---
App Name | `String` | `.getAppName()` | `.setAppName(String)`
Package Name | `String` | `.getAppPackageName()` | `.setAppPackageName(String)`
Icon | `Drawable` | `.getAppIcon()` | `.setAppIcon(Drawable)`
Flags | `Integer` | `.getFlags()` | `.setFlags(Integer)`

### 3. Other Functionality

Library also provides functions to quickly compare applications (by their package names) and check application flags. More detailed information and examples can be found below.
<br><br>
---
<br><br>
## Usage

It is important that you review the overview section above before using this library to get acquainted with the basic functionality. Do not skim through the usage below because a tiny mistake might reduce functionality, make your application crash or create a memory leak.

### 1. Adding Dependency

![Progress1](/Progress/1.jpg?raw=true)

Add the dependency to your app's `build.gradle`:

```
    TODO: Adding Dependency
```

### 2. Registering Receiver

![Progress2](/Progress/2.jpg?raw=true)

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

### 3. Starting Functionality

![Progress3](/Progress/3.jpg?raw=true)

Decide whether your class will implement listeners or will create them in `onCreate` method.

1. If you have decided to implement listeners to your class, add this line of code to your `onCreate` method:
```
AppList.start(<yourClassName>.this,<yourClassName>.this,<yourClassName>.this,<yourClassName>.this);
```
You have to replace <yourClassName> to your class name that will implement listeners. If your class will implement only some of the listeners, replace `<yourClassName>.this` with `null`.

2. If you have decided to create listeners in your `onCreate` method, add this line of code to your `onCreate` method:
```
AppList.start(<allListenerName>,<newListenerName>,<uninstallListenerName>,<sortListenerName>);
```
You have to replace listener names with your actual listener names that you will use. If you will use only some of the listeners, replace listener name with `null`. **Note: this line of code has to be under your listeners in `onCreate` method.**

### 4. Stopping Functionality

![Progress4](/Progress/4.jpg?raw=true)

In your `onPause` method add this line of code to not create any memory leaks:
```
AppList.stop();
```

### 5. Adding Listeners

![Progress5](/Progress/5.jpg?raw=true)

You can choose to use only some of the listeners. You must use `allListener` or create `List<AppData>` with your application list manually so that other listeners could work as well. If you want to receive a list of new installed applications (invoked either manually or automatically), you have to use `newListener`. If you want to receive a list of uninstalled applications (invoked either manually or automatically), you have to use `uninstalledListener`.

#### "allListener"

![Progress5.1](/Progress/5.1.jpg?raw=true)

`allListener` receives 3 parameters - `List<AppData> appDataList`, which is a new list of all installed apps, `Integer filterFlags`, which informs what filters had been used when generating a list, and `Boolean match`, which informs whether apps in the list have flags or do not (read more in "Invoking Listeners" section). This is useful when you store multiple lists of different apps. When no filter was used, `filterFlags` is `null`. More about filters and flags in "Flags" section.

1. If you have decided to implement listeners to your class, override `allListener` method like this:
```
Override
public void allListener(List<AppData> appDataList, Integer filterFlags, Boolean match) {
    //Your code replacing main List<AppData> with a new one, invoking sort, notifying adapters about dataset
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `allListener` like this:
```
AllListener allListener = new AllListener() {
    @Override
    public void allListener(List<AppData> appDataList, Integer filterFlags, Boolean match) {
          //Your code replacing main List<AppData> with a new one, invoking sort, notifying adapters about dataset changing etc...      
    }
};
```

#### "newListener"

![Progress5.2](/Progress/5.2.jpg?raw=true)

`newListener` receives 4 parameters - `List<AppData> appDataList`, which is a list of new apps, `Integer filterFlags`, which informs what filters had been used when generating a list, `Boolean match`, which informs whether apps in the list have flags or do not (read more in "Invoking Listeners" section), and `Boolean fromReceiver`, which is `true` when a `BroadcastReceiver` invoked this listener - this means a list contains only one application and it did not check for filters (you may want to check application filters as described in "Checking Flags" section or manually).

1. If you have decided to implement listeners to your class, override `newListener` method like this:
```
@Override
public void newListener(List<AppData> appDataList, Integer filterFlags, Boolean match, Boolean fromReceiver) {
    //Your code adding new list to the main List<AppData>, invoking sorting, notifying adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `newListener` like this:
```
AllListener allListener = new AllListener() {
    @Override
    public void allListener(List<AppData> appDataList, Integer filterFlags, Boolean match) {
        //Your code adding new list to the main List<AppData>, invoking sorting, notifying adapters about dataset changing etc...
    }
};
```

#### "uninstalledListener"

![Progress5.3](/Progress/5.3.jpg?raw=true)

`uninstalledListener` receives 2 parameters - `List<AppData> appDataList`, which is a list of new apps, and `Boolean fromReceiver`, which is `true` when a `BroadcastReceiver` invoked this listener - this means a list contains only one application and only package name is correct (other data is `null`).

1. If you have decided to implement listeners to your class, override `uninstalledListener` method like this:
```
@Override
public void uninstalledListener(List<AppData> appDataList, Boolean fromReceiver) {
    //Your code removing apps from the main List<AppData>, invoking sorting, notifying adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `uninstalledListener` like this:
```
UninstalledListener uninstalledListener = new UninstalledListener() {
    @Override
    public void uninstalledListener(List<AppData> appDataList, Boolean fromReceiver) {
        //Your code removing apps from the main List<AppData>, invoking sorting, notifying adapters about dataset changing etc...
    }
};
```

#### "sortListener"

![Progress5.4](/Progress/5.4.jpg?raw=true)

Any given `AppData` list is unsorted, as given by the `PackageManager`. Because sorting takes additional time and resources, and is not needed for every app or every task, it is a separate listener. It is not integrated in other listeners because you might want to receive additional apps, add them to your app list and only sort after that.

`sortListener` receives 4 parameters - `List<AppData appDataList`, which is your sorter app list, `Integer sortBy`, which informs what was the sort type, `Integer inOrder`, which informs in what order the app list was sorted, and `Integer uniqueIdentifier`, which you might use to identify different tasks (where the sort method was invoked, what apps were in the list etc).

1. If you have decided to implement listeners to your class, override `sortListener` method like this:
```
@Override
public void sortListener(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) { {
    //Your code adding or removing apps from the main List<AppData>, notify adapters about dataset changing etc...
}
```
2. If you have decided to create listeners in your `onCreate` method, create a new `sortListener` like this:
```
SortListener sortListener = new SortListener() {
    @Override
    public void sortListener(List<AppData> appDataList, Integer sortBy, Integer inOrder, Integer uniqueIdentifier) {
        //Your code adding or removing apps from the main List<AppData>, notify adapters about dataset changing etc...
    }
};
```

Sort types:
1. `AppList.BY_APPNAME` (equals to 0). Sorts `AppData` list by app names.
2. `AppList.BY_PACKAGENAME` (equals to 1). Sorts `AppData` list by package names.

Order:
1. `AppList.IN_ASCENDING` (equals to 0). Sorts `AppData` list in ascending order (A to Z).
2. `AppList.IN_DESCENDING` (equals to 1). Sorts `AppData` list in descending order (Z to A).


### 6. Invoking Listeners

![Progress6](/Progress/6.jpg?raw=true)

#### "allListener"

![Progress6.1](/Progress/6.1.jpg?raw=true)

There are 2 ways to invoke `allListener`:
1. `AppList.getAll(Context);`<br>`Context` is your context.<br>For example: `AppList.getAll(getApplicationContext());`.
2. `AppList.getAll(Context, Integer, Boolean);`<br>`Context` is your context, `Integer` is your flag (flags), and `Boolean` is whether to return applications that match the given flags or those that do not match.<br>For example, `AppList.getAll(getApplicationContext(), ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP,true);` will return (to your `allListener`) all system apps. Changing `true` to `false` will return only user apps. To learn more about flags, go to "Flags" section.

It is recommended to invoke `allListener` when application is opened (`onCreate`) and when user refreshes app list (if possible).

#### "newListener"

![Progress6.2](/Progress/6.2.jpg?raw=true)

There are 3 ways to invoke `newListener`:
1. `AppList.getNew(Context, List<AppData>);`<br>`Context` is your context, and `List<AppData>` is a list with your current applications (application list will be compared to this list).<br>For example: `AppList.getNew(getApplicationContext(), AllAppsList);`.
2. `AppList.getNew(Context, List<AppData>, Integer, Boolean);`<br>`Context` is your context, `Integer` is your flag (flags), and `Boolean` is whether to return applications that match the given flags or those that do not match.<br>For example, `AppList.getNew(getApplicationContext(), AllAppsList, ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP,true);` will return (to your `newListener`) all new system apps. Changing `true` to `false` will return only user apps. To learn more about flags, go to "Flags" section.
3. It is invoked automatically when a new app is installed.<br>Limitations: on Android versions >=8.0 works only when application is opened, on lower Android versions it works in background .

It is recommended to invoke `newListener` periodically and when the app is reopened (`onResume`).

#### "uninstalledListener"

![Progress6.3](/Progress/6.3.jpg?raw=true)

There are 2 ways to invoke 'uninstalledListener':
1. `AppList.getUninstalled(Context, List<AppData>);`<br>`Context` is your context, and `List<AppData>` is a list with your current applications (application list will be compared to this list).<br>For example: `AppList.getUninstalled(getApplicationContext(), AllAppsList);`.
2. It is invoked automatically when any app is uninstalled.<br>Limitations: on Android versions >=8.0 works only when application is opened, on lower Android versions it works in background .

It is recommended to invoke `uninstalledListener` periodically and when the app is reopened (`onResume`).

#### "sortListener"

![Progress6.4](/Progress/6.4.jpg?raw=true)

There is only 1 way to invoke `sortListener`:<br>`AppList.sort(List<AppData>, Integer, Integer, Integer);`<br>`List<AppData>` is your app list, first `Integer` is your sort type, second `Integer` is your order, and a third `Integer` is your unique identifier. You might use it to identify different tasks (where the sort method was invoked, what apps were in the list etc).

Sort types:
1. `AppList.BY_APPNAME` (equals to 0). Sorts `AppData` list by app names.
2. `AppList.BY_PACKAGENAME` (equals to 1). Sorts `AppData` list by package names.

Order:
1. `AppList.IN_ASCENDING` (equals to 0). Sorts `AppData` list in ascending order (A to Z).
2. `AppList.IN_DESCENDING` (equals to 1). Sorts `AppData` list in descending order (Z to A).

### 7. Accessing "AppData" Contents

![Progress7](/Progress/7.jpg?raw=true)

`AppData` is an object with 4 values that can be read using getters and locally changed (in a list) using setters.

Information | Data Type | How to get | How to set
--- | --- | --- | ---
App Name | `String` | `.getAppName()` | `.setAppName(String)`
Package Name | `String` | `.getAppPackageName()` | `.setAppPackageName(String)`
Icon | `Drawable` | `.getAppIcon()` | `.setAppIcon(Drawable)`
Flags | `Integer` | `.getFlags()` | `.setFlags(Integer)`

**Note: App name, icon and flags can be `null`**. This is the case when `AppData` is received from a BroadcastReceiver in `uninstalledListener`.

### 8. Other Functionality

![Progress8](/Progress/8.jpg?raw=true)

#### Comparing

Because we can not get app names, icons and other data of already uninstalled apps, `AppData` `.equals()` is overridden to compare by package names. This includes `.contains(AppData)`, `.remove(AppData)`, `.removeAll(List<AppData>)` and others.

#### Flags

It is possible to filter app lists received by `allListener` and `newListener` by any combination of [flags](https://developer.android.com/reference/android/content/pm/ApplicationInfo#flags) and their opposites (if `FLAG_SYSTEM` filter returns a list of not updated systems apps, the opposite would be all user apps and updated system apps). You can also check individual `AppData`'s flags and check if it contains any combination of them.

##### Setting Flags

Description on how to filter app lists is in the "Invoking Listeners" section.

To set multiple flags you can use any combination of these operators:
* & - AND operator. Adds apps that have all filters.
* | - OR operator. Adds apps that have any of the given filters.
* ^ - XOR (exlcusive or) operator. Adds apps that have one but not the other filter.

##### Checking Flags

You can check if individual app has (or does not have) flags by using `ApList.checkFlags(AppData, Integer Boolean)`, where `AppData` is a single app, `Integer` is flags, `Boolean` is to check whether or not the app has flags (`true`) or quite the opposite (`false`). Alternatively, you can check flags  yourself by accessing app's flags using `.getFlags`.
<br><br>
---
<br><br>
## Other Information

### Donate
You can now donate to me (LayouXML) on **[Google Play](https://play.google.com/store/apps/details?id=com.layoutxml.support)** or **[PayPal](https://www.paypal.me/RJankunas)**.

### Author
More information about me: https://rokasjankunas.com

More information about my projects: https://layoutxml.com

### License
Twelveish is licensed under "MIT" license. Copyright laws apply.

Copyright © 2018 Rokas Jankūnas (LayoutXML)
