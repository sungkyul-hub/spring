package org.skuhub.skuhub.api.taxi.service;

import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiJoinRequest;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.skuhub.skuhub.repository.taxi.TaxiCommentRepository;
import org.skuhub.skuhub.repository.taxi.TaxiShareRepository;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TaxiJoinService {

    public BaseResponse<String> joinTaxiShare(TaxiJoinRequest request, String userId) throws IOException;

    public BaseResponse<String> leaveTaxiShare(TaxiJoinRequest request, String userId);
}
