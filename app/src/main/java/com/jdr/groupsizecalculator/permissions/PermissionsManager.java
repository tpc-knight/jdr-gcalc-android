package com.jdr.groupsizecalculator.permissions;

import android.app.Activity;

public interface PermissionsManager {
    boolean hasStoragePermissions(Activity activity);
}
