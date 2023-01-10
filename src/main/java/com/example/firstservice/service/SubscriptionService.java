package com.example.firstservice.service;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.domain.User;
import com.example.firstservice.repos.SubscriptionRepo;
import com.example.firstservice.repos.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    SubscriptionRepo subsRepo;
    @Autowired
    UserRepo userRepo;
    private static final Logger LOG = LogManager.getLogger(SubscriptionService.class);

    public String createSubscription(String followerUserName, String followedUserName) {
        User followerUser = userRepo.findByUserName(followerUserName);
        User followedUser = userRepo.findByUserName(followedUserName);
        if (followerUser == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Follower user not found: ", followerUserName);
            }
            return String.format("User with userName: %s not found in the database", followerUser.getUserName());
        }
        if (followedUser == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Followed user not found: ", followedUserName);
            }
            return String.format("User with userName: %s not found in the database", followedUser.getUserName());
        }

        Subscription subscription = new Subscription();
        subscription.setFollower(followerUser);
        subscription.setFollowed(followedUser);
        subscription.setActive(true);
        subsRepo.save(subscription);
        return String.format("Subscription for follower user: %s and followed user: %s has been saved in the database with id %s", followerUserName, followedUserName, subscription.getId());
    }

    public String deleteSubscription(Long id) {
        if (!subsRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        subsRepo.deleteById(id);
        return String.format("User with user id: %s has been deleted from the database", id);
    }

    public List<Subscription> getSubscriptions() {
        return subsRepo.findAll();
    }

    public List<Subscription> getSubscriptionsByFollower(String followerUserName) {
        List<Subscription> subscriptions = subsRepo.findByFollower_UserName(followerUserName);
        if (CollectionUtils.isEmpty(subscriptions)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return subscriptions;
    }

    public Subscription getSubscription(Long id) {
        if (!subsRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return subsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
