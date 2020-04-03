package com.blkcaphax.remotepc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.blkcaphax.remotepc.socket.RpClient;

public class MainActivity extends AppCompatActivity {
    public Terminal terminal;
    private RelativeLayout editorContainer;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    public static MainActivity mainActivity= null;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handler = new Handler();
        mainActivity = this;
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        fragmentManager = getSupportFragmentManager();
        terminal = new Terminal(this);
        editorContainer = (RelativeLayout) findViewById(R.id.editorcontainer);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        terminal.setLayoutParams(layoutParams);
        terminal.setBackgroundColor(Color.BLACK);
        terminal.setTextColor(Color.WHITE);
        terminal.setGravity(Gravity.TOP);
        editorContainer.addView(terminal);

        RpClient rpClient = new RpClient("192.168.43.162",8090);
        rpClient.connect();
        terminal.setRpClient(rpClient);
    }

    public void addFragment(){
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        ViewFragment viewFragment = new ViewFragment();
        fragmentTransaction.add(R.id.frame,viewFragment);
        fragmentTransaction.commit();
    }

    public void updateUi(final String str){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.terminal.append(str+"\n");
            }
        });
    }
}
