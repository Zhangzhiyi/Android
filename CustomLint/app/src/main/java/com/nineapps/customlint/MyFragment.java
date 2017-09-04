package com.nineapps.customlint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by zhangzhiyi on 2017/5/26.
 */

public class MyFragment extends Fragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        add(1, "123");

        getArguments().getInt("int");

//        if (getArguments() != null) {
            getArguments().getString("str");
//        }


        Toast.makeText(getActivity(), "123", Toast.LENGTH_SHORT).show();
    }

    void add(int i, String s) {
        getArguments().getLong("long");
    }
}
