package hr.kanezi.postgres.fts;

import hr.kanezi.postgres.fts.quotes.QuotesService;
import hr.kanezi.postgres.fts.search.FtsDocuments;
import hr.kanezi.postgres.fts.search.FtsService;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
@Value
@Log4j2
public class SearchController {

    FtsService ftsService;

    @GetMapping
    public String home() {
        return "search";
    }

    @PostMapping("/search")
    public String search(String q, RedirectAttributes attributes) {
        log.info("search for : {}", q);
        attributes.addFlashAttribute("q", q);

        List<FtsDocuments> docs = ftsService.search(q, 25L);
        attributes.addFlashAttribute("docs", docs);

        if (docs.isEmpty()) {
            attributes.addFlashAttribute("misspelling", ftsService.misspellings(q));
        }

        return "redirect:/";
    }

}
