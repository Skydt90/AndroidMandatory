package skydt.kearating.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Course implements Parcelable
{
    private String name;
    private List<Float> ratings;
    private int count;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Course createFromParcel(Parcel in)
        {
            return new Course(in);
        }

        public Course[] newArray(int size)
        {
            return new Course[size];
        }
    };

    public Course(String name)
    {
        this.name = name;
        this.ratings = new ArrayList<>();
        this.count = 0;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.name);
        dest.writeList(this.ratings);
        dest.writeInt(this.count);
    }

    private Course(Parcel in)
    {
        this.name = in.readString();
        this.ratings = new ArrayList<>();
        in.readList(this.ratings, Float.class.getClassLoader());
        this.count = in.readInt();
    }

    public String getName()
    {
        return name;
    }

    public List<Float> getRatings()
    {
        return ratings;
    }

    public float getRating()
    {
        float sum = 0;
        for (float rating: ratings)
        {
            sum += rating;
        }
        return sum;
    }

    public void addRating(float rating)
    {
        ratings.add(rating);
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
