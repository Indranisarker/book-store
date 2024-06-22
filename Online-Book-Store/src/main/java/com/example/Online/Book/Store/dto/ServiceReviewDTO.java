package com.example.Online.Book.Store.dto;

import com.example.Online.Book.Store.entity.ServiceReview;
import com.example.Online.Book.Store.entity.User;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceReviewDTO {
    private Integer ratings;
    private String comments;
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public static ServiceReviewDTO reviewEntityToDTO(ServiceReview serviceReview){
        ServiceReviewDTO serviceReviewDTO = new ServiceReviewDTO();
        serviceReviewDTO.setUser(serviceReview.getUser());
        serviceReviewDTO.setRatings(serviceReview.getRatings());
        serviceReviewDTO.setComments(serviceReview.getComments());
        serviceReviewDTO.setCreateTime(serviceReview.getCreateTime());
        return serviceReviewDTO;
    }
}
