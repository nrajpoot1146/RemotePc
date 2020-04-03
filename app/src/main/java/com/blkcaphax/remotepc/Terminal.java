package com.blkcaphax.remotepc;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.blkcaphax.remotepc.socket.RpClient;

import java.util.logging.Handler;

@SuppressLint("AppCompatCustomView")
public class Terminal extends EditText {
    private String TAG;
    private MainActivity context;
    private RpClient rpClient;
    Terminal(MainActivity context) {
        super(context);
        this.context = context;
        this.TAG = this.getClass().toString();
        //this.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/clacon.ttf"));
        this.append(">>");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String str = this.getText().toString();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DEL:
                if (str.substring(str.length() - 2).equals(">>")) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                this.handleCommant();
                this.append("\n>>");
                return true;
        }
        if (this.getSelectionStart() <= str.lastIndexOf(">>")) {
            this.setSelection(str.length());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        //outAttrs.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        outAttrs.imeOptions |= EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        BaseInputConnection fic = new BaseInputConnection(this,false);
        return fic;//super.onCreateInputConnection(outAttrs);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public boolean handleCommant(){
        String str = this.getText().toString();
        String[] lines = str.split("\n");
        String lastLine = lines[this.getLineCount()-1];
        String commandString = lastLine.substring(lastLine.indexOf(">>")+2);
        this.rpClient.send(commandString);
        return true;
    }

    public void setRpClient(RpClient rpClient) {
        this.rpClient = rpClient;
    }

}