
package com.example.xiaotianweather.base;

import com.example.xiaotianweather.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPDialog extends ProgressDialog {

    public AnimationDrawable manimation;
    public static String str;
    public static int themeid;
    ImageView mdialogview;
    TextView mdialogtext;
    static CustomPDialog mdialog;

    public static CustomPDialog showPDialog(Context context, String text) {
        themeid = R.anim.progressdialog;
        str = text;
        mdialog = new CustomPDialog(context, text, themeid);
        return mdialog;
    }

    public CustomPDialog(Context context, String str, int theme) {
        super(context);
        setCanceledOnTouchOutside(true);
        // setOwnerActivity((Activity)context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.mprogressdialog);
        mdialogview = (ImageView) findViewById(R.id.mdialogview);
        mdialogtext = (TextView) findViewById(R.id.mdialogtext);
    }

    private void initData() {
        mdialogtext.setText(str);
        mdialogview.setBackgroundResource(themeid);
        manimation = (AnimationDrawable) mdialogview.getBackground();
        mdialogview.post(new Runnable() {

            @Override
            public void run() {
                manimation.start();
            }
        });
    }
}
