package org.skuhub.skuhub.api.taxi.service;

import org.skuhub.skuhub.api.taxi.dto.request.TaxiEditRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostDeleteRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostDetailsResponse;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostResponse;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaxiPostService {
    public BaseResponse<String> postTaxiShare(TaxiPostRequest request, String userId);

    public BaseResponse<List<TaxiPostResponse>> getTaxiShare(Long cursor, int limit);

    public BaseResponse<String> postEditTaxiShare(TaxiEditRequest request, String authorizationHeader);

    public BaseResponse<String> deleteTaxiShare(TaxiPostDeleteRequest request, String authorizationHeader);

    public BaseResponse<TaxiPostDetailsResponse> getTaxiShareDetail(Long postId);
}
