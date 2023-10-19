package com.example.service;

import com.example.model.*;
import com.example.repository.ReviewRepository;
import com.example.repository.VoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;
    @Autowired
    private RequestService service;
    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RabbitMQSenderVote rabbitMQSenderVote;

    @Autowired
    RabbitMQSenderReview rabbitMQSenderReview;
    @Autowired
    JWTService serviceJWT;
    public VoteDTO createVote(final VoteDetailsDTO resource, int reviewId, HttpServletRequest request) throws IOException {
        /*Pint statusCode = service.getStatusCodeOfReview(reviewId);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
        }
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = service.makeRequestToAutentication(jwt);
        //System.out.println(user.getRoles());
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        VoteDTO voteDTO = repository.findIfUserMadeAVoteInAReview(reviewId,user.getId());
        if (voteDTO != null){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t make another vote on this review");
        }*/
        String jwt = service.parseJwt(request);
        UserDetailsDTO user = serviceJWT.searchForUser(jwt);
        Vote vote = new Vote(resource.getVote(),reviewId,user.getId()/*2*/);//user.getId());
        //int res=service.updateReview(reviewId,resource.getVote());
        VoteUpdate update = new VoteUpdate(reviewId,resource.getVote());
        repository.save(vote);
        rabbitMQSender.send(vote);
        rabbitMQSenderVote.send(vote);

        VoteDTO reviewDTO = new VoteDTO(vote);
        return reviewDTO;
    }

    public void createVote(final VoteDetailsDTO resource, String sku, HttpServletRequest request) throws IOException {

        String jwt = service.parseJwt(request);
        UserDetailsDTO user = serviceJWT.searchForUser(jwt);
        Review review;
        if (resource.getVote()) {
            //review = new Review("Default","UNDEFINED", 5, sku, user.getId());
            review = new Review("Default", 5, sku, user.getId());
        } else {
           // review = new Review("Default","UNDEFINED", 1, sku, user.getId());
            review = new Review("Default", 5, sku, user.getId());

        }
        review.setStatus("UNDEFINED");
        rabbitMQSenderReview.sendJsonMessage(review);

        //float n = -1;
        //review = new Review("Default","UNDEFINED", n, sku, user.getId());
        /*rabbitMQSenderReview.sendJsonMessage(review);
        reviewRepository.save(review);
        Vote vote = new Vote(resource.getVote(), review.getId(), user.getId());
        repository.save(vote);
        rabbitMQSender.send(vote);
        rabbitMQSenderVote.send(vote);*/

    }


    public void createVote(final Vote vote) throws JsonProcessingException {
        /*if(repository.findById(vote.getId())== null){
            repository.save(vote);
            System.out.println(vote.getId());
            rabbitMQSender.send(vote);
            rabbitMQSenderVote.send(vote);
        }
        */
        Optional<Vote> v=repository.findById(vote.getId());
        System.out.println(vote);
        if((v == null) || (v == Optional.<Vote>empty())) {
            repository.save(vote);
            System.out.println(vote.getId());
            rabbitMQSender.send(vote);
            //rabbitMQSenderVote.send(vote);
        }
    }

    public List<VoteDTO> searchVotes(int idReview) throws IOException {
        List<VoteDTO> votes = repository.findVotesInReview(idReview);
        if (votes == null){
            votes = service.retrieveVoteFromApi(idReview);
            if (votes == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote Not Found");
            }
        }
        return votes;
    }

}
