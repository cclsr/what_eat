package com.cclsr.eat.entity.dto;

import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.entity.SetMealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetMealDto extends SetMeal {

    private List<SetMealDish> setmealDishes;

    private String categoryName;
}
