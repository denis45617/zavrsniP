package com.example.mathspace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class TaskSettingView extends RelativeLayout {
    private String taskText;
    private Switch taskSwitch;
    private TextView taskTextView;
    private int defaultId;


    public String getTaskText() {
        return taskText;
    }

    public Switch getTaskSwitch() {
        return taskSwitch;
    }

    public TextView getTaskTextView() {
        return taskTextView;
    }

    public TaskSettingView(Context context, String toastText, int defaultId) {
        super(context);
        this.defaultId = defaultId;

        inflate(getContext(), R.layout.task_view, this);

        this.taskText = toastText;
        taskTextView = findViewById(R.id.textView);
        taskTextView.setText(this.taskText);

        taskSwitch = findViewById(R.id.taskSwitch);

        //dohvati spremljeno stanje
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isChecked = sharedPreferences.getBoolean("DEFAULT_SETTING:" + defaultId, true);
        taskSwitch.setChecked(isChecked);  //postavi switch na spremljeno stanje

        taskSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("DEFAULT_SETTING:" + defaultId, taskSwitch.isChecked());
                editor.apply();
            }
        });
    }
}
