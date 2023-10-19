package com.example.service;


import com.example.model.UserDetailsDTO;
import com.example.model.Vote;
import com.example.model.VoteDTO;
import com.example.model.VoteDetailsDTO;
import com.example.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;
    @Autowired
    private RequestService service;
    public VoteDTO createVote(final VoteDetailsDTO resource, int reviewId, HttpServletRequest request) throws IOException {
        int statusCode = service.getStatusCodeOfReview(reviewId);
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
        }
        Vote vote = new Vote(resource.getVote(),reviewId,user.getId());
        int res=service.updateReview(reviewId,resource.getVote());

        repository.save(vote);

        VoteDTO reviewDTO = new VoteDTO(vote);
        return reviewDTO;
    }

    public void createVote(final Vote vote) throws IOException {
        if(repository.findById(vote.getId())== null) repository.save(vote);
    }

    public List<VoteDTO> searchVotes(int idReview) throws IOException {
        List<VoteDTO> votes = repository.findVotesInReview(idReview);
        if (votes == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote Not Found");
        }
        return votes;
    }

}
