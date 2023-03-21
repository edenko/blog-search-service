package com.test.search.service;

import com.test.search.entity.Keywords;
import com.test.search.repository.KeywordsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordsService {

    private final KeywordsRepository keywordsRepository;
    private final AtomicLong atomic = new AtomicLong(0);

    public synchronized void countUpForKeyword(String keyword) {
        keywordsRepository.findTopByKeyword(keyword).ifPresentOrElse(i -> {
            i.updateKeyWord(i.getKeyword(), atomic.incrementAndGet());
        }, () -> {
            Keywords builder = Keywords.builder()
                    .keyword(keyword)
                    .count(atomic.incrementAndGet())
                    .build();
            keywordsRepository.save(builder);
        });
    }

//    @Transactional
//    public synchronized void countUpForKeyword(String keyword) {
//        Optional<Keywords> existingKeyword = keywordsRepository.findByKeyword(keyword);
//
//        if (existingKeyword != null) {
//            existingKeyword.setCount(existingKeyword.getCount() + 1);
//        } else {
//            Keywords newKeyword = Keywords.builder()
//                    .keyword(keyword)
//                    .count(atomic.getAndIncrement())
//                    .build();
//            keywordsRepository.save(newKeyword);
//        }
//    }
}
