package hr.kanezi.postgres.fts.search;

import lombok.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Value
public class FtsService {

    FtsDocumentRepository ftsDocumentRepository;

    FtsWordsViewRepository ftsWordsViewRepository;

    public List<FtsDocuments> search(String query, Long limit){
        return StringUtils.isEmptyOrWhitespace(query)
                ? Collections.emptyList()
                : ftsDocumentRepository.ftsSearch(query, limit);
    }

    public List<FtsWordView> misspellings(String query) {
        return ftsWordsViewRepository.findSimilar(query);
    }
}
