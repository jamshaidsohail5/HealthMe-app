package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IBM on 11/29/2016.
 */
public class fbint extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        printhaskey();
    }
    public void printhaskey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hayyaalassalah.faizanahmad.healthyme",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}
