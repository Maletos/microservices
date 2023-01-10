package com.example.firstservice.controller;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("subscribe")
    public String createUser(@RequestParam String followerUserName, @RequestParam String followedUserName) {
        return subscriptionService.createSubscription(followerUserName, followedUserName);
    }

    @GetMapping("/getUserSubscriptions")
    public List<Subscription> getUserSubscriptionsByFollowerUserName(@RequestParam String followerUserName) {
        return subscriptionService.getSubscriptionsByFollower(followerUserName);
    }

    @GetMapping("/getSubscription/{id}")
    public  Subscription getSubscription(@PathVariable Long id) {
        return subscriptionService.getSubscription(id);
    }

    @DeleteMapping("/deleteSubscription/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        return subscriptionService.deleteSubscription(id);
    }
}
