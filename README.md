Android-Home-Screen-Shortcut-Creator
====================================

Sample Code of creating/removing app shortcut in home screen:

        HomeScreenShortcutManager homeScreenShortcutManager = new HomeScreenShortcutManager(this);
        homeScreenShortcutManager.createShortcut();
        homeScreenShortcutManager.removeShortcut();
        
Need to add permission in manifest.xml to work right :

    <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />

