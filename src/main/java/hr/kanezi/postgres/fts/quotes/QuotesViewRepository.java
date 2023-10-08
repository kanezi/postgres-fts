package hr.kanezi.postgres.fts.quotes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotesViewRepository extends JpaRepository<QuotesView, Long> {

    @Query(value = "select q.* from quotes q order by  q.author||' '||q.quote <-> :q limit 10", nativeQuery = true)
    List<QuotesView> findSimilarQuotes(@Param("q") String q);
//
//    @Query(value =
//    """
//    select   q.id
//    ,        ts_headline(author, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') author
//    ,        ts_headline(quote, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') quote
//    ,        fts
//    ,        greatest(ts_rank_cd(fts, websearch_to_tsquery(:q)), ts_rank_cd(fts, websearch_to_tsquery('simple', :q))) ranking
//    from     quotes q
//    where    fts@@ websearch_to_tsquery(:q) or fts@@ websearch_to_tsquery('simple', :q)
//    union all
//    select   q.id
//    ,        ts_headline(author, to_tsquery(:q||':*'), 'startSel=<em> stopSel=</em>') author
//    ,        ts_headline(quote, to_tsquery(:q||':*'), 'startSel=<em> stopSel=</em>') quote
//    ,        fts
//    ,        ts_rank_cd(fts, to_tsquery(:q||':*')) ranking
//    from     quotes q
//    where    fts@@ to_tsquery(:q||':*')
//    order by ranking desc limit 25
//            """, nativeQuery = true)
//    List<QuotesView> ftsFindQuotes(@Param("q") String q);
//
//    @Query(value =
//    """
//    select   q.id
//    ,        ts_headline(author, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') author
//    ,        ts_headline(quote, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') quote
//    ,        fts
//    ,        greatest(ts_rank_cd(fts, websearch_to_tsquery(:q)), ts_rank_cd(fts, websearch_to_tsquery('simple', :q))) ranking
//    from     quotes q
//    where    fts@@ websearch_to_tsquery(:q) or fts@@ websearch_to_tsquery('simple', :q)
//    union all
//    -- match begining of the word id query does not contain string
//    select   q.id
//    ,        ts_headline(author, to_tsquery(regexp_substr(:q,  '[[:word:]]*')||':*'), 'startSel=<em> stopSel=</em>') author
//    ,        ts_headline(quote, to_tsquery(regexp_substr(:q,  '[[:word:]]*')||':*'), 'startSel=<em> stopSel=</em>') quote
//    ,        fts
//    ,        ts_rank_cd(fts, to_tsquery(regexp_substr(:q,  '[[:word:]]*')||':*')) ranking
//    from     quotes q
//    where    fts@@ to_tsquery(regexp_substr(:q,  '[[:word:]]*') ||':*')
//    and      :q !~* '[[:word:]]*'
//    order by ranking desc limit 25
//    """, nativeQuery = true)
//    List<QuotesView> ftsFindQuotes2(@Param("q") String q);
//


}
