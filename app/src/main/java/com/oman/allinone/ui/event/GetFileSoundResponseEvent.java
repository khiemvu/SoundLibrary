package com.oman.allinone.ui.event;

import com.oman.allinone.dto.SoundFileDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class GetFileSoundResponseEvent {
    private List<SoundFileDTO> soundFileDTO;

    public GetFileSoundResponseEvent(List<SoundFileDTO> soundFileDTO) {
        this.soundFileDTO = soundFileDTO;
    }

    public List<SoundFileDTO> getSoundFileDTO() {
        return soundFileDTO;
    }
}
