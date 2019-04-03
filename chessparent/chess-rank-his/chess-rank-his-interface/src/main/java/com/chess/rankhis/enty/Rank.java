package com.chess.rankhis.enty;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private Integer rankGrade;

    private Integer rankGradeStage;

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