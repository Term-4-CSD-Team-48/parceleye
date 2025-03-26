package com.example.parceleyelogin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CameraFragment extends Fragment {


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

        // Build the media item.
        String mediaUri = "https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8";
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(mediaUri)
                .setLiveConfiguration(
                        new MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build())
                .build();
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

        return rootView;
    }
}