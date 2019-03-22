package skydt.kearating.repositories;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import skydt.kearating.models.Course;

public class CourseDAO
{
    public void saveCourses(List<Course> teachers, SharedPreferences sharedPreferences)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(teachers);
        editor.putString("courses", json);
        editor.apply();
    }

    public List<Course> loadCourses(SharedPreferences sharedPreferences)
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<Course>>() {}.getType();
        List<Course> courses = gson.fromJson(json, type);

        if (courses == null)
        {
            courses = new ArrayList<>();
            courses.add(new Course("Software Construction"));
            courses.add(new Course("Software Design"));
            courses.add(new Course("Technology"));
            courses.add(new Course("IT-Organisation"));
        }
        return courses;
    }

    public void deleteCourses(SharedPreferences sharedPreferences)
    {
        sharedPreferences.edit().remove("courses").apply();
    }
}
