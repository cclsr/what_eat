package com.cclsr.eat.entity.dto;

import com.cclsr.eat.entity.OrderDetail;
import com.cclsr.eat.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
