package com.example.viewpagertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * date: 2018/9/18
 * description: MainActivity
 **/
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.button);
        mBtn.setOnClickListener(this);
    }

    private void showDialog(){
        final TestDialogFragment dialog = new TestDialogFragment();
        dialog.show(getSupportFragmentManager(), "TestDialogFragment");
    }

    @Override
    public void onClick(View v) {
        showDialog();
    }
}
