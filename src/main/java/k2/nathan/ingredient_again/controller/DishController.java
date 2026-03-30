package k2.nathan.ingredient_again.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import k2.nathan.ingredient_again.service.DishService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishController {
    
    private final DishService dishService;

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        try {
             return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(this.dishService.findAll());
        } catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
