package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jun.baiduidentityauthapp.R;

/**
 * 开始身份证检测识别
 */
public class IDCardAuthActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_auth);
        findViewById(R.id.next_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                Intent i = new Intent(this, IDCardScanActivity.class);
                startActivity(i);
                finish();
                break;
            default:
        }
    }
}