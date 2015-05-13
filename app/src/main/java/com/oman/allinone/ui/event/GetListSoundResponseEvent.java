package com.oman.allinone.ui.event;

import com.oman.allinone.dto.ListSoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class GetListSoundResponseEvent {
    private List<ListSoundCategoryDTO> listSoundDTOs;

    public GetListSoundResponseEvent(List<ListSoundCategoryDTO> listSoundDTOs) {
        this.listSoundDTOs = listSoundDTOs;
    }

    public List<ListSoundCategoryDTO> getListSoundDTOs() {
        return listSoundDTOs;
    }
}
