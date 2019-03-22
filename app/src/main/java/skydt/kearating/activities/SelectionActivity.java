package skydt.kearating.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import skydt.kearating.R;
import skydt.kearating.models.Course;
import skydt.kearating.models.Teacher;
import skydt.kearating.repositories.CourseDAO;
import skydt.kearating.repositories.TeacherDAO;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private List<Teacher> teachers;
    private List<Course> courses;
    private ArrayAdapter<Course> listCourses;
    private ListView lvOptions;
    private ToggleButton toggleButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        sharedPreferences = getSharedPreferences("", MODE_PRIVATE);

        loadTeachersAndCourses();
        loadInterface();
    }

    private void loadInterface()
    {
        toggleButton = findViewById(R.id.tbCourses);
        toggleButton.setOnClickListener(this);

        listCourses = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, courses);
        lvOptions = findViewById(R.id.lvCourseTeachers);
        lvOptions.setAdapter(listCourses);
        lvOptions.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;
        String id = button.getText().toString().toLowerCase();

        switch (id)
        {
            case "courses":
                lvOptions.setAdapter(listCourses);
                break;
            case "teachers":
                ArrayAdapter<Teacher> listTeachers = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, teachers);
                lvOptions.setAdapter(listTeachers);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        final String value;
        Object o = parent.getItemAtPosition(position);
        if (o instanceof Teacher)
        {
            Teacher teacher = (Teacher) o;
            value = teacher.getName();
        } else
        {
            Course course = (Course) o;
            value = course.getName();
        }

        AlertDialog.Builder alertBox = new AlertDialog.Builder(SelectionActivity.this);

        alertBox.setMessage("You have selected " + value + " is that correct?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(SelectionActivity.this, ReviewActivity.class);
                        String message = toggleButton.getText().toString();
                        intent.putExtra(message, value);
                        startActivity(intent);
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
        alertDialog.setTitle("Confirm Selection");
        alertDialog.show();
    }

    private void loadTeachersAndCourses()
    {
        CourseDAO courseDAO = new CourseDAO();
        courses = new ArrayList<>();
        courses = courseDAO.loadCourses(sharedPreferences);

        TeacherDAO teacherDAO = new TeacherDAO();
        teachers = new ArrayList<>();
        teachers = teacherDAO.loadTeachers(sharedPreferences);
    }
}

