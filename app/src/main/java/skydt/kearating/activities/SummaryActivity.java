package skydt.kearating.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import skydt.kearating.R;
import skydt.kearating.models.Course;
import skydt.kearating.models.Teacher;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView tvResult1;
    private TextView tvResult2;
    private TextView tvResult3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Button btnBackToStart = findViewById(R.id.btnBackToStart);
        btnBackToStart.setOnClickListener(this);
        tvResult1 = findViewById(R.id.tvResult1);
        tvResult2 = findViewById(R.id.tvResult2);
        tvResult3 = findViewById(R.id.tvResult3);

        updateInterface();
    }

    private void updateInterface()
    {
        float average;
        Bundle bundle = getIntent().getExtras();
        String key = new ArrayList<>(bundle.keySet()).get(0);
        float rating = bundle.getFloat("rating");
        tvResult2.setText(String.format(Locale.getDefault(), "%.2f", rating));

        switch (key)
        {
            case "teacher":
                Teacher teacher = bundle.getParcelable(key);
                tvResult1.setText(teacher.getName());
                average = teacher.getRating() / teacher.getCount();
                tvResult3.setText(String.format(Locale.getDefault(), "%.2f", average));
                break;

            case "course":
                Course course = bundle.getParcelable(key);
                tvResult1.setText(course.getName());
                average = course.getRating() / course.getCount();
                tvResult3.setText(String.format(Locale.getDefault(), "%.2f", average));
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        /*
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "christian_skydt@hotmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "hej");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "tester igen");
        startActivity(emailIntent); */

        Intent intent = new Intent(SummaryActivity.this, SelectionActivity.class);
        startActivity(intent);
    }
}
