package org.skuhub.skuhub.api.push.service;

import org.skuhub.skuhub.api.push.dto.KeywordRequest;
import org.skuhub.skuhub.api.push.dto.KeywordResponse;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface PushService {

    BaseResponse<String> saveToken(String userId, String tokenRequest);

    BaseResponse<String> deleteToken(String userId);

    BaseResponse<String> saveKeyword(String userId, String Keyword);

    BaseResponse<String> deleteKeyword(String userId, Long Keyword);

    boolean pushKeywordAlarm(Long postId, String notice) throws IOException;

    boolean pushTaxiJoinAlarm(Long postId) throws IOException;

    boolean pushTaxiCommentAlarm(Long postId, String content) throws IOException;

    BaseResponse<List<KeywordResponse>> getKeyword(String userId);
}
