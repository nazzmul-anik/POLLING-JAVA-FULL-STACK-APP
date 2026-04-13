package com.nazmul.polling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollDTO {
    private Long id;
    private String question;
    private Date postedDate;
    private Date expiryDate;
    private Integer totalVoteCount = 0;
    private Boolean isExpired;
    private List<String> options;

    private Long userId;
    private String username;
    private List<OptionsDTO> optionsDTOS;
    private Boolean voted;
}
