package com.nazmul.polling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollDetailsDTO {
    private PollDTO pollDTO;
    private List<CommentDTO> commentDTOList;
    private Long likeCount;
    private Long commentCount;
}
