package com.example.jpaselfstudy.application.response;

import lombok.Getter;

@Getter
public class MemberResponse {

    private String name;

    public MemberResponse() {
    }

    public MemberResponse(String name) {
        this.name = name;
    }
}
