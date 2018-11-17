package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jun.baiduidentityauthapp.R;

/**
 * 活体检测失败
 */
public class LivingAuthFailureActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_auth_failure);

        findViewById(R.id.skip_button).setOnClickListener(this);
        findViewById(R.id.try_again_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_button:
                startActivity(new Intent(this, IDCardAuthActivity.class));
                finish();
                break;
            case R.id.try_again_button:
                startActivity(new Intent(this, LivingDetection2Activity.class));
                finish();
                break;
            default:
        }
    }
}