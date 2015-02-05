package com.oman.allinone.temp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.oman.allinone.R;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class DownloadAndPlayVideo extends Activity
{

    public static final int progress_bar_type = 0;
    Button btDownload;
    String file_url =
            "https://dl.dropbox.com/s/ev2qqk4qzbuw7mv/Video%2B%20D%C3%A2n%20m%E1%BA%A1ng%20ph%C3%A1t%20%E2%80%9Cs%E1%BB%91t%E2%80%9D%20v%E1%BB%9Bi%20c%C3%B4%20b%C3%A9%20VN%20h%C3%A1t%20%E2%80%9CThe%20Show%E2%80%9D%20hay%20h%C6%A1n%20c%E1%BA%A3%20Lenka%2B%2B%20Bu%C3%B4nChuy%E1%BB%87n%20InFo%2B%2B%20thu%20gian%2B%2B%20giai%20tri%2B%2B%20xem%20phim%2B%2B%20ngh.mp4";
    VideoView vidView;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_example);

        vidView = (VideoView) findViewById(R.id.myVideo);
        btDownload = (Button) findViewById(R.id.btDownload);


        btDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");
                // Check if the Music file already exists
                if (file.exists())
                {
                    playVideo(vidView);
                }
                else
                {
                    new DownloadMusicfromInternet().execute(file_url);
                }
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Downloading Mp4 file. Please wait...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    private void playVideo(final VideoView vidView)
    {
        MediaController vidControl = new MediaController(DownloadAndPlayVideo.this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        Uri vidUri = Uri.parse("file:///sdcard/video.mp4");
        vidView.setVideoURI(vidUri);
        vidView.start();
//        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
//        {
//            // Close the progress bar and play the video
//            public void onPrepared(MediaPlayer mp)
//            {
//                vidView.seekTo(3);
//            }
//        });
    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String>
    {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url)
        {
            int count;
            try
            {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress)
        {
            // Set progress percentage
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url)
        {
            // Dismiss the dialog after the Music file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            playVideo(vidView);
        }
    }

}
