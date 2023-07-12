package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class comment {

    @Id
    @GeneratedValue
    @Column(name="comment_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(length=250)
    private String content;

    private LocalDateTime timestamp;
    private String createdBy; // 나중에 user_id로 바꾸어야 할 지도 모름. 일단 String으로.


}