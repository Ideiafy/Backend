package ideiafy.backend.service;

import ideiafy.backend.Repository.PostsRepository;
import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.SecurityUtils;
import ideiafy.backend.dto.PostDto;
import ideiafy.backend.dto.PostResponseDto;
import ideiafy.backend.dto.UserResponseDto;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {
    @Autowired
    PostsRepository repository;

    @Autowired
    UserRepository userRepository;

    public List<PostResponseDto> getMyPosts(){
        Integer userId = SecurityUtils.getAuthenticationUserId();

        return repository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public List<PostResponseDto> getFeed(){
        Integer userId = SecurityUtils.getAuthenticationUserId();

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public PostResponseDto createPost(PostDto dto){
        Integer userId = SecurityUtils.getAuthenticationUserId();

        User user = userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User not found"));

        Post post = toEntity(dto);
        post.setUser(user);
        Post saved = repository.save(post);

        return toResponse(saved);
    }
    public void deletePost(Integer id){
        Integer userid = SecurityUtils.getAuthenticationUserId();

        Post post = repository.findById(id).orElseThrow(()->
                new RuntimeException("Post not found"));

        if(!post.getUser().getId().equals(userid)){
            throw new RuntimeException("Not allowed");
        }
        repository.delete(post);
    }
    public PostResponseDto putPost(Integer id,PostDto dto){
        Integer userId = SecurityUtils.getAuthenticationUserId();

        Post post = repository.findById(id).orElseThrow(()->
                new RuntimeException("Post not found"));
        if(!post.getUser().getId().equals(userId)){
            throw new RuntimeException("Not allowed");
        }

        UpdatePost(post,dto);
        Post saved = repository.save(post);
        return toResponse(saved);
    }

    private void UpdatePost(Post post,PostDto dto){
        post.setTitle(dto.title());
        post.setDescription(dto.description());
        post.setComment(dto.comment());
        post.setImages(dto.images());
    }
    private Post toEntity(PostDto dto){
        return Post.builder()
                .title(dto.title())
                .description(dto.description())
                .comment(dto.comment())
                .images(dto.images())
                .build();
    }
    private PostResponseDto toResponse(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getComment(),
                post.getImages(),
                new UserResponseDto(
                        post.getUser().getId(),
                        post.getUser().getName(),
                        post.getUser().getEmail()
                )
        );
    }

}
