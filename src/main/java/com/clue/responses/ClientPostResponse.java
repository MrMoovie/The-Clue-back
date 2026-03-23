package com.clue.responses;

import com.clue.entities.BidEntity;
import com.clue.entities.PostEntity;

import java.util.List;

public class ClientPostResponse extends BasicResponse {
    private PostModel post;

    public ClientPostResponse() {
    }

    public ClientPostResponse(boolean success, Integer errorCode, PostEntity postEntity, List<BidEntity> bidEntities) {
        super(success, errorCode);
        this.post = new PostModel(postEntity, bidEntities.stream().filter(bid -> bid.getPostEntity().getId() == postEntity.getId()).toList());
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }
}
