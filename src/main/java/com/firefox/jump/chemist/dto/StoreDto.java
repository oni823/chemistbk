package com.firefox.jump.chemist.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private String jwt;
    private String storeName;
}
