package ru.practicum.comments.service;

import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;

public interface CommentService {

    Comment addComment(Long eventId, Long authorId, NewCommentDto commentDto);

    Comment updateComment(Long eventId, Long authorId,  Long commId, NewCommentDto dto);

    void deleteCommentFromAdmin(Long commId);

    void deleteComment(Long authorId, Long eventId, Long commId);
}
