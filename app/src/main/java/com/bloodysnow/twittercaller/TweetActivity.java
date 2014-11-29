package com.bloodysnow.twittercaller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.bloodysnow.twittercaller.R;

public class TweetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        Intent intent = getIntent();
        tweet(intent == null ? null : intent.getStringExtra(Intent.EXTRA_TEXT));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(resultCode == RESULT_OK ? TwitterCaller.ACTION_TWEET_SUCCESS : TwitterCaller.ACTION_TWEET_CANCEL);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void tweet(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivityForResult(intent, 0);
    }
}