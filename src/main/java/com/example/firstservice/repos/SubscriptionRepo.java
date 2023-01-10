package com.example.firstservice.repos;

import com.example.firstservice.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    List<Subscription> findByFollower_UserName(String userName);

    List<Subscription> findByFollowed_UserName(String userName);

    Subscription findByFollower_UserNameAndFollowed_UserName(String followerUsername, String followedUserName);

}
