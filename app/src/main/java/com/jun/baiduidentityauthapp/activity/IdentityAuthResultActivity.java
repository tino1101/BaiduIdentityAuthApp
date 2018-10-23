package com.jun.baiduidentityauthapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.jun.baiduidentityauthapp.R;

/**
 * 活体认证和身份证验证结果
 */
public class IdentityAuthResultActivity extends Activity {

    private TextView nameTextView;
    private TextView sexTextView;
    private TextView nationTextView;
    private TextView birthdayTextView;
    private TextView addressTextView;
    private TextView numberTextView;
    private TextView similarityTextView;

    private double matchScore = -1;

    private String name = "";
    private String sex = "";
    private String nation = "";
    private String birthday = "";
    private String address = "";
    private String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_auth_result);
        name = getIntent().getStringExtra("name");
        sex = getIntent().getStringExtra("sex");
        nation = getIntent().getStringExtra("nation");
        birthday = getIntent().getStringExtra("birthday");
        address = getIntent().getStringExtra("address");
        number = getIntent().getStringExtra("number");
        matchScore = getIntent().getDoubleExtra("matchScore", -1);
        initViews();
    }

    private void initViews() {
        nameTextView = findViewById(R.id.name);
        sexTextView = findViewById(R.id.sex);
        nationTextView = findViewById(R.id.nation);
        birthdayTextView = findViewById(R.id.birthday);
        addressTextView = findViewById(R.id.address);
        numberTextView = findViewById(R.id.number);
        similarityTextView = findViewById(R.id.similarity);

        nameTextView.setText("姓名：" + name);
        sexTextView.setText("性别：" + sex);
        nationTextView.setText("民族：" + nation);
        birthdayTextView.setText("出生日期：" + birthday);
        addressTextView.setText("住址：" + address);
        numberTextView.setText("身份证号码：" + number);

        if (matchScore > -1) {
            similarityTextView.setVisibility(View.VISIBLE);
            similarityTextView.setText("照片和身份证匹配度：" + matchScore);
        } else {
            similarityTextView.setVisibility(View.GONE);
        }
    }
}