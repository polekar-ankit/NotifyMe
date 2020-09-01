/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gipl.notifyme.ui.videoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityPlayerBinding;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;



/**
 * A fullscreen activity to play audio or video streams.
 * Naresh
 */
public class PlayerActivity extends BaseActivity<ActivityPlayerBinding, PlayerViewModel> {
    private ActionBar actionbar;
    private static final String KEY_URL = "url";
    private static final String KEY_TYPE = "keyType";
    private static final String KEY_PLAYBACK_POSITION = "KEY_PLAYBACK_POSITION";
    private static final String KEY_CURRENT_WINDOW = "KEY_CURRENT_WINDOW";
    private static final String KEY_PLAY_WHEN_READY = "KEY_PLAY_WHEN_READY";
    private static final String TAG = PlayerActivity.class.getSimpleName();

    private SimpleExoPlayer player;
    //private PlayerView playerView;
    private PlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private Uri uri;
    private Toolbar toolbarl;
    private String type;
    private String stringUrl;
    private ActivityPlayerBinding binding;

    @Inject
    PlayerViewModel playerViewModel;


    public static void start(Context context, String mp4videoUrl) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(KEY_URL, mp4videoUrl);
        context.startActivity(intent);
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }


    @Override
    public PlayerViewModel getViewModel() {
        return playerViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = getViewDataBinding();
        playerView = binding.videoView;
        stringUrl = getIntent().getStringExtra(KEY_URL);

        if (stringUrl != null) {
            uri = Uri.parse(stringUrl);
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // landscape
                hideSystemUI();
            } else {
                // portrait
                showSystemUI();
            }
        }

        //setActionBar(binding.toolbar, "");
    }

    public void setActionBar(Toolbar toolbar, String sTitle) {
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setTitle(sTitle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
       /* playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();*/

        outState.putLong(KEY_PLAYBACK_POSITION, playbackPosition);
        outState.putInt(KEY_CURRENT_WINDOW, currentWindow);
        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
        }
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        /*decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void initializePlayer() {

//        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
//                new DefaultTrackSelector(), new DefaultLoadControl());

        player = ExoPlayerFactory.newSimpleInstance(this,new DefaultRenderersFactory(this)
                ,new DefaultTrackSelector(), new DefaultLoadControl());
        player.addListener(new ExoPlayerEventsListener(new WeakReference<Context>(this)));
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri mp4VideoUri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
        return videoSource;
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi1() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    /*private String getVideoLink(Video video) {
        String link = null;
        ArrayList<VideoFile> videoFiles = video.getDownload();
        if (videoFiles != null && !videoFiles.isEmpty()) {
            VideoFile videoFile = videoFiles.get(0); // you could sort these files by size, fps, width/height
            link = videoFile.getLink();
        }
        return link;
    }*/

}


class ExoPlayerEventsListener extends Player.DefaultEventListener {
    private static final String TAG = ExoPlayerEventsListener.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;

    public ExoPlayerEventsListener(@NonNull WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                showErrorMessage();
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                showErrorMessage();
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                showErrorMessage();
                break;
        }
    }

    private void showErrorMessage() {
        Context context = contextWeakReference.get();
        if (context != null) {
            Toast.makeText(context, R.string.video_play_error, Toast.LENGTH_SHORT).show();
        }
    }

}