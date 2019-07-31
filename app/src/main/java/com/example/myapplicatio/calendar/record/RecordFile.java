package com.example.myapplicatio.calendar.record;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicatio.aralash.MyTextSample;
import com.example.myapplicatio.R;
import com.example.myapplicatio.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uz.greenwhite.lib.mold.MoldContentFragment;
import uz.greenwhite.lib.view_setup.ViewSetup;

public class RecordFile extends MoldContentFragment {

    private MediaRecorder recorder;
    private ViewSetup vs;
    private Boolean record = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vs = new ViewSetup(inflater.inflate(R.layout.z_record, container, false));
        return vs.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView id = vs.id(R.id.rc_audio);
        songList(id);
        vs.button(R.id.btn_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record) {
                    startRecording();
                } else {
                    stopRecording();

                }
            }
        });
    }

    public void startRecording() {
        try {
            record = false;
            vs.button(R.id.btn_record).setText("Recoring....");
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy-MM-dd-HH.mm.ss");
            String fileName = "audio_" + timeStampFormat.format(new Date())
                    + ".wav";
            File tempFile = getTempFile(fileName);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (tempFile != null) {
                recorder.setOutputFile(tempFile.getPath());
            }

            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getTempFile(String fileName) {
        try {
            String dir = "/Warloc/audio";
            File file = new File(dir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    ToastUtils.showShortToast(getContext(), "Audio file directory not created");
                }
            }
            File audio = new File(file, fileName);
            loadmp3(file.getPath());
            for (String s:listmp3)
            Log.d("$$$$$$$", s);
            FileOutputStream fos = new FileOutputStream(fileName);
            return audio;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void stopRecording() {
        vs.button(R.id.btn_record).setText("Recoring Stoped");
        record = true;
        recorder.stop();
        recorder.reset();
        recorder.release();
    }


    public void songList(RecyclerView id) {
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//        ContentResolver cr = getActivity().getContentResolver();
//        String[] projection = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.TITLE,
////                MediaStore.Audio.Media.DATA,
////                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.DURATION
//        };
//
//        Cursor cur = cr.query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                null,
//                sortOrder);
        AudioAdapter adapter = new AudioAdapter(getContext(), new ArrayList<MyTextSample>(), new AudioAdapter.ItemClickLizstener() {

            @Override
            public void onItemClick(@NotNull MyTextSample position) {
                ToastUtils.showShortToast(getContext(), position.getName());
            }
        });

//        while (cur.moveToNext()) {
//            ArrayList<MyTextSample> map = new ArrayList<>();
//
//            map.add(new MyTextSample("ID", cur.getString(0)));
//            map.add(new MyTextSample("artist", cur.getString(1)));
//            map.add(new MyTextSample("title", cur.getString(2)));
//            map.add(new MyTextSample("duration", cur.getString(4)));
//
//
//            adapter.setAdapter(map);
//        }

        id.setLayoutManager(new LinearLayoutManager(getContext()));
        id.setAdapter(adapter);
    }



    private ArrayList<String> listmp3 = new ArrayList<String>();
    String[] extensions = { "wav" };

    private void loadmp3(String YourFolderPath) {

        File file = new File(YourFolderPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        loadmp3(f.getAbsolutePath());
                    } else {
                        for (int i = 0; i < extensions.length; i++) {
                            if (f.getAbsolutePath().endsWith(extensions[i])) {
                                listmp3.add(f.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        }

    }
}
