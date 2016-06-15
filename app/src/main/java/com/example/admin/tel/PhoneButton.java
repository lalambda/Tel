package com.example.admin.tel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yaoyafeng on 16/5/30.
 */
public class PhoneButton extends LinearLayout{

    TextView num;
    TextView alpha;
    LinearLayout childLinear;
    View view;
    OnClickListener onClickListener;

    public PhoneButton(Context context) {
        super(context);
    }

    public PhoneButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PhoneButton);
        CharSequence textNum = ta.getText(R.styleable.PhoneButton_textNum);
        Float textNumSize = ta.getDimension(R.styleable.PhoneButton_textNumSize, 20);
        CharSequence textAlpha = ta.getText(R.styleable.PhoneButton_textAlphabet);
        Float textAlphaSize = ta.getDimension(R.styleable.PhoneButton_textAlphaSize, 12);
        view = LayoutInflater.from(context).inflate(R.layout.phone_button_content, this);
        num = (TextView) view.findViewById(R.id.phonebtn_number_text);
        alpha = (TextView) view.findViewById(R.id.phonebtn_alphabet_text);
        if (textNum==null){
            textNum = "";
        }
        childLinear = (LinearLayout) findViewById(R.id.linear_listener);
        childLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickListener.onClick(view);
            }
        });
        num.setText(textNum);
        num.setTextSize(textNumSize);
        if (textAlpha==null||textAlpha.equals("")) {
            alpha.setVisibility(INVISIBLE);
        }else {
            alpha.setText(textAlpha);
            alpha.setTextSize(textAlphaSize);
        }
        ta.recycle();
    }

    //将父控件的监听事件转移给子控件
    public void setOnClickListener(PhoneButton.OnClickListener l) {
        onClickListener = l;
    }

    public String getNumText() {
        return num.getText().toString();
    }

    public void setNumText(String num) {
        this.num.setText(num);
    }

    public String getAlphapetText() {
        return alpha.getText().toString();
    }

    public void setAlphapetText(String alpha) {
        this.alpha.setText(alpha);
    }
    interface OnClickListener{
        void onClick(View v);
    }

}
