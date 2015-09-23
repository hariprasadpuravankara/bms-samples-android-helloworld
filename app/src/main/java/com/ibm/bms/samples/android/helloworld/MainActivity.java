package com.ibm.helloworld_android;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.security.api.AuthorizationManager;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;

public class MainActivity extends Activity implements ResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.adjustTypefaceForMainView();

        TextView buttonText = (TextView) findViewById(R.id.button_text);

        try {
            //initialize SDK with IBM Bluemix application ID and route
            //TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            //Example:
            //BMSClient.getInstance().initialize(this, "<APPLICATION_ROUTE>", "<APPLICATION_ID>");
        }
        catch (MalformedURLException mue) {
            this.setStatus(mue.getMessage() + "\nPlease ensure you are using the correct Application Route and Application Id and rebuild the app", false);
            buttonText.setClickable(false);
        }
    }

    public void pingBluemix(View view) {
        final String buttonWaitColor = "#ff00AED3";

        TextView buttonText = (TextView) findViewById(R.id.button_text);
        buttonText.setBackgroundColor(Color.parseColor(buttonWaitColor));
        buttonText.setClickable(false);

        TextView errorText = (TextView) findViewById(R.id.error_text);
        errorText.setText("");

        AuthorizationManager.getInstance().obtainAuthorizationHeader(this, this);
    }

    private void adjustTypefaceForMainView () {
        Typeface IBMFont = Typeface.createFromAsset(getAssets(), "fonts/helvetica-neue-light.ttf");

        TextView errorText = (TextView) findViewById(R.id.error_text);
        errorText.setTypeface(IBMFont);
        TextView topText = (TextView) findViewById(R.id.top_text);
        topText.setTypeface(IBMFont);
        TextView bottomText = (TextView) findViewById(R.id.bottom_text);
        bottomText.setTypeface(IBMFont);
        TextView buttonText = (TextView) findViewById(R.id.button_text);
        buttonText.setTypeface(IBMFont);
    }

    private void setStatus(final String text, boolean wasSuccessful){
        final TextView errorText = (TextView) findViewById(R.id.error_text);
        final TextView topText = (TextView) findViewById(R.id.top_text);
        final TextView bottomText = (TextView) findViewById(R.id.bottom_text);
        final TextView buttonText = (TextView) findViewById(R.id.button_text);
        final String buttonDefaultColor = "#ff1CB299";
        final String topStatus = wasSuccessful ? "Yay!" : "Bummer";
        final String bottomStatus = wasSuccessful ? "You Are Connected" : "Something Went Wrong";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonText.setBackgroundColor(Color.parseColor(buttonDefaultColor));
                buttonText.setClickable(true);
                errorText.setText(text);
                topText.setText(topStatus);
                bottomText.setText(bottomStatus);
            }
        });
    }

    @Override
    public void onSuccess(Response response) {
        setStatus("", true);
    }

    @Override
    public void onFailure(Response response, Throwable throwable, JSONObject jsonObject) {
        String errorMessage = "Authorization Process Failed With Unknown Error.";

        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            errorMessage = sw.toString();
        }

        setStatus(errorMessage, false);
    }
}
