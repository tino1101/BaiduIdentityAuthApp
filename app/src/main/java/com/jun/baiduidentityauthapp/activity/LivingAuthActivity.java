package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jun.baiduidentityauthapp.R;
import com.jun.baiduidentityauthapp.util.IdentityAuthHelper;
import com.jun.baiduidentityauthapp.util.ToastUtil;

/**
 * 开始活体检测
 */
public class LivingAuthActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_auth);
        findViewById(R.id.next_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                if (IdentityAuthHelper.API_KEY.equals("Your API KEY") || IdentityAuthHelper.SECRET_KEY.equals("Your API KEY")) {
                    ToastUtil.showToast(LivingAuthActivity.this, "请先配置 API KEY 和 SECRET KEY");
                    return;
                }
                Intent i = new Intent(this, LivingDetectionActivity.class);
                startActivity(i);
                finish();
                break;
            default:
        }
    }
}