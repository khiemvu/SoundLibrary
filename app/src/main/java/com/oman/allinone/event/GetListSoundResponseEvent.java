package com.oman.allinone.event;

import com.oman.allinone.dto.ListSoundDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/7/2015.
 */
public class GetListSoundResponseEvent {
    private List<ListSoundDTO> listSoundDTOs;

    public GetListSoundResponseEvent(List<ListSoundDTO> listSoundDTOs) {
        this.listSoundDTOs = listSoundDTOs;
    }

    public List<ListSoundDTO> getListSoundDTOs() {
        return listSoundDTOs;
    }
}
