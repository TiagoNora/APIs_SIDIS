package com.example.controller;

import com.example.model.VoteDTO;
import com.example.model.VoteDetailsDTO;
import com.example.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Tag(name = "Votes", description = "Endpoints for managing votes")
@RequestMapping("/votes")
@RestController
public class VoteController {
    @Autowired
    VoteService service;
    @Operation(summary = "Make a vote in a review")
    @PostMapping(value = "/{idReview}")
    @ResponseStatus(HttpStatus.CREATED)
    public VoteDTO create(@RequestBody final VoteDetailsDTO resource, @PathVariable("idReview") final int idReview, HttpServletRequest request) throws IOException {
        return service.createVote(resource,idReview,request);
    }

    @Operation(summary = "Make a vote in a product")
    @PostMapping(value = "/sku/{sku}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createNoReview(@RequestBody final VoteDetailsDTO resource, @PathVariable("sku") final String sku, HttpServletRequest request) throws IOException {
         service.createVote(resource,sku,request);
    }

}
