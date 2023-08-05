package ru.practicum.comments;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
public class CommentsController {
    private final CommentService commentService;
    private static final String USER_ID_HEADERS = "X-Sharer-User-Id";

    @PostMapping("/events/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
                                 @PathVariable long id,
                                 @RequestBody @Valid NewCommentDto dto) {
        Comment comment = commentService.addComment(id, authorId, dto);
        return CommentMapper.mapToCommentDto(comment);
    }

    @PatchMapping("/events/{id}/comments/{commId}")
    public CommentDto updateComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
                                    @PathVariable long id,
                                    @PathVariable long commId,
                                    @RequestBody @Valid NewCommentDto dto) {
        Comment comment = commentService.updateComment(id, authorId, commId, dto);
        return CommentMapper.mapToCommentDto(comment);
    }

    @DeleteMapping("/admin/comments/{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentsFromAdmin(@PathVariable Long commId) {
        commentService.deleteCommentFromAdmin(commId);
    }

    @DeleteMapping("/events/{id}/comments/{commId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestHeader(USER_ID_HEADERS) Long authorId,
                              @PathVariable long id,
                              @PathVariable long commId) {
        commentService.deleteComment(authorId, id, commId);
    }
}
