package cn.demoz.www.demos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.demoz.www.R;
import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.demos.view.ClockView;

public class ClockViewFragment extends BaseDemosFragment implements View.OnClickListener{

    ClockView clockView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_clock_view_fragment, null);
        final TextView tv = (TextView) view.findViewById(R.id.tv);
        clockView = (ClockView) view.findViewById(R.id.clockView);

        view.findViewById(R.id.btn1).setOnClickListener(this);
        view.findViewById(R.id.btn2).setOnClickListener(this);
        view.findViewById(R.id.btn3).setOnClickListener(this);

        clockView.setOnTimeChangeListener(new ClockView.OnTimeChangeListener() {
            @Override
            public void onTimeChange(View view, int hour, int minute, int second) {
                tv.setText(String.format("%s-%s-%s", hour, minute, second));
            }
        });
        
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                clockView.setTime(11, 59, 55);
                break;
            case R.id.btn2:
                clockView.setTime(20, 30, 0);
                break;
            case R.id.btn3:
                clockView.setTime(23, 59, 55);
                break;
        }
    }
}
