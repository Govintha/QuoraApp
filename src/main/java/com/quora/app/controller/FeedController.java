package com.quora.app.controller;

import com.quora.app.dto.FeedItemDTO;
import com.quora.app.dto.FeedResponseDTO;
import com.quora.app.service.impl.CacheFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final CacheFeedService cacheFeedService;


    @GetMapping("/{userId}/feed")
    public Mono<FeedResponseDTO> getUserFeed(@PathVariable  Integer userId,
                                             @RequestParam(required = false) String cursor,
                                             @RequestParam(defaultValue = "20") int pageSize){

        log.info("Cursor TIme and Size {},{}",cursor,pageSize);
        return cacheFeedService.getUserFeed(userId,cursor,pageSize)
                .doOnSuccess(response->log.info("Able get Feed"))
                .doOnError(error->log.error("Unable to get Feed"));
    }
}
