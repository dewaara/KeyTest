package com.seven.keytest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;

public class KeyHashUtility {
    
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void getKeys(Context context)
    {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature sig: packageInfo.signatures)
            {
                MessageDigest msgSHA1 = MessageDigest.getInstance("SHA");
                msgSHA1.update(sig.toByteArray());

                MessageDigest msgSHA256 = MessageDigest.getInstance("SHA-256");
                msgSHA256.update(sig.toByteArray());

                String sha1 = byteToHex(msgSHA1.digest());
                String sha256 = byteToHex(msgSHA256.digest());
                String keyHash = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    keyHash = new String(Base64.getEncoder().encode(msgSHA1.digest()));
                }

                Log.d("keysha1",sha1);
                Log.d("keysha256",sha256);
                Log.d("keyHash",keyHash);
            }
        }
        catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


}
