package com.example.parceleyelogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

/*-- TODO: code for out of delivery, replace text with checker,
      set all elements to invisible if nothing */

//-- TODO: delivery number checker //
//-- TODO: call delivery number checker and status//
//-- TODO: call image thumbnail for delivery location//