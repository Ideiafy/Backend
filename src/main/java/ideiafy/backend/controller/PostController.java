package ideiafy.backend.controller;

import ideiafy.backend.Inputs.PostInput;
import ideiafy.backend.model.Post;
import ideiafy.backend.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostsService service;

    @QueryMapping
    public List<Post> myPosts(){
        return service.getMyPosts();
    }
    @QueryMapping
    public List<Post> posts(){
        return service.getFeed();
    }

    @MutationMapping
    public Post createPost(@Argument PostInput input){
        return service.createPost(input);
    }
    @MutationMapping
    public Post updatePost(@Argument Integer id,
                        @Argument PostInput input){
        return service.updatePost(id,input);
    }
    @MutationMapping
    public Boolean deletePost(@Argument Integer id){
        service.deletePost(id);
        return true;
    }
}
