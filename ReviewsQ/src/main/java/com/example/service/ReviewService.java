package com.example.service;

import com.example.model.*;
import com.example.repository.ReviewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private RequestService service;

    public void createReview(Review review) throws IOException {
        if(repository.findReviewById(review.getId())==null) repository.save(review);
    }

    public void deleteById(int review){

        if (repository.findReviewById(review)==null ){
            repository.deleteByIdReview(review);
        }
    }
    public void changeStatus(Change review) throws JsonProcessingException {
        repository.updateReview(review.getStatus(),review.getId());
    }

    public void updateReviewWithVote(Votes review) throws JsonProcessingException {
        ReviewDTO reviewDTO= repository.findReviewByIdAndApproved(review.getId());
        if (review == null){
            // throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
        }
        else {
            int totalVotes = review.getTotalVotes();
            int upVotes = review.getUpVotes();
            int downVotes = review.getDownVotes();

            repository.updateReviewWithVote(review.getId(),upVotes,downVotes,totalVotes);
        }
    }

    public List<ReviewDTO> findAllReviewsPending(Integer pageNo, Integer pageSize,HttpServletRequest request) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review = repository.findAllPendingReviews(paging);
        List<ReviewDTO> reviews = review.getContent();
        /*if (reviews.isEmpty()){
            reviews = service.
        }
        */
        return reviews;
    }
    public ReviewDTO changeStatus(int idReview, ChangeStatus resource, HttpServletRequest request) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        String updateString = resource.updateString();
        repository.updateReview(updateString,idReview);
        ReviewDTO reviewDTO = repository.findReviewById(idReview);
        return reviewDTO;
    }

    public List<ReviewDTO> findAllApprovedReviews(String sku,Integer pageNo,Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review =  repository.findAllApprovedReviews(sku,paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public List<ReviewDTO> findAllReviewsByUser(Integer pageNo,Integer pageSize, HttpServletRequest request ) {
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ReviewDTO> review= repository.findAllReviewsByUser(user.getId(),paging);
        List<ReviewDTO> reviews = review.getContent();
        return reviews;
    }

    public int getStatusCodeOfProduct(String sku){
        String urlRequest = "http://localhost:8081/products/" + sku;
        int statusCode = service.getStatusOfRequest(urlRequest);
        return statusCode;
    }

    public AggregateRating findAllRates(String sku) {
        List<ReviewDTO> review= repository.findAllAprovedReviewsBySku(sku);
        AggregateRating rating = new AggregateRating(review);
        if (rating.getSku().equals("0")){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviews Not Found");
        }
        return rating;
    }

    public ReviewDTO findReviewById(int reviewId) {
        ReviewDTO review= repository.findReviewByIdAndApproved(reviewId);
        if (review == null){
            review = service.retriveReviewFromApi(reviewId);
            if (review == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
            }
        }
        return review;
    }

    public void deleteById(int idReview,HttpServletRequest request){
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        ReviewDTO review= findReviewById(idReview);
        String urlRequest = "http://localhost:8083/votes/search/" + idReview;
        int statusCode = service.getStatusOfRequest(urlRequest);
        if (statusCode == 404 && review.getUserid() == user.getId()){
            repository.deleteByIdReview(idReview);
        }
    }

    public ReviewDTO updateReviewWithVote(int reviewId, String status) {
        ReviewDTO review= repository.findReviewByIdAndApproved(reviewId);
        if (review == null){
            review = service.retriveReviewFromApi(reviewId);
            if (review == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
            }
            return service.updateReviewFromApi(reviewId,status);
        }
        else {
            int totalVotes = review.getTotalVotes();
            int upVotes = review.getUpVotes();
            int downVotes = review.getDownVotes();
            totalVotes += 1;
            if (status == "true"){
                upVotes += 1;
            }
            else {
                downVotes += 1;
            }
            repository.updateReviewWithVote(review.getId(),upVotes,downVotes,totalVotes);
        }
        return repository.findReviewByIdAndApproved(reviewId);
    }

    public List<ReviewDTO> orderAllReviewsByVotes(String sku) {
        int statusCode = getStatusCodeOfProduct(sku);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        List<ReviewDTO> reviewDTOS = repository.orderByVotes(sku);
        return reviewDTOS;
    }
}
