package com.example.myapplicatio.z_camera;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.myapplicatio.R;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class VideoPlayBack extends Activity implements SurfaceHolder.Callback {
    MediaPlayer mp;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private TextView mTextview;
    public static final int SERVERPORT = 6775;
    public static String SERVERIP = "192.168.0.11";
    Socket clientSocket;
    private Handler handler = new Handler();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPreview = (SurfaceView) findViewById(R.id.video_view);
        mTextview = (TextView) findViewById(R.id.tv_ipcamera);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTextview.setText("Attempting to connect");
        mp = new MediaPlayer();
        Thread t = new Thread() {
            public void run() {
                try {
                    clientSocket = new Socket(SERVERIP, SERVERPORT);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTextview.setText("Connected to server");
                        }
                    });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(clientSocket);
                                pfd.getFileDescriptor().sync();
                                mp.setDataSource(pfd.getFileDescriptor());
                                pfd.close();
                                mp.setDisplay(holder);
                                mp.prepareAsync();
                                mp.start();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}