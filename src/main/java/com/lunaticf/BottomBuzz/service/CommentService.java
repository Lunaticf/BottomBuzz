package com.lunaticf.BottomBuzz.service;


import com.lunaticf.BottomBuzz.dao.CommentDAO;
import com.lunaticf.BottomBuzz.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.getCommentsByNewsId(entityId, entityType);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public int deleteComment(int commentId) {
        return commentDAO.updateCommentStatus(1, commentId);
    }


}
