package com.sls.addresssearch.service;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.sls.addresssearch.model.Address;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {

    private final ElasticsearchOperations operations;

    public AddressService(ElasticsearchOperations operations)
    {
        this.operations = operations;
    }

    public List<Address> search(String q) {

        Query query = MultiMatchQuery.of(m -> m
                .query(q)
                .fields("street", "city", "fullAddress")
                )._toQuery();

        var searchQuery = NativeQuery.builder()// searchQuery is a search request object.It contains
                                      // instructions, not data.
                .withQuery(query)
                .withMaxResults(5)
                .build();

        return operations.search(searchQuery, Address.class)
                .stream()                    // Stream<SearchHit<Address>>
                .map(hit -> hit.getContent())// Extract Address
                .toList();                   // Convert to List


//        We pass Address.class so Spring knows to convert the JSON results from Elasticsearch into
//        Address objects it acts like a blueprint telling Spring what type of Java object to
//        create for each search result.
       // Streams:
//        1) operations.search(searchQuery, Address.class)
//        This sends your search request to Elasticsearch and gets results back.
//        But the results don’t come back as Address directly — they come wrapped inside a
//        container called SearchHit (because Elasticsearch also gives extra info like score,
//        index name, id, etc.). So after this line you basically have: a bunch of SearchHit objects,
//        each one containing an Address inside.
//
//        2) .stream()
//        Now you have a collection of many SearchHit results. stream() just means:
//        “I want to go through these results one by one in a modern way.
//        ” It’s like starting a loop, but instead of writing for(...), you use stream operations.
//
//        3) .map(hit -> hit.getContent())
//        Each item is a SearchHit<Address>.
//        Inside it, the real address is stored as “content”. So this line means: for each hit,
//        take only the Address inside it. After map, you no longer have
//        SearchHit objects — you now have Address objects.
//
//        4) .toList()
//        After extracting all Address objects, toList() just collects them into a
//        final List<Address> so your controller can return it as JSON.
//
//        What is a SearchHit?
//        When Elasticsearch returns results, each result is not just the data.
//        It also includes extra info like:
//        document id
//        score (how relevant it is)
//        index name
//        So Spring wraps each result into a SearchHit object.
//                Elasticsearch might return something like:
//
//        {
//            "_index": "address_index",
//                "_id": "1",
//                "_score": 1.23,
//                "_source": {
//            "street": "Main Street",
//                    "city": "Dallas",
//                    "state": "TX",
//                    "zip": "75201",
//                    "fullAddress": "123 Main Street, Dallas, TX 75201"
//        }
//        }
//
//        In Java this becomes:
//        SearchHit<Address> hit;
//        Example in code
//        for (SearchHit<Address> hit : hits) {
//            Address address = hit.getContent();
//            System.out.println(address.street());
//        }
//        So
//                SearchHit = wrapper
//        Inside it = your real Address data.
//                That’s why we use:
//.map(hit -> hit.getContent())
//        to extract only the Address part.
    }
}


//ElasticsearchOperations
// ElasticsearchOperations is a Spring-provided interface that lets your service send
// search requests to Elasticsearch.

//Query is a class?
//Yes Query is a class (actually a type/interface) provided by the Elasticsearch Java client library.
//It comes from: co.elastic.clients.elasticsearch._types.query_dsl.Query
//You didn’t create it — the Elasticsearch library already defines it.

//What does it represent?
//Query represents the final search request structure that Elasticsearch understands.

//Think of it like:
//It can represent: match query, multi match, range query, bool query
//All those different query types are wrapped inside this common Query type.

//Old Java style:
//public void doSomething(Builder m) {  m.query(q);  }
//Lambda style: m -> m.query(q)
//Same meaning, shorter.

//Below is the Lamda structure
//Lambda Structure Parameter ->action
//So here: m -> m.query(q).fields(...)
//“Take an object m and run these methods on it.”

//What is m here?
//m is a builder object that Elasticsearch Java client library gives you.
//The Elasticsearch Java client library gives the builder object.
//You don’t create it — the library gives it.

//🔌 Elastic search Client
//A client is a part of the library that actually talks to an external system (Elasticsearch server).
//In your project:
//ElasticsearchOperations
//MultiMatchQuery
//Query
//are part of the Elasticsearch client API.

//📦 Library
//A library is just a collection of code files (classes, methods) that help you do something.
//You add it to your project using Maven dependency.

//What is m?
//m is a builder object used only to prepare a search query.
//It does not talk to Elasticsearch server.
//It just helps you fill details like:
//
//what word to search, which fields to search
//After you finish setting things on m, the library turns it into a Query object (a search instruction).
//So: m = temporary helper to build the search instruction.
//

//What is a Builder?
//A builder is a helper object used to construct something complex step by step.
//Instead of giving everything at once, you build it gradually.

//MultiMatchQuery.of(m -> m.query(q).fields(...))
//m is that helper builder.
//It helps you set:
//search word → .query(q)
//fields to search → .fields(...)
//After you finish, the library uses that builder to create the final Query object.


//What is the client?
//The client is the part that actually sends the search to Elasticsearch server.
//In your code, this line uses the client:
//        operations.search(searchQuery, Address.class);
//Here ElasticsearchOperations is the client bridge.
//It sends the Query to Elasticsearch and gets JSON results back.

