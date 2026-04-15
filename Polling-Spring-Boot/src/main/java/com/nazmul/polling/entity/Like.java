package com.nazmul.polling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazmul.polling.dto.LikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Poll poll;

    public LikeDTO getLikeDTO(){
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setId(id);
        likeDTO.setUserId(user.getId());
        likeDTO.setPollId(poll.getId());
        likeDTO.setUsername(user.getFirstName()+" "+user.getLastName());
        return likeDTO;
    }
}
