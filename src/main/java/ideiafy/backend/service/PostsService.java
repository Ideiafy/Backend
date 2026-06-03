package ideiafy.backend.service;

import ideiafy.backend.Repository.PostsRepository;
import ideiafy.backend.Repository.UserRepository;
import ideiafy.backend.Security.JwtUtil;
import ideiafy.backend.dto.PostDto;
import ideiafy.backend.dto.PostResponseDto;
import ideiafy.backend.dto.UserResponseDto;
import ideiafy.backend.model.Post;
import ideiafy.backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {
    @Autowired
    PostsRepository repository;

    @Autowired
    UserRepository userRepository;

    public List<PostResponseDto> getPost(){
        Integer userId = getAuthenticationUserId();
        return repository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public PostResponseDto createPost(PostDto dto){
        Integer userId = getAuthenticationUserId();

        User user = userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User not found"));

        Post post = toEntity(dto);
        post.setUser(user);
        Post saved = repository.save(post);

        return toResponse(saved);
    }
    public void deletePost(Integer id){
        Integer userid = getAuthenticationUserId();

        Post post = repository.findById(id).orElseThrow(()->
                new RuntimeException("Post not found"));

        if(!post.getUser().getId().equals(userid)){
            throw new RuntimeException("Not allowed");
        }
        repository.delete(post);
    }

    private Post toEntity(PostDto dto){
        return Post.builder()
                .title(dto.title())
                .description(dto.description())
                .comment(dto.comment())
                .images(dto.images())
                .build();
    }
    private Integer getAuthenticationUserId(){
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(auth == null){
            throw new RuntimeException("Invalid token");
        }
        return (Integer) auth.getPrincipal();
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
