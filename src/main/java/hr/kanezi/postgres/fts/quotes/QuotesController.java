package hr.kanezi.postgres.fts.quotes;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quotes")
@Value
public class QuotesController {

    QuotesService quotesService;

    @GetMapping("/{id}")
    String showQuoteDetail(@PathVariable(name = "id") Long quoteId, Model model) {

        quotesService
                .findById(quoteId)
                .map(quotesEntity -> model.addAttribute("quote", quotesEntity));

        return "quote";
    }
}
