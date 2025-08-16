package com.quora.app.repository;

import com.quora.app.entity.Likes;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends ReactiveMongoRepository<Likes,String> {
}
