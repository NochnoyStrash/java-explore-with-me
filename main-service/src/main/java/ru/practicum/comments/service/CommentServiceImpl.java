package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.events.enums.State;
import ru.practicum.events.exceptions.CommentNotFoundException;
import ru.practicum.events.exceptions.EventNotFoundException;
import ru.practicum.events.exceptions.ValidateCommentException;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final EventService eventService;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;


    @Override
    @Transactional
    public Comment addComment(Long eventId, Long authorId, NewCommentDto commentDto) {
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Событие с ID =%d  не найдено", eventId));
        }
        if (authorId == null) {
            throw new ValidateCommentException("Комментарии могут оставлять только зарегистрированные пользователи");
        }
        User author = userRepository.findById(authorId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID = %d не найден", authorId)));
        String text = commentDto.getText();
        Comment comment = CommentMapper.getComment(text, event, author);
        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long eventId, Long authorId,  Long commId, NewCommentDto dto) {
        Event event = eventService.getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Событие с ID =%d  не найдено", eventId));
        }
        if (authorId == null) {
            throw new ValidateCommentException("Комментарии могут оставлять только зарегистрированные пользователи");
        }
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new ValidateCommentException("Обновить комментарий может только его создатель");
        }
        comment.setText(dto.getText());
        comment.setCreated(LocalDateTime.now());
        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteCommentFromAdmin(Long commId) {
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        commentsRepository.deleteById(commId);
    }

    @Override
    @Transactional
    public void deleteComment(Long authorId, Long eventId, Long commId) {
        Event event = eventService.getEventById(eventId);
        Comment comment = commentsRepository.findById(commId).orElseThrow(
                () -> new CommentNotFoundException(String.format("Комментарий с ID = %d  не найден", commId)));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new ValidateCommentException("Удалить комментарий может только его создатель или администратор");
        }
        commentsRepository.deleteById(commId);
    }
}
