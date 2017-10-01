package com.quesan.app.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;

/**
 * Created by Dinesh Singh on 8/25/2017.
 */

public class UserData implements UserInfo
{


    @Override
    public String getUid() {
        return null;
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    @Nullable
    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }
}
