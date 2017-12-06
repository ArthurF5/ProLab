package com.moliveiralucas.prolab.Controle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moliveiralucas.prolab.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Preferencias extends Fragment {


    public Preferencias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferencias, container, false);
    }

}
