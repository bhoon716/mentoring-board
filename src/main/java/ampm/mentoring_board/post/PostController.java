package ampm.mentoring_board.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    // 게시글 목록 페이지
    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Post post = postOpt.get();
        model.addAttribute("post", post);
        return "posts/detail";
    }

    // 게시글 작성 폼 페이지
    @GetMapping("/posts/new-form")
    public String newPostForm() {
        return "posts/new-form";
    }

    // 게시글 저장
    @PostMapping("/posts")
    public String createPost(@ModelAttribute Post post) {
        postRepository.save(post);
        return "redirect:/posts";
    }

    // 게시글 수정 폼 페이지
    @GetMapping("/posts/{postId}/edit-form")
    public String editPostForm(@PathVariable Long postId, Model model) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Post post = postOpt.get();
        model.addAttribute("post", post);
        return "posts/edit-form";
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}")
    @ResponseBody
    public String updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Post post = postOpt.get();

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postRepository.save(post);

        return "ok";
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        Post post = postOpt.get();

        postRepository.delete(post);
        return "ok";
    }
}
