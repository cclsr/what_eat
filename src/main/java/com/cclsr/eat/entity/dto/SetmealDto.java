package com.cclsr.eat.entity.dto;

import com.cclsr.eat.entity.Setmeal;
import com.cclsr.eat.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
