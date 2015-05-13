package com.oman.allinone.ui.event;

import com.oman.allinone.dto.ListSubSoundCategoryDTO;

import java.util.List;

/**
 * Created by Khiemvx on 5/8/2015.
 */
public class GetSubSoundResponseEvent {
    List<ListSubSoundCategoryDTO> results;

    public GetSubSoundResponseEvent(List<ListSubSoundCategoryDTO> results) {
        this.results = results;
    }

    public List<ListSubSoundCategoryDTO> getResults() {
        return results;
    }
}
