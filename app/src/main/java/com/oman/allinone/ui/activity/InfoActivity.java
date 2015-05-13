package com.oman.allinone.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.oman.allinone.R;
import com.oman.allinone.ui.model.InfoModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: Khiemvx
 * Date: 4/11/14
 */
public class InfoActivity extends Activity {
    WebView webView;
    String value;
    //    ImageView btHome;
    InfoModel infoModel = new InfoModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        webView = (WebView) findViewById(R.id.label);

        infoModel = getObjectDTOList("about_document");
        value = infoModel.getContent();
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.setBackgroundColor(0x00000000);
        webView.loadDataWithBaseURL("file:///android_asset/", value, "text/html", "utf-8", null);
    }

    public InfoModel getObjectDTOList(String dirFromHtml) {
        InfoModel modelForScreen3DTO = new InfoModel();
        try {
            String[] fileList = getAssets().list(dirFromHtml);

            if (fileList != null) {
                for (int i = 0; i <= fileList.length - 1; i++) {
                    int j = i + 1;
                    InputStream stream = getAssets().open(dirFromHtml + "/" + j + ".htm");
                    ByteArrayOutputStream oas = new ByteArrayOutputStream();
                    copyStream(stream, oas);
                    String t = oas.toString();
                    try {
                        oas.close();
                        oas = null;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    modelForScreen3DTO.setContent(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelForScreen3DTO;
    }

    private void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
