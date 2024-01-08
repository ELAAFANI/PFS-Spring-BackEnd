package com.ensa.pfs.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "result")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Result {
    @Id
    private String id;
    private String resultClassName;
    private String imageUrl;
    @DBRef
    private User user;
}
