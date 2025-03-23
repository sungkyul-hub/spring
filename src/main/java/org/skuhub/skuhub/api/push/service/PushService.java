package org.skuhub.skuhub.api.push.service;

import org.skuhub.skuhub.common.response.BaseResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PushService {

    BaseResponse<String> saveToken(String userId, String tokenRequest);

    BaseResponse<String> deleteToken(String userId);

    BaseResponse<String> saveKeyword(String userId, String Keyword);

    BaseResponse<String> deleteKeyword(String userId, String Keyword);

    boolean pushKeywordAlarm(Long postId, String notice) throws IOException;

    boolean pushTaxiJoinAlarm(Long postId) throws IOException;

    boolean pushTaxiCommentAlarm(Long postId, String content) throws IOException;
}
