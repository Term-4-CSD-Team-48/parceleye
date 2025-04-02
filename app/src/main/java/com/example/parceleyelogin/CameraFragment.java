package com.example.parceleyelogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CameraFragment extends Fragment {

    public final String TAG = "CameraFragment";
    private PlayerViewModel playerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get context
        Context context = requireContext();

        // Get PlayerView and ExoPlayer and set the player to PlayerView
        PlayerView playerView = view.findViewById(R.id.player_view);
        ExoPlayer player = playerViewModel.getExoPlayer(context);
        playerView.setPlayer(player);

        // Dynamically set playerView height
        playerView.getLayoutParams().height = (getResources().getDisplayMetrics().widthPixels*9)/16;

        // Set prompt button listener
        final boolean[] disabledListener = {false};
        FloatingActionButton button = view.findViewById(R.id.promptButton);
        button.setOnClickListener(v -> {
            if (disabledListener[0]) {
                playerView.setOnTouchListener(null);
            } else {
                playerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Get raw touch position inside PlayerView
                        float x = event.getX();
                        float y = event.getY();

                        int viewWidth = playerView.getWidth();
                        int viewHeight = playerView.getHeight();

                        Log.i(TAG, "Tapped at: (" + x + ", " + y + ")");

                        // Convert coordinates to match video resolution
                        float videoWidth = 640f;  // Example resolution (replace dynamically)
                        float videoHeight = 360f;

                        float relativeX = (x / viewWidth) * videoWidth;
                        float relativeY = (y / viewHeight) * videoHeight;

                        Log.i(TAG, "Mapped to video: (" + relativeX + ", " + relativeY + ")");

                        ApiClient.prompt((int) relativeX, (int) relativeY, new ApiClient.CallbackParts() {});

                        return true; // Return false to allow default controls (play/pause)
                    }
                });
            }
            disabledListener[0] = !disabledListener[0];
        });

        // Start playing
        player.prepare();
        player.play();
    }

}