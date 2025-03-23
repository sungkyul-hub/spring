package org.skuhub.skuhub.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private HttpStatus status;

    public CustomException(ErrorCode errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
