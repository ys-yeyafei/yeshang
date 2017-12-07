package cn.ysgroup.ysdai.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linyujie on 16/10/21.
 */

public abstract class BaseFragment extends Fragment{
    private Context context;
    private String from;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    public abstract View getmView();


    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}

