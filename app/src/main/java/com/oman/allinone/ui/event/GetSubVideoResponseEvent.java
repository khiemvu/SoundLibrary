package com.oman.allinone.ui.event;

import com.oman.allinone.dto.SubSoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class GetSubVideoResponseEvent {
    private List<SubSoundCategoryDTO> listSoundDTOs;

    public GetSubVideoResponseEvent(List<SubSoundCategoryDTO> listSoundDTOs) {
        this.listSoundDTOs = listSoundDTOs;
    }

    public List<SubSoundCategoryDTO> getListSoundDTOs() {
        return listSoundDTOs;
    }

    public void setListSoundDTOs(List<SubSoundCategoryDTO> listSoundDTOs) {
        this.listSoundDTOs = listSoundDTOs;
    }
}
