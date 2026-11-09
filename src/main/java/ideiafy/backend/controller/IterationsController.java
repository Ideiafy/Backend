package ideiafy.backend.controller;

import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.model.iterations.Comment;
import ideiafy.backend.service.iterations.comment.CommentService;
import ideiafy.backend.service.iterations.follow.FollowService;
import ideiafy.backend.service.iterations.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class IterationsController {
    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @MutationMapping
    public boolean toggleFollow(@Argument UUID followingId){
        return followService.toggleFollow(followingId);
    }
    @MutationMapping
    public boolean toggleLike(@Argument UUID postId){
        return likeService.toggleLike(postId);
    }
    @MutationMapping
    public Comment createComment(@Argument UUID postId, @Argument String content){
        return commentService.createComment(postId,content);
    }
    @MutationMapping
    public boolean deleteComment(@Argument UUID id){
        return commentService.deleteComment(id);
    }

    @QueryMapping
    public long countByFollowing(@Argument UUID followingId){
        return followService.countByFollowing(followingId);
    }
    @QueryMapping
    public long countByFollower(@Argument UUID followerId){
        return followService.countByFollower(followerId);
    }
    @QueryMapping
    public List<Comment> viewComments(@Argument UUID postId){
        return commentService.viewComments(postId);
    }
    @QueryMapping
    public long countLikeByPost(@Argument UUID postId){
        return likeService.countByPost(postId);
    }


}
