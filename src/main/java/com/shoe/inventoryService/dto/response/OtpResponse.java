package com.shoe.inventoryService.dto.response;


import com.shoe.inventoryService.enums.ApiResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {
    private ApiResponseCode responseCode;
    private String responseMessage;
    private String token;
    private UserInfoDTO user;
}
