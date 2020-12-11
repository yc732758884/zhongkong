package com.shtt.zhongkong;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MySwitch  extends LinearLayout {

    boolean open =true ;
    Context context;

    View view_open,View_close;
    TextView tv_open,tv_close;



    public MySwitch(@NonNull Context context) {
//        super(context);
        this(context, null);

//        initView();

    }

    public MySwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);

        this(context, attrs, 0);

   //     initView();

    }

    public MySwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.open=true;
        initView();

    }

    void  initView(){

        View.inflate(context,R.layout.layout_switch,this);
        findViewById(R.id.cl).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listener!=null) {
                    listener.click(!open);
                }
            }
        });



        view_open=findViewById(R.id.view_open);
        View_close=findViewById(R.id.view_close);

        tv_open=findViewById(R.id.tv_open);

        tv_close=findViewById(R.id.tv_close);



    }

    public   void setOpen(boolean bol ){

        this.open=bol;

        if (bol){
            view_open.setVisibility(View.VISIBLE);
            View_close.setVisibility(View.GONE);
            tv_open.setVisibility(View.VISIBLE);
            tv_close.setVisibility(View.GONE);

        }else {
            view_open.setVisibility(View.GONE);
            View_close.setVisibility(View.VISIBLE);
            tv_open.setVisibility(View.GONE);
            tv_close.setVisibility(View.VISIBLE);
        }

      postInvalidate();
    }





    MySwitchListener listener;


    public  void setListener(MySwitchListener listener){
        this.listener=listener;

    }
     public  interface MySwitchListener{
         void click(boolean open);
    }

    public  void  setOpenText(String str){
        tv_open.setText(str);
    }

    public  void  setColseText(String str){
        tv_close.setText(str);
    }

}
