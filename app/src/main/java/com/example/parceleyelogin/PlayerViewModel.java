package com.example.parceleyelogin;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.lifecycle.ViewModel;
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

public class PlayerViewModel extends ViewModel {
    public final String TAG = "PlayerViewModel";
    private ExoPlayer exoPlayer;

    @OptIn(markerClass = UnstableApi.class)
    public ExoPlayer getExoPlayer(Context context) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).setMediaSourceFactory(
                    new DefaultMediaSourceFactory(context).setLiveTargetOffsetMs(1000)).build();

            // Create a data source factory.
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();

            // Create a HLS media source pointing to a playlist uri and set it to player
            HlsMediaSource hlsMediaSource =
                    new HlsMediaSource.Factory(dataSourceFactory)
                            .setExtractorFactory(new DefaultHlsExtractorFactory())
                            .setAllowChunklessPreparation(false)
                            .createMediaSource(MediaItem.fromUri("https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8"));
            exoPlayer.setMediaSource(hlsMediaSource);
            exoPlayer.addAnalyticsListener(new EventLogger());
            exoPlayer.addListener(
                    new Player.Listener() {
                        @Override
                        public void onTimelineChanged(
                                @NonNull Timeline timeline, @Player.TimelineChangeReason int reason) {
                            Object manifest = exoPlayer.getCurrentManifest();
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
                            Log.v(TAG, "Playback State: " + exoPlayer.getPlaybackState());
                            Log.v(TAG, "Suppressed:" + exoPlayer.getPlaybackSuppressionReason());
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
        }
        return exoPlayer;
    }

    @Override
    protected void onCleared() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
