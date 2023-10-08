package hr.kanezi.postgres.fts.quotes;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Value
public class QuotesService {

    QuotesViewRepository quotesViewRepository;

    public List<QuotesView> getQuotes() {
        return quotesViewRepository.findAll();
    }

    public Optional<QuotesView> findById(Long quoteId) {
        return quotesViewRepository.findById(quoteId);
    }

    //public List<QuotesView> findQuotes(String query) {
//        return quotesViewRepository.findSimilarQuotes(query);
//    }
//
//    public List<QuotesView> ftsFindQuotes(String query) {
//        return quotesViewRepository.ftsFindQuotes(query);
//    }
//
//    public List<QuotesView> ftsFindQuotes2(String query) {
//        return quotesViewRepository.ftsFindQuotes2(query);
//    }


}
