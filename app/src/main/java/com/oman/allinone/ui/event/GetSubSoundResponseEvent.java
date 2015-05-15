package com.oman.allinone.ui.event;

import com.oman.allinone.dto.SubSoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class GetSubSoundResponseEvent {
    List<SubSoundCategoryDTO> results;

    public GetSubSoundResponseEvent(List<SubSoundCategoryDTO> results) {
        this.results = results;
    }

    public List<SubSoundCategoryDTO> getResults() {
        return results;
    }
}
