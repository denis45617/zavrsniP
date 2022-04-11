package com.example.mathspace;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mathspace.task.Task;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        linearLayout = findViewById(R.id.linear);
        List<Task> taskList = new ArrayList<>();
        taskList = GameViewInitUtil.getAllTasks();


        for(int i = 0; i< taskList.size();++i){
            TaskSettingView taskSettingView = new TaskSettingView(this, taskList.get(i).toString(),i);
            linearLayout.addView(taskSettingView);
        }



    }
}
