package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.oman.allinone.R;

/**
 * User: Khiemvx
 * Date: 3/17/14
 */
public class AboutActivity extends Activity implements View.OnClickListener
{
    Button btShare, btMail, btApp, btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btShare = (Button) findViewById(R.id.btShare);
        btApp = (Button) findViewById(R.id.btAppstore);
        btMail = (Button) findViewById(R.id.btEmail);
        btInfo = (Button) findViewById(R.id.btInfo);

        btInfo.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btApp.setOnClickListener(this);
        btMail.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btAppstore:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en"));
                startActivity(browserIntent);
                break;
            case R.id.btEmail:
                String subject = "اعمال الصلاة و الوضوء ";
                String message = "https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"omanimuslim2@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client"));
                break;
            case R.id.btInfo:
                Intent intent = new Intent(AboutActivity.this, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btShare:
                Intent dropbox = new Intent(Intent.ACTION_SEND);
                dropbox.setType("text/plain");
                dropbox.putExtra(Intent.EXTRA_TEXT, "اعمال الصلاة و الوضوء " +
                        "\n https://play.google.com/store/apps/developer?id=Omani%20Muslim&hl=en");
                startActivity(dropbox);
                break;
        }
    }
}
