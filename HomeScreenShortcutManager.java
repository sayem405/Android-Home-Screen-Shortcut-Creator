import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by sayem on 11/26/14.
 * Required to add in manifest.xml:
 *<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 *<uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 */
public class HomeScreenShortcutManager {

    private final String TAG = "HomeScreenShortcutManager";
    private final String EXCEPTION = "Exception :";

    private Context mContext;
    private String mPackageName;
    private PackageManager mPackageManager;


    public HomeScreenShortcutManager(Context context) {
        mContext = context;
        mPackageName = mContext.getPackageName();
        mPackageManager = mContext.getPackageManager();

    }

    public boolean createShortcut() {
        Intent intent = prepareShortcutIntent();
        if (intent != null) {
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            mContext.sendBroadcast(intent);
            return true;
        } else
            return false;
    }

    public boolean removeShortcut() {
        Intent intent = prepareShortcutIntent();
        if (intent != null) {
            intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            mContext.sendBroadcast(intent);
            return true;
        } else
            return false;
    }

    private Intent prepareShortcutIntent() {

        String appName = getAppName();
        int appIconResourceId = getAppIcon();
        Class<?> c = getMainLauncherClass();

        if (appIconResourceId != -1 && c != null) {
            Intent shortcutIntent = new Intent(mContext.getApplicationContext(), c);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(mContext, appIconResourceId));
            return intent;
        } else {
            return null;
        }
    }

    private Class<?> getMainLauncherClass() {
        try {
            Intent launchIntent = mPackageManager.getLaunchIntentForPackage(mPackageName);
            String className = launchIntent.getComponent().getClassName();
            Class<?> c = Class.forName(className);
            return c;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, EXCEPTION, e);
            return null;
        }
    }

    private String getAppName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        String appName = (String) (applicationInfo != null ? mPackageManager
                .getApplicationLabel(applicationInfo) : "Unknown");
        return appName;
    }

    private int getAppIcon() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        return (applicationInfo != null) ? applicationInfo.icon : -1;
    }

    private ApplicationInfo getApplicationInfo() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mPackageManager.getApplicationInfo(mPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, EXCEPTION, e);
        }
        return applicationInfo;
    }
}
