package org.skuhub.skuhub.api.taxi.service;

import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentDeleteRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentEditRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiCommentResponse;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public interface TaxiCommentService {
    public BaseResponse<String> postTaxiComment(TaxiCommentRequest request, String userId) throws IOException;

    public BaseResponse<List<TaxiCommentResponse>> getTaxiComment(Long postId);

    public BaseResponse<String> editTaxiComment(TaxiCommentEditRequest request, String userId);

    public BaseResponse<String> deleteTaxiComment(TaxiCommentDeleteRequest request, String userId);
}
