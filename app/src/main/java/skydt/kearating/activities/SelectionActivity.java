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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
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
    private ArrayAdapter<Teacher> listTeachers;
    private ListView lvOptions;
    private ToggleButton toggleButton;
    private SharedPreferences sharedPreferences;
    private CourseDAO courseDAO;
    private TeacherDAO teacherDAO;
    private String buttonID;
    private static final String TOGGLE_BUTTON_STATE = "buttonState";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        courseDAO = new CourseDAO();
        teacherDAO = new TeacherDAO();

        loadTeachersAndCourses();
        loadInterface();
    }

    private void loadInterface()
    {
        toggleButton = findViewById(R.id.tbCourses);
        toggleButton.setOnClickListener(this);

        listCourses = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, courses);
        listTeachers = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, teachers);

        lvOptions = findViewById(R.id.lvCourseTeachers);
        lvOptions.setOnItemClickListener(this);
        lvOptions.setAdapter(listCourses);
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;
        buttonID = button.getText().toString().toLowerCase();

        switch (buttonID)
        {
            case "courses":
                lvOptions.setAdapter(listCourses);
                break;
            case "teachers":
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
        courses = new ArrayList<>();
        courses = courseDAO.loadCourses(sharedPreferences);

        teachers = new ArrayList<>();
        teachers = teacherDAO.loadTeachers(sharedPreferences);
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
        AlertDialog.Builder alertBox = new AlertDialog.Builder(SelectionActivity.this);

        alertBox.setMessage("Are you sure you want to reset everything?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        courseDAO.deleteCourses(sharedPreferences);
                        teacherDAO.deleteTeachers(sharedPreferences);
                        Toast.makeText(SelectionActivity.this, "All ratings has been reset", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(TOGGLE_BUTTON_STATE, buttonID);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        buttonID = savedInstanceState.getString(TOGGLE_BUTTON_STATE);
        if (buttonID != null && buttonID.equals("teachers"))
        {
            lvOptions.setAdapter(listTeachers);
        }
    }
}

