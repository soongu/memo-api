package com.example.memoapi.dto;

import com.example.memoapi.entity.Memo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class MemoDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request { // 생성 요청 DTO
        private String content;

        public Memo toEntity() {
            return new Memo(this.content);
        }
    }

    @Getter
    public static class Response { // 응답 DTO
        private final Long id;
        private final String content;
        private final LocalDateTime createdAt;

        private Response(Long id, String content, LocalDateTime createdAt) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
        }

        public static Response from(Memo memo) {
            return new Response(memo.getId(), memo.getContent(), memo.getCreatedAt());
        }
    }
}