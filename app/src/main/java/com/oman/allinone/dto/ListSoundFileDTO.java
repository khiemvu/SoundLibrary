package com.oman.allinone.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class ListSoundFileDTO implements Parcelable
{
    private int file_id;
    private String file_title;
    private int file_alias;
    private String extern_file;  //url of file audio

    public ListSoundFileDTO()
    {
    }

    public ListSoundFileDTO(Parcel in)
    {
        file_id = in.readInt();
        file_title = in.readString();
        file_alias = in.readInt();
        extern_file = in.readString();
    }


    public int getFile_id()
    {
        return file_id;
    }

    public void setFile_id(int file_id)
    {
        this.file_id = file_id;
    }

    public String getFile_title()
    {
        return file_title;
    }

    public void setFile_title(String file_title)
    {
        this.file_title = file_title;
    }

    public int getFile_alias()
    {
        return file_alias;
    }

    public void setFile_alias(int file_alias)
    {
        this.file_alias = file_alias;
    }

    public String getExtern_file()
    {
        return extern_file;
    }

    public void setExtern_file(String extern_file)
    {
        this.extern_file = extern_file;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(file_id);
        dest.writeString(file_title);
        dest.writeInt(file_alias);
        dest.writeString(extern_file);
    }

    public static final Parcelable.Creator<ListSoundFileDTO> CREATOR = new Parcelable.Creator<ListSoundFileDTO>()
    {
        public ListSoundFileDTO createFromParcel(Parcel in)
        {
            return new ListSoundFileDTO(in);
        }

        public ListSoundFileDTO[] newArray(int size)
        {
            return new ListSoundFileDTO[size];
        }
    };
}
