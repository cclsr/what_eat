package com.cclsr.eat.entity.dto;

import com.cclsr.eat.entity.Dish;
import com.cclsr.eat.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
