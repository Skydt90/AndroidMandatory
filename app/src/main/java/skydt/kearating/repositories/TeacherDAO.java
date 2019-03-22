package skydt.kearating.repositories;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import skydt.kearating.models.Teacher;


public class TeacherDAO
{
    public void saveTeachers(List<Teacher> teachers, SharedPreferences sharedPreferences)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(teachers);
        editor.putString("teachers", json);
        editor.apply();
    }

    public List<Teacher> loadTeachers(SharedPreferences sharedPreferences)
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("teachers", null);
        Type type = new TypeToken<ArrayList<Teacher>>() {}.getType();
        List<Teacher> teachers = gson.fromJson(json, type);

        if (teachers == null)
        {
            teachers = new ArrayList<>();
            teachers.add(new Teacher("Faisal"));
            teachers.add(new Teacher("Oskar"));
            teachers.add(new Teacher("Troels"));
            teachers.add(new Teacher("Cay"));
        }
        return teachers;
    }

    public void deleteTeachers(SharedPreferences sharedPreferences)
    {
        sharedPreferences.edit().remove("teachers").apply();
    }
}
