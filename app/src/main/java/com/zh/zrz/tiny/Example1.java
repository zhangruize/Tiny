package com.zh.zrz.tiny;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.zrz.tinyviewmodel.TinyViewModel;

public class Example1 extends TinyViewModel implements View.OnClickListener {
    TextView textView;
    Button button;
    Dialog dialog;

    @Override
    public View getView(Context context) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout1;
    }

    @Override
    public void initView(View v) {
        textView = v.findViewById(R.id.layout1_title);
        button = v.findViewById(R.id.layout1_button);
        textView.setText("That's awasome");
        button.setOnClickListener(this);
        dialog = new Dialog(v.getContext());
        dialog.setContentView(createView(v.getContext(), R.layout.dialog));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout1_button:
                dialog.show();
                break;
        }
    }
}
