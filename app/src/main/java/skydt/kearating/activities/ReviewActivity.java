package skydt.kearating.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import skydt.kearating.R;
import skydt.kearating.models.Course;
import skydt.kearating.models.Teacher;
import skydt.kearating.repositories.CourseDAO;
import skydt.kearating.repositories.TeacherDAO;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener
{
    private List<Course> courses;
    private List<Teacher> teachers;
    private List<RatingBar> ratingBars;
    private TextView tvReviewID;
    private TextView tvPageNum;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private Button btnSubmit;
    private ProgressBar progressBar;

    private boolean isCourse;
    private String name;
    private int progressPage;
    private int count;
    private float rating;
    private TeacherDAO teacherDAO;
    private CourseDAO courseDAO;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        sharedPreferences = getSharedPreferences("", MODE_PRIVATE);
        teacherDAO = new TeacherDAO();
        courseDAO = new CourseDAO();

        loadInterface();
        loadTeachersAndCourses();
        addIdToTextView();
        addQuestions();
    }

    private void loadTeachersAndCourses()
    {
        courses = new ArrayList<>();
        courses = courseDAO.loadCourses(sharedPreferences);

        teachers = new ArrayList<>();
        teachers = teacherDAO.loadTeachers(sharedPreferences);
    }

    private void loadInterface()
    {
        progressPage = 1;

        ratingBars = new ArrayList<>();
        ratingBars.add((RatingBar) findViewById(R.id.rb1));
        ratingBars.add((RatingBar) findViewById(R.id.rb2));
        ratingBars.add((RatingBar) findViewById(R.id.rb3));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(progressPage);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tvPageNum = findViewById(R.id.tvPageNum);
        tvReviewID = findViewById(R.id.tvReviewID);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        tvPageNum.setText(Integer.toString(progressPage) + "/" + progressBar.getMax());
    }

    private void addIdToTextView()
    {
        Bundle bundle = getIntent().getExtras();
        String key = new ArrayList<>(bundle.keySet()).get(0);
        name = bundle.getString(key);

        switch (key)
        {
            case "Courses":
                isCourse = true;

                for (Course course : courses)
                {
                    if (course.getName().equals(name))
                    {
                        tvReviewID.setText(course.getName());
                        break;
                    }
                }
                break;

            case "Teachers":
                isCourse = false;

                for (Teacher teacher : teachers)
                {
                    if (teacher.getName().equals(name))
                    {
                        tvReviewID.setText(teacher.getName());
                        break;
                    }
                }
                break;

            default:
                break;
        }
    }

    private void addQuestions()
    {
        if (isCourse)
        {
            switch (progressPage)
            {
                case 1:
                    tv1.setText(R.string.course_q1);
                    tv2.setText(R.string.course_q2);
                    tv3.setText(R.string.course_q3);
                    break;

                case 2:
                    tv1.setText(R.string.course_q4);
                    tv2.setText(R.string.course_q5);
                    tv3.setText(R.string.course_q6);
                    break;
            }
        } else if (!isCourse)
        {
            switch (progressPage)
            {
                case 1:
                    tv1.setText(R.string.teacher_q1);
                    tv2.setText(R.string.teacher_q2);
                    tv3.setText(R.string.teacher_q3);
                    break;

                case 2:
                    tv1.setText(R.string.teacher_q4);
                    tv2.setText(R.string.teacher_q5);
                    tv3.setText(R.string.teacher_q6);
                    break;
            }
        }
    }

    private void updateInterface()
    {
        addQuestions();
        progressBar.setProgress(progressPage);
        tvPageNum.setText(Integer.toString(progressPage) + "/" + progressBar.getMax());
        for (RatingBar ratingBar : ratingBars)
        {
            ratingBar.setRating(0);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (progressPage)
        {
            case 1:
                rating = 0;
                count = 0;
                for (RatingBar rb : ratingBars)
                {
                    rating += rb.getRating();
                    count += 1;
                }
                btnSubmit.setText("Submit");
                progressPage += 1;
                updateInterface();
                break;

            case 2:
                for (RatingBar rb : ratingBars)
                {
                    rating += rb.getRating();
                    count += 1;
                }

                if (isCourse)
                {
                    for (Course course : courses)
                    {
                        if (course.getName().equals(name))
                        {
                            course.addRating(rating);
                            course.setCount(count);
                            courseDAO.saveCourses(courses, sharedPreferences);
                            Intent intent = new Intent(ReviewActivity.this, SummaryActivity.class);
                            intent.putExtra("course", course);
                            intent.putExtra("rating", rating);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                } else if (!isCourse)
                {
                    for (Teacher teacher : teachers)
                    {
                        if (teacher.getName().equals(name))
                        {
                            teacher.addRating(rating);
                            teacher.setCount(count);
                            teacherDAO.saveTeachers(teachers, sharedPreferences);
                            Intent intent = new Intent(ReviewActivity.this, SummaryActivity.class);
                            intent.putExtra("teacher", teacher);
                            intent.putExtra("rating", rating);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                }
                break;

            default:
        }
    }
}
