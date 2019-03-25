package skydt.kearating.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import skydt.kearating.R;
import skydt.kearating.models.Course;
import skydt.kearating.models.Teacher;
import skydt.kearating.repositories.CourseDAO;
import skydt.kearating.repositories.TeacherDAO;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tvResult1;
    private TextView tvResult2;
    private TextView tvResult3;
    private TextView tvResult4;
    private String ratingChar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        sharedPreferences = getSharedPreferences("", MODE_PRIVATE);

        loadInterface();

        try
        {
            updateInterface();
        } catch (NullPointerException npe)
        {
            Toast.makeText(SummaryActivity.this, "Couldn't retrieve rated course or teacher", Toast.LENGTH_LONG).show();
            npe.printStackTrace();
        }
    }

    private void loadInterface()
    {
        Button btnBackToStart = findViewById(R.id.btnBackToStart);
        btnBackToStart.setOnClickListener(this);
        tvResult1 = findViewById(R.id.tvResult1);
        tvResult2 = findViewById(R.id.tvResult2);
        tvResult3 = findViewById(R.id.tvResult3);
        tvResult4 = findViewById(R.id.tvResult4);
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
                calculateRatingValue(average);
                tvResult3.setText(String.format(Locale.getDefault(), "%.2f", average));
                tvResult4.setText(ratingChar);
                break;

            case "course":
                Course course = bundle.getParcelable(key);
                tvResult1.setText(course.getName());
                average = course.getRating() / course.getCount();
                calculateRatingValue(average);
                tvResult3.setText(String.format(Locale.getDefault(), "%.2f", average));
                tvResult4.setText(ratingChar);
                break;
        }
    }

    private void calculateRatingValue(float average)
    {
        if (average < 5.00)
        {
            ratingChar = "F";
        } else if (average >= 5.00 && average < 6.00)
        {
            ratingChar = "E";
        } else if (average >= 6.00 && average < 7.00)
        {
            ratingChar = "D";
        } else if (average >= 7.00 && average < 8.00)
        {
            ratingChar = "C";
        } else if (average >= 8.00 && average < 9.00)
        {
            ratingChar = "B";
        } else
        {
            ratingChar = "A";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.reset_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(SummaryActivity.this);

        alertBox.setMessage("Are you sure you want to reset everything?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CourseDAO courseDAO = new CourseDAO();
                        courseDAO.deleteCourses(sharedPreferences);
                        TeacherDAO teacherDAO = new TeacherDAO();
                        teacherDAO.deleteTeachers(sharedPreferences);
                        Toast.makeText(SummaryActivity.this, "All ratings has been reset", Toast.LENGTH_LONG).show();
                        resetInterface();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertBox.create();
        alertDialog.setTitle("WARNING!");
        alertDialog.show();

        return true;
    }

    private void resetInterface()
    {
        tvResult1.setText("");
        tvResult2.setText("");
        tvResult3.setText("");
        tvResult4.setText("");
    }
}
