package skydt.kearating.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Teacher implements Parcelable
{
    private String name;
    private List<Float> ratings;
    private int count;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Teacher createFromParcel(Parcel in)
        {
            return new Teacher(in);
        }

        public Teacher[] newArray(int size)
        {
            return new Teacher[size];
        }
    };

    public Teacher(String name)
    {
        this.name = name;
        this.ratings = new ArrayList<>();
        this.count = 0;
    }

    private Teacher(Parcel in)
    {
        this.name = in.readString();
        this.ratings = new ArrayList<>();
        in.readList(this.ratings, Float.class.getClassLoader());
        this.count = in.readInt();
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

    public String getName()
    {
        return name;
    }

    public List<Float> getRatings()
    {
        return ratings;
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
