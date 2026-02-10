package com.sls.addresssearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "address_index")
public record Address(
        @Id String id,
        String street,
        String city,
        String state,
        String zip,
        String fullAddress
) {}
