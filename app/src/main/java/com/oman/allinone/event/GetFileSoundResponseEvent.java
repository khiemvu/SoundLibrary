package com.oman.allinone.event;

import com.oman.allinone.dto.ListSoundFileDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class GetFileSoundResponseEvent {
    private List<ListSoundFileDTO> listSoundFileDTO;

    public GetFileSoundResponseEvent(List<ListSoundFileDTO> listSoundFileDTO) {
        this.listSoundFileDTO = listSoundFileDTO;
    }

    public List<ListSoundFileDTO> getListSoundFileDTO() {
        return listSoundFileDTO;
    }
}
