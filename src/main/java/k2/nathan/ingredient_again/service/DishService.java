package k2.nathan.ingredient_again.service;

import java.util.List;

import org.springframework.stereotype.Service;

import k2.nathan.ingredient_again.entities.dish.Dish;
import k2.nathan.ingredient_again.repository.DishRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishService {
    
    private final DishRepository dishRepository;

    public List<Dish> findAll(){
        return this.dishRepository.findAll();
    }
}
