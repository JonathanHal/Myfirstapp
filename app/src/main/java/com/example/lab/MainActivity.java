package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static TextToSpeech mTts = null;
    private final int MY_DATA_CHECK_CODE = 2323;
    private Button mBtnTts;
    private EditText mETxt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");
        Toast.makeText(this, getResources().getString(R.string.hello_world), Toast.LENGTH_LONG).show();
        if (mTts == null)
            checkTTS();
        // create controls
        mBtnTts = findViewById(R.id.button1);
        mETxt1 = findViewById(R.id.editText1);
// listen for button press and speak the text in the edittext control
        mBtnTts.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mETxt1.getText().toString().length() > 0 && mTts != null)
                    mTts.speak(mETxt1.getText().toString(),
                            TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void checkTTS() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
// success, create the TTS instance which automatically will call the
// TextToSpeech.OnInitListener when the TextToSpeech engine has initialized.
                mTts = new TextToSpeech(this, this);
            } else {
// missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    /**
     TextToSpeech.OnInitListener that will be called when the TextToSpeech engine has
     initialized.
     */
    @Override
    public void onInit(int status) {
// TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS)
        {
            mTts.setLanguage(Locale.US);
            String myText1 = getResources().getString(R.string.hello_android);
            String myText2 = "Welcome to the GMI2BU Android course. See you in Learn!";
            mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
            mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);
        }
        else
            Toast.makeText(this, "An error occurred with TTS init!",
                    Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy()");
        if (mTts != null)
            mTts.shutdown();
        super.onDestroy();
    }
}