package com.example.yuedan.ContinuousText;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

    private byte flag = 0x00;// 00000000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText1 = (EditText) findViewById(R.id.EditText01);
        editText2 = (EditText) findViewById(R.id.EditText02);
        editText3 = (EditText) findViewById(R.id.EditText03);

        editText1.setTag(1);
        editText2.setTag(2);
        editText3.setTag(3);

        // 添加 内容change listener ：输入焦点后移 + 密码验证
        editText1.addTextChangedListener(new MyTextChangeWatcher(1));
        editText2.addTextChangedListener(new MyTextChangeWatcher(2));
        editText3.addTextChangedListener(new MyTextChangeWatcher(3));

        // del 监听，输入焦点前移
        editText2.setOnKeyListener(keyListener);
        editText3.setOnKeyListener(keyListener);
    }

    /**
     * 对指定位 进行位操作
     *
     * @param isNull true：当前值为null ，清零。false：有值，该标志位 给1.
     * @param index  标志位index
     */
    private void setFlag(boolean isNull, int index) {
        // 得到 唯一一个 1的二进制数 00001000
        byte b = (byte) (1 << (index - 1));
        if (isNull) {// 指定 位 清零
            b = (byte) ~b; // 11110111
            flag = (byte) (flag & b);
        } else {// 制定位 赋值 1
            flag = (byte) (flag | b);
        }
    }

    /**
     * 监听删除键 前移焦点
     */
    private OnKeyListener keyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((((EditText) v).getText().toString() == null || ((EditText) v)
                    .getText().toString().isEmpty())
                    && keyCode == KeyEvent.KEYCODE_DEL
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                // 该EditText的 内容已为空，并且 del 键按下
                v.clearFocus();// 清除该控件焦点
                // 将焦点给到前面一个EditText
                EditText editText = getEditTextFromIndex(Integer
                        .parseInt(String.valueOf(v.getTag())) - 1);
                // editText.requestFocus(); //也可以
                editText.requestFocusFromTouch();
            }

            return false;
        }
    };

    private EditText getEditTextFromIndex(int index) {
        switch (index) {
            case 1:
                return editText1;
            case 2:
                return editText2;
            case 3:
                return editText3;
            default:
                break;
        }
        return null;
    }

    class MyTextChangeWatcher implements TextWatcher {
        // 标示 绑定的EditText
        private int index;

        public MyTextChangeWatcher(int index) {
            super();
            this.index = index;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() == 3) {
                if (index < 3) {// 焦点后移
                    getEditTextFromIndex(index).clearFocus();
                    getEditTextFromIndex(index + 1).requestFocusFromTouch();
                }
                setFlag(false, index);// 对应标志位置
                // 有内容输入，判断密码是否输入OK
            } else {
                // 清除 对应 标识位
                setFlag(true, index);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    }
}
