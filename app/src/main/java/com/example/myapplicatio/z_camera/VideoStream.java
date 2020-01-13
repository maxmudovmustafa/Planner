package com.example.myapplicatio.z_camera;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.SurfaceHolder;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.myapplicatio.R;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoStream extends Activity {
    // User Interface Elements
    VideoView mView;
    TextView connectionStatus;
    SurfaceHolder mHolder;
    // Video variable
    MediaRecorder recorder;
    // Networking variables
    public static String SERVERIP = "";
    public static final int SERVERPORT = 6775;
    private Handler handler = new Handler();
    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Define UI elements
        mView = (VideoView) findViewById(R.id.video_view);
        connectionStatus = (TextView) findViewById(R.id.tv_ipcamera);
        mHolder = mView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        SERVERIP = "192.168.0.11";
        // Run new thread to handle socket communications
        Thread sendVideo = new Thread(new SendVideoThread());
        sendVideo.start();
    }

    public class SendVideoThread implements Runnable {
        public void run() {
            // From Server.java
            try {
                if (SERVERIP != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            connectionStatus.setText("Listening on IP: " + SERVERIP);
                        }
                    });
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (true) {
                        //listen for incoming clients
                        Socket client = serverSocket.accept();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                connectionStatus.setText("Connected.");
                            }
                        });
                        try {
                            // Begin video communication
                            final ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(client);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    recorder = new MediaRecorder();
                                    recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                    recorder.setOutputFile(pfd.getFileDescriptor());
                                    recorder.setVideoFrameRate(20);
                                    recorder.setVideoSize(176, 144);
                                    recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
                                    recorder.setPreviewDisplay(mHolder.getSurface());
                                    try {
                                        recorder.prepare();
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    recorder.start();
                                }
                            });
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    connectionStatus.setText("Oops.Connection interrupted. Please reconnect your phones.");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            connectionStatus.setText("Couldn't detect internet connection.");
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectionStatus.setText("Error");
                    }
                });
                e.printStackTrace();
            }
            // End from server.java
        }
    }
}

