package com.sls.addresssearch.controller;

import com.sls.addresssearch.model.Address;
import com.sls.addresssearch.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService service;
    public AddressController(AddressService service)
    {
        this.service = service;
    }

    @GetMapping("/search")
    public List<Address> search(@RequestParam String q) {

        return service.search(q);
    }
}
