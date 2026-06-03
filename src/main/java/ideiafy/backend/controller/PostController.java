package ideiafy.backend.controller;

import ideiafy.backend.dto.PostDto;
import ideiafy.backend.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostsService service;

    @GetMapping
    public ResponseEntity getPost(){
        return ResponseEntity.ok(service.getPost());
    }
    @PostMapping
    public ResponseEntity createPost(@RequestBody PostDto dto){
        return ResponseEntity.ok(service.createPost(dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Integer id){
        service.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post was deleted");
    }
}
