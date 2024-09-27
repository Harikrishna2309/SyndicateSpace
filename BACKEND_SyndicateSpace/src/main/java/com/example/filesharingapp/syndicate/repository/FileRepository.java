package com.example.filesharingapp.syndicate.repository;

import com.example.filesharingapp.syndicate.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    List<File> findByOwnerUsername(String username);
}
