package com.moonz.memoProject.domain;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    private String username;
    private String contents;

    MemoRequestDto(String username, String contents) {
        this.username = username;
        this.contents = contents;
    }

//    public Memo update()
}
