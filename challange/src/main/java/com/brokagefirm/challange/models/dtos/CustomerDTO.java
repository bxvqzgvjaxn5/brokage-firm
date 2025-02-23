package com.brokagefirm.challange.models.dtos;

import com.brokagefirm.challange.models.CustomerType;

@lombok.Data
@lombok.NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private CustomerType type;
    private String email;

    public CustomerDTO(CustomerDTO customer) {
        this.id = customer.getId();
        this.type = customer.getType();
        this.email = customer.getEmail();
    }
}
