package hr.kanezi.postgres.fts.movies;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/movies")
@Value
public class MoviesController {

    MoviesService moviesService;

    @GetMapping("/{id}")
    public String movieDetail(@PathVariable(name = "id") Long id, Model model) {
        moviesService
                .findMovieInfo(id)
                .map(moviesView -> model.addAttribute("movie", moviesView));

        return "movie";
    }

}
