package com.application.intercom.helper;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class DynamicLinks {
    private static final String TAG = "FirebaseDynamicLinks";
    public static final String DOMAIN_PREFIX = "https://intercomapp.page.link";
    public static final String WEBSITE_PREFIX = "https://intercomapp.app/";
    public static final String IOS_BUNDLE_ID = "com.application.intercom";
    //public static final String IOS_BUNDLE_ID="com.mobulous.StreetTalk";
    //public static final String WEBSITE_PREFIX=" https://streettak.com/#/street-talk-post/";

    public static void createFirebaseDynamicLink(Activity activity, String productInfo, String title, String description, String imageUrl) {
        String URL_LINK = "https://intercomapp.page.link/Go1D/" + "Community/" + productInfo;

        DynamicLink.SocialMetaTagParameters socialMetaTagParameters;
        if (ValidationUtils.stringNullValidation(imageUrl)) {
            socialMetaTagParameters = new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .setImageUrl(Uri.parse(imageUrl))
                    .build();
        } else {
            socialMetaTagParameters = new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .build();
        }


        /*DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(URL_LINK))
                .setDomainUriPrefix(DOMAIN_PREFIX)

                // Open links with this app on Android
                // .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(socialMetaTagParameters)
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(IOS_BUNDLE_ID).build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        try {
            shareIntent(activity, URLDecoder.decode(dynamicLinkUri.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }*/


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(URL_LINK))
                .setSocialMetaTagParameters(socialMetaTagParameters)
                .setDomainUriPrefix(DOMAIN_PREFIX)
                .setIosParameters(new DynamicLink.IosParameters.Builder(IOS_BUNDLE_ID).build())
                .buildShortDynamicLink()
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        Log.d("MyLink", "onComplete: " + shortLink.toString());
                        try {
                            shareIntent(activity, URLDecoder.decode(shortLink.toString(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {

                    }
                });
    }


    public static void shareIntent(Activity activity, String shearableLink) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setText(shearableLink)
                .setType("text/plain")
                .setChooserTitle("Share with the users")
                .startChooser();
    }


}
