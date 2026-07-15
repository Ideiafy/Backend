package ideiafy.backend.service.post;

import ideiafy.backend.Repository.PostsRepository;
import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.Inputs.PostInput;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import ideiafy.backend.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    PostsRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    public List<Post> getMyPosts(){
        return repository.findByUserId(findUserId());
    }
    public List<Post> getFeed(){
        findUserId();
        return repository.findAll();
    }
    public Post createPost(PostInput input){
       User user = authenticationService.getCurrentUser();

        Post post = toEntity(input);
        post.setUser(user);
        return repository.save(post);
    }
    public void deletePost(UUID id){
        Post post = repository.findById(id).orElseThrow(()->
                new RuntimeException("Post not found"));

        if(!post.getUser().getId().equals(findUserId())){
            throw new IllegalStateException("Not allowed");
        }
        repository.delete(post);
    }
    public Post updatePost(UUID id, PostInput input){
        Post post = repository.findById(id).orElseThrow(()->
                new RuntimeException("Post not found"));
        if(!post.getUser().getId().equals(findUserId())){
            throw new IllegalStateException("Not allowed");
        }

        setPost(post,input);
        return repository.save(post);
    }

    private void setPost(Post post, PostInput input){
        post.setTitle(input.title());
        post.setDescription(input.description());
        post.setComment(input.comment());
        post.setImages(input.images());
    }
    private Post toEntity(PostInput input){
        return Post.builder()
                .title(input.title())
                .description(input.description())
                .comment(input.comment())
                .images(input.images())
                .build();
    }
    private UUID findUserId(){
        return SecurityUtils.getAuthenticationUserId();
    }

}
