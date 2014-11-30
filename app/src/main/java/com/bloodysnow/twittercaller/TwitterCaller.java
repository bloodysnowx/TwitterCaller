package com.bloodysnow.twittercaller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.unity3d.player.UnityPlayer;

public class TwitterCaller {
    public interface Callback {
        public void onResult(int resultCode);
    }

    public static final String ACTION_TWEET_SUCCESS = TwitterCaller.class.getName() + ".SUCCESS";
    public static final String ACTION_TWEET_CANCEL = TwitterCaller.class.getName() + ".CANCEL";

    public void callFromUnity(final Context context, String text, final String gameObjectName, final String callbackMethodName) {
        tweet(context, text, new Callback() {
            @Override
            public void onResult(int resultCode) {
                UnityPlayer.UnitySendMessage(gameObjectName, callbackMethodName, resultCode == Activity.RESULT_OK ? "OK" : "CANCELED");
            }
        });
    }

    public void tweet(Context context, String text, final Callback callback) {
        final BroadcastReceiver tweetBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                callback.onResult(intent.getAction().equals(ACTION_TWEET_SUCCESS) ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TWEET_SUCCESS);
        intentFilter.addAction(ACTION_TWEET_CANCEL);
        context.registerReceiver(tweetBroadcastReceiver, intentFilter);
        Intent intent = new Intent(context, TweetActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }
}