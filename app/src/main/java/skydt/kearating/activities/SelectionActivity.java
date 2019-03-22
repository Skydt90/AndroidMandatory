package skydt.kearating.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import skydt.kearating.R;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{

    private String[] teachers = {"Faisal", "Troels", "Cay", "Oskar"};
    private String[] courses = {"Software Construction", "Software Design", "Technology", "IT-Organisation"};
    private ArrayAdapter<String> listCourses;
    private ArrayAdapter<String> listTeachers;
    private ListView lvOptions;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        toggleButton = findViewById(R.id.tbCourses);
        lvOptions = findViewById(R.id.lvCourseTeachers);
        listCourses = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, courses);
        listTeachers = new ArrayAdapter<>(SelectionActivity.this, R.layout.list_options, teachers);

        toggleButton.setOnClickListener(this);
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
                lvOptions.setAdapter(listTeachers);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        final String value = (String) parent.getItemAtPosition(position);
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
}
