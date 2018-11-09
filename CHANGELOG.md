# Changelog for AppListManager (Android Library)

## Version 2.1.0

- Added a new generic type object to AppData (variable called object). It is an additional variable that you can use for your own needs. If you need multiple variables, create a new wrapper object (new type) to hold those variables. Can be used with `getObject()` and `setObject(Object)`, where Object is any type.

## Version 2.0.0
**NOTE**: This version introduces backwards incompatible changes. Follow the steps below on how to solve issues.

- Added ability to filter by application permissions:
    - All `getSome...` methods now require two additional parameters - `String[]` (which contains permissions), and `Boolean` (which tells whether to look for applications that contain at least one of the given permissions or not).
    - All listeners (except for `sortListener`) - `activityListener`, `appListener`, `newActivityListener`, `newAppListener`, `uninstalledActivityListener`, `uninstalledAppListener` - now receive two additional parameters (look the point above). If not applicable, then they are null and false.
    - AppData object now holds application permissions (if you are working with activity lists, this field still contains application permissions) that can be accessed with `getPermissions()` and `setPermissions(...)`.
    - `checkApplicationPermissions` method which allows checking whether a single AppData object contains (or does not contain) at least one of the given permissions.

For more information read [README.md](https://github.com/LayoutXML/AppListManager/blob/master/README.md).

## Version 1.1.0
- Added `BY_APPNAME_IGNORE_CASE` for sorting.
- Updated dependencies

## Version 1.0.0
- Initial release
