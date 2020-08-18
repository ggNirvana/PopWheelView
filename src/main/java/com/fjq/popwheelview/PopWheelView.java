package com.fjq.popwheelview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;


public abstract class PopWheelView<T> {

    private Context mContext;
    private List<T> items;
    private Dialog bgSetDialog;
    private LoopView loopView;
    private int dialogWidth;

    public PopWheelView(Context mContext, List<T> items) {
        this.mContext = mContext;
        this.items = items;
        initDialog();
        initLoopView();

    }


    public abstract void OnCancelButtonClicked();

    public abstract void OnConfirmButtonClicked(int currentIndex);

    public abstract void onLoopViewSelected(int index);



    public void showDialog() {
        bgSetDialog.show();
    }

    public void setDialogWidth(int dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    public void setNotLoop() {
        loopView.setNotLoop();
    }


    private void initDialog() {
        dialogWidth = getScreenWidth();
        bgSetDialog = new Dialog(mContext, R.style.BottomDialogStyle);


        //填充对话框的布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_wheel_pop_dialog, null);
        TextView btCancel = view.findViewById(R.id.btn_cancel);
        TextView btConfirm = view.findViewById(R.id.btn_confirm);
        loopView = view.findViewById(R.id.loop_view);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnCancelButtonClicked();
                bgSetDialog.dismiss();
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = loopView.getSelectedItem();
                OnConfirmButtonClicked(currentIndex);
                bgSetDialog.dismiss();
            }
        });

        //将布局设置给Dialog
        bgSetDialog.setContentView(view);

        //获取当前Activity所在的窗体
        Window dialogWindow = bgSetDialog.getWindow();

        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);

        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = (int) (dialogWidth);
        lp.y = 0; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体
    }

    private void initLoopView() {
        loopView.setItems(itemsToString());
        loopView.setInitPosition(0);//初始化，设置默认选项
        loopView.setListener(new OnItemSelectedListener() {//设置监听
            @Override
            public void onItemSelected(int index) {
                onLoopViewSelected(index);
            }
        });
    }

    private List<String> itemsToString() {
        List<String> strings = new ArrayList<>();
        for (T item : items) {
            strings.add(item.toString());
        }
        return strings;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return mContext.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }


}
