package com.chess.rankhis.enty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*
用于展现对局列表
 */
@Table(name = "game_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String redNickName;
    private Integer redStage;
    private String blackNickName;
    private Integer blackStage;
    private Integer matchType;
    private Date created;
    //棋盘id
    @Column(name = "check_board_info_id")
    private String checkBoardInfoId;
}
