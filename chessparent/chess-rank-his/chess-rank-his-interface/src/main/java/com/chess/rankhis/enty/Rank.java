package com.chess.rankhis.enty;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "rank")
public class Rank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String nickName;

    //大段位比如黄金
    private Integer rankGrade;

    //黄金一
    private Integer rankGradeStage;

    //一颗星
    private Integer star;

    private Integer protectCount;

    private Integer winCount;

    private Integer transportCount;

    private Integer continusTransport;

    private Integer continusWin;

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;


}