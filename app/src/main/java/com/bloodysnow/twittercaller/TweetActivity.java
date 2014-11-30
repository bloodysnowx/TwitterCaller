package com.bloodysnow.twittercaller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

public class TweetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String text = (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT)) ? null : intent.getStringExtra(Intent.EXTRA_TEXT);
        String textureUrl = (intent == null || !intent.hasExtra(Intent.EXTRA_STREAM)) ? null : intent.getStringExtra(Intent.EXTRA_STREAM);
        tweet(text, textureUrl);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(resultCode == RESULT_OK ? TwitterCaller.ACTION_TWEET_SUCCESS : TwitterCaller.ACTION_TWEET_CANCEL);
        sendBroadcast(intent);
        finish();
    }

    private void tweet(String text, String textureUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(textureUrl == null ? "text/plain" : "image/png");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if(textureUrl != null) intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(textureUrl)));

        for(ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            if(resolveInfo.activityInfo.name.contains("twitter")) {
                intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                break;
            }
        }

        startActivityForResult(intent, 0);
    }
}