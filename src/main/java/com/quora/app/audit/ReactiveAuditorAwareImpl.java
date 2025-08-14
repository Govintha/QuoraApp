package com.quora.app.audit;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveAuditorAwareImpl implements ReactiveAuditorAware<String> {
    @Override
    public Mono<String> getCurrentAuditor() {

        return Mono.just("Test User");
    }
}
