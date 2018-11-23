package rottenstudentertainment.hyperfitness.AdMob;

import android.content.Context;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdMob {

    public static void initializeAdMob( Context context){
        String AdMob_App_Id = context.getString( R.string.admob_app_id);
        MobileAds.initialize( context, AdMob_App_Id);
    }


    public static void loadAdBanner( Context context){
        AdView mAdView = ((OpenGLES20Activity)context).findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.bringToFront(); // sichern, das werbung gesehen wird
    }


}
