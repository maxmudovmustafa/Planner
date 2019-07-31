package com.example.myapplicatio.voice_record;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicatio.R;
import com.example.myapplicatio.calendar.record.RecordFile;
import com.ilike.voicerecorder.utils.PathUtil;
import com.ilike.voicerecorder.widget.VoiceRecorderView;

import uz.greenwhite.lib.mold.Mold;
import uz.greenwhite.lib.mold.MoldContentFragment;
import uz.greenwhite.lib.view_setup.ViewSetup;

public class RecordActiv extends MoldContentFragment {
    private String BasePath = Environment.getExternalStorageDirectory().toString() + "/voicerecord";
    private ViewSetup vs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vs = new ViewSetup(inflater.inflate(R.layout.activity_main_3, container, false));
        return vs.view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PathUtil.getInstance().createDirs("chat", "voice", getContext());
        final VoiceRecorderView id = (VoiceRecorderView) vs.id(R.id.voice_recorder);
        vs.id(R.id.voice_recorder).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Mold.openContent(RecordFile.class);

                return id.onPressToSpeakBtnTouch(v, event, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        Log.e("voiceFilePath=", voiceFilePath + "  time = " + voiceTimeLength);


                    }
                });
            }
        });

        id.setCustomNamingFile(true, "meeee.mp3");

        /**
         *
         *
         *
         *
         */
    }
}