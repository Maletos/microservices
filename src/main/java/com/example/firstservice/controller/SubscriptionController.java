package com.example.firstservice.controller;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("subscribe/v1")
    @Operation(summary = "Create subscription for user on another user")
    public String createSubscription(@RequestParam String followerUserName, @RequestParam String followedUserName) {
        return subscriptionService.createSubscription(followerUserName, followedUserName);
    }

    @PostMapping("subscribe/v2")
    @Operation(summary = "Create subscription for user on another user")
    public String createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping("/getUserSubscriptions")
    @Operation(summary = "Create subscription for user on another user")
    public List<Subscription> getUserSubscriptionsByFollowerUserName(@RequestParam String followerUserName) {
        return subscriptionService.getSubscriptionsByFollower(followerUserName);
    }

    @GetMapping("/getSubscription/{id}")
    @Operation(summary = "Get subscription by id")
    public  Subscription getSubscription(@PathVariable Long id) {
        return subscriptionService.getSubscription(id);
    }

    @DeleteMapping("/deleteSubscription/{id}")
    @Operation(summary = "Delete user subscription")
    public String deleteSubscription(@PathVariable Long id) {
        return subscriptionService.deleteSubscription(id);
    }
}
