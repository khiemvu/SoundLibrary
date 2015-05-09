package com.oman.allinone.event;

import com.oman.allinone.dto.ListSoundCategoryDTO;
import com.oman.allinone.dto.ListSubSoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class GetSubVideoResponseEvent
{
    private List<ListSubSoundCategoryDTO> listSoundDTOs;

    public GetSubVideoResponseEvent(List<ListSubSoundCategoryDTO> listSoundDTOs)
    {
        this.listSoundDTOs = listSoundDTOs;
    }

    public List<ListSubSoundCategoryDTO> getListSoundDTOs()
    {
        return listSoundDTOs;
    }

    public void setListSoundDTOs(List<ListSubSoundCategoryDTO> listSoundDTOs)
    {
        this.listSoundDTOs = listSoundDTOs;
    }
}
