package com.ensa.pfs.repository;

import com.ensa.pfs.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token,String> {

     @Query("{'user.id': ?0, 'expired': false, 'revoked': false}")
     List<Token> findAllValidTokensByUser(String UserId);

     Optional<Token> findByToken(String Token);

}
