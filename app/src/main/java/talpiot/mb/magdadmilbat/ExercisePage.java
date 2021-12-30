package talpiot.mb.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;
import com.google.mediapipe.components.PermissionHelper;

import talpiot.mb.magdadmilbat.vision.VisionMaster;

public class ExercisePage extends AppCompatActivity implements View.OnClickListener {

    /**
     * Tag for logging
     */
    private static final String TAG = "EXR";

    private static final int CAMERA_ID = 1;

    private VisionMaster vision;

    private Button btnBack;
    private TextView tvRepetition, tvExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);

        btnBack = findViewById(R.id.btnBack);
        tvRepetition = findViewById(R.id.tvRepetition);
        tvExercise = findViewById(R.id.tvExercise);
        btnBack.setOnClickListener(this);
        tvExercise.setText(getIntent().getStringExtra("exercise"));

        // I'm pretty sure only one of those lines is needed, but better safe than sorry
        PermissionHelper.checkAndRequestCameraPermissions(this);
        requestPermissions(new String[]{"android.permission.CAMERA"}, CAMERA_ID);

        vision = VisionMaster.getInstance();
        vision.attachToContext(this);
        vision.attachFrame(findViewById(R.id.preview_display_layout));
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            vision.attachCamera(this);
        } else {
            PermissionHelper.checkAndRequestCameraPermissions(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}