package com.cclsr.eat.entity.dto;

import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.entity.SetMealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends SetMeal {

    private List<SetMealDish> setMealDishes;

    private String categoryName;
}
