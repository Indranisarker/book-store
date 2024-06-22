package com.example.Online.Book.Store.dto;

import com.example.Online.Book.Store.entity.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfoDTO {
    private String full_name;
    private String phone_no;
    private String city;
    private String address;

    public static ShippingInfoDTO infoToDTO(ShippingInfo shippingInfo){
        ShippingInfoDTO shippingInfoDTO = new ShippingInfoDTO();
        shippingInfoDTO.setFull_name(shippingInfo.getFull_name());
        shippingInfoDTO.setPhone_no(shippingInfo.getPhone_no());
        shippingInfoDTO.setCity(shippingInfoDTO.getCity());
        shippingInfoDTO.setAddress(shippingInfoDTO.getAddress());
        return shippingInfoDTO;
    }
}
