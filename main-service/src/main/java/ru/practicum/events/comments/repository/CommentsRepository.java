package ru.practicum.events.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.comments.model.Comment;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findByEventId(Long eventId);
}
