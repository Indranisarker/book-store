package com.example.Online.Book.Store.repository;

import com.example.Online.Book.Store.entity.ServiceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceReviewRepository extends JpaRepository<ServiceReview, Long> {

}
