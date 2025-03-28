package com.example.parceleyelogin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.hls.DefaultHlsExtractorFactory;
import androidx.media3.exoplayer.hls.HlsManifest;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.util.EventLogger;
import androidx.media3.ui.PlayerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CameraFragment extends Fragment {

    public final String TAG = "CameraFragment";

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        // Get PlayerView
        PlayerView playerView = rootView.findViewById(R.id.player_view);

        // Get context
        Context context = requireContext();

        // Bind ExoPlayer to PlayerView
        ExoPlayer player = new ExoPlayer.Builder(context)
                .setMediaSourceFactory(
                        new DefaultMediaSourceFactory(context).setLiveTargetOffsetMs(5000))
                .build();
        playerView.setPlayer(player);

        // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();

        // Create a HLS media source pointing to a playlist uri and set it to player
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .setExtractorFactory(new DefaultHlsExtractorFactory())
                        .setAllowChunklessPreparation(false)
                        .createMediaSource(MediaItem.fromUri("https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8"));
        player.setMediaSource(hlsMediaSource);
        player.addAnalyticsListener(new EventLogger());
        player.addListener(
                new Player.Listener() {
                    @Override
                    public void onTimelineChanged(
                            @NonNull Timeline timeline, @Player.TimelineChangeReason int reason) {
                        Object manifest = player.getCurrentManifest();
                        if (manifest != null) {
                            HlsManifest hlsManifest = (HlsManifest) manifest;
                            Log.v(TAG,"HLS segments length: " + hlsManifest.mediaPlaylist.segments.size());
                        }
                    }

                    @Override
                    public void onPositionDiscontinuity(@NonNull Player.PositionInfo oldPosition, @NonNull Player.PositionInfo newPosition, int reason) {
                        Log.v(TAG, "onPositionDiscontinuity: " + reason);
                        Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
                    }

                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {
                        if (isPlaying) {
                            // Active playback.
                        } else {
                            // Not playing because playback is paused, ended, suppressed, or the player
                            // is buffering, stopped or failed. Check player.getPlayWhenReady,
                            // player.getPlaybackState, player.getPlaybackSuppressionReason and
                            // player.getPlaybackError for details.
                            Log.v(TAG, "Playback State: " + player.getPlaybackState());
                            Log.v(TAG, "Suppressed:" + player.getPlaybackSuppressionReason());
                        }
                    }

                    @Override
                    public void onEvents(@NonNull Player player, @NonNull Player.Events events) {
                        //Log.v(TAG, String.valueOf(events.size()));
                        Player.Listener.super.onEvents(player, events);
                    }

                    @Override
                    public void onPlayerError(@NonNull PlaybackException error) {
                        Log.e(TAG, "onPlayerError: " + error.getMessage());
                        Player.Listener.super.onPlayerError(error);
                    }
                });

        // Build the media item.
//        String mediaUri = "http://54.251.147.213:8080/hls/stream.m3u8";
//        MediaItem mediaItem = new MediaItem.Builder()
//                .setUri(mediaUri)
//                .setLiveConfiguration(
//                        new MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build())
//                .build();
//        // Set the media item to be played.
//        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

        return rootView;
    }
}