package com.example.controller;

import com.example.model.AggregateRating;
import com.example.model.ReviewDTO;
import com.example.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Tag(name = "Reviews", description = "Endpoints for managing reviews")
@RequestMapping("/reviews")
@RestController
@Controller
public class ReviewController {
    @Autowired
    ReviewService service;

    @Operation(summary = "Get reviews approved")
    @GetMapping(value = "/{sku}")
    List<ReviewDTO> findAllReviews(@PathVariable("sku") final String sku,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
        return service.findAllApprovedReviews(sku,pageNo,pageSize);
    }

    @Operation(summary = "Gets all reviews that the user made")
    @GetMapping(value = "/user")
    List<ReviewDTO> findAllReviewsByUser(@RequestParam Integer pageNo, @RequestParam Integer pageSize, HttpServletRequest request){
        return service.findAllReviewsByUser(pageNo,pageSize,request);
    }
    @Operation(summary = "Gets the rating of the given sku")
    @GetMapping(value = "/{sku}/rating")
    public AggregateRating findAggregateRatingBySku(@PathVariable("sku") String sku){
        return service.findAllRates(sku);
    }

    @Operation(summary = "Get reviews by id")
    @GetMapping(value = "/search/{reviewId}")
    ReviewDTO findReviewById(@PathVariable("reviewId") int reviewId){
        return service.findReviewById(reviewId);
    }


    @Operation(summary = "Add vote to review")
    @GetMapping(value = "/vote/{reviewId}/{string}")
    ReviewDTO updateReviewWithVote(@PathVariable("reviewId") int reviewId,@PathVariable("string") String status){
        return service.updateReviewWithVote(reviewId,status);
    }

    @Operation(summary = "Get all reviews order by total votes ")
    @GetMapping(value = "/order/{sku}")
    List<ReviewDTO> orderReviewsReviews(@PathVariable("sku") final String sku){
        return service.orderAllReviewsByVotes(sku);
    }

}
