package com.youthcon.start.application;

import com.youthcon.start.domain.Review;
import com.youthcon.start.error.ReviewNotFoundException;
import com.youthcon.start.infra.ReviewRepository;
import com.youthcon.start.error.DuplicateSendGiftException;
import com.youthcon.start.error.SendGiftInternalException;
import com.youthcon.start.infra.GiftApi;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReviewService {

    private final GiftApi giftApi;
    private final ReviewRepository reviewRepository;

    public ReviewService(GiftApi giftApi, ReviewRepository reviewRepository) {
        this.giftApi = giftApi;
        this.reviewRepository = reviewRepository;
    }

    public Review getById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("no review: " + id));
    }

    @Transactional
    public Review sendGift(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("no review id : " + id));

        if (review.getSent()) {
            throw new DuplicateSendGiftException("duplicate review id : " + id);
        }

        if (!giftApi.send(review.getPhoneNumber())) {
            throw new SendGiftInternalException("Internal excaption");
        }

        review.makeTrue();

        return review;
    }
}
