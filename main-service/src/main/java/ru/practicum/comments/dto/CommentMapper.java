package ru.practicum.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment getComment(String text, Event event, User author) {
        return Comment.builder()
                .text(text)
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();

    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }
}
