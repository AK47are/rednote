package com.example.rednote.service;

public interface LikeService {

    Boolean isLike(Integer postId);

    Boolean toogleLike(Integer postId);
}
