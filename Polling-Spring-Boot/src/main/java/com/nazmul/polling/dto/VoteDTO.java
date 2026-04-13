package com.nazmul.polling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long id;
    private Long optionId;
    private Long pollId;
    private Long postedBy;
}
