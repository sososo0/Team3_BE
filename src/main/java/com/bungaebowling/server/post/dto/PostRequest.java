package com.bungaebowling.server.post.dto;

import com.bungaebowling.server.post.Post;
import com.bungaebowling.server.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PostRequest {

    public record PostCreateRequest (
        @NotBlank(message = "모집글 제목은 필수 입력 사항입니다.")
        @Size(max = 100, message = "모집글 제목은 최대 100자까지 입니다.")
        String title,

        @NotNull(message = "게임 예정 일시는 필수 입력 사항입니다.")
        LocalDateTime startTime,

        @NotNull(message = "모집 마감 기한은 필수 입력 사항입니다.")
        LocalDateTime dueTime,

        @NotNull(message = "모집글 내용은 필수 입력 사항입니다.")
        String content,
        Boolean isClose
    ) {
        public Post toEntity(User user) {
            return Post.builder()
                    .user(user)
                    .title(title)
                    .startTime(startTime)
                    .dueTime(dueTime)
                    .content(content)
                    .isClose(isClose)
                    .build();
        }
    }

    public record PostUpdateRequest (
            @NotBlank(message = "모집글 제목은 필수 입력 사항입니다.")
            @Size(max = 100, message = "모집글 제목은 최대 100자까지 입니다.")
            String title,

            @NotNull(message = "게임 예정 일시는 필수 입력 사항입니다.")
            LocalDateTime startTime,

            @NotNull(message = "모집 마감 기한은 필수 입력 사항입니다.")
            LocalDateTime dueTime,

            @NotNull(message = "모집글 내용은 필수 입력 사항입니다.")
            String content,
            Boolean isClose
    ) {
        public Post toEntity(User user) {
            return Post.builder()
                    .user(user)
                    .title(title)
                    .startTime(startTime)
                    .dueTime(dueTime)
                    .content(content)
                    .isClose(isClose)
                    .build();
        }
    }
}
