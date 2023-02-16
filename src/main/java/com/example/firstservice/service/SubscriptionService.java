package com.example.firstservice.service;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.domain.User;
import com.example.firstservice.repos.SubscriptionRepo;
import com.example.firstservice.repos.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubscriptionService {
    private SubscriptionRepo subsRepo;
    private UserRepo userRepo;

    public SubscriptionService (SubscriptionRepo subsRepo, UserRepo userRepo) {
        this.subsRepo = subsRepo;
        this.userRepo = userRepo;
    }
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
        Subscription savedSubscription = subsRepo.save(subscription);
        return String.format("Subscription for follower user: %s and followed user: %s has been saved in the database with id %s", followerUserName, followedUserName, subscription.getId());
    }

    public String createSubscription(Subscription subscription) {
        User followerUser = userRepo.findByUserName(subscription.getFollower().getUserName());
        User followedUser = userRepo.findByUserName(subscription.getFollowed().getUserName());
        if (followerUser == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Follower user not found: ", subscription.getFollower().getUserName());
            }
            return String.format("User with userName: %s not found in the database", subscription.getFollower().getUserName());
        }
        if (followedUser == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Followed user not found: ", subscription.getFollowed().getUserName());
            }
            return String.format("User with userName: %s not found in the database", subscription.getFollowed().getUserName());
        }
        subscription.setActive(true);
        Subscription savedSubscription = subsRepo.save(subscription);
        return String.format("Subscription for follower user: %s and followed user: %s has been saved in the database with id %s",
                savedSubscription.getFollower().getUserName(), savedSubscription.getFollowed().getUserName(), savedSubscription.getId());
    }

    public String deleteSubscription(Long id) {
        if (!subsRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        subsRepo.deleteById(id);
        return String.format("Subscription with subs id: %s has been deleted from the database", id);
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
