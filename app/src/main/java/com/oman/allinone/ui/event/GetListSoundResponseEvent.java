package com.oman.allinone.ui.event;

import com.oman.allinone.dto.SoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class GetListSoundResponseEvent {
    private List<SoundCategoryDTO> listSoundDTOs;

    public GetListSoundResponseEvent(List<SoundCategoryDTO> listSoundDTOs) {
        this.listSoundDTOs = listSoundDTOs;
    }

    public List<SoundCategoryDTO> getListSoundDTOs() {
        return listSoundDTOs;
    }
}
