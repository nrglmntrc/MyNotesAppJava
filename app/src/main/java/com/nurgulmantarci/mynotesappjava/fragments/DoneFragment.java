package com.nurgulmantarci.mynotesappjava.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.activities.AddNoteActivity;


public class DoneFragment extends Fragment  {

    Context context;
    FloatingActionButton fab;

    public DoneFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_done, container, false);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab=getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddNoteActivity.class);
                startActivity(intent);
            }
        });


    }

    





}





