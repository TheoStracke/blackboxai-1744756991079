package com.pizzademo.config;

import com.pizzademo.model.Extra;
import com.pizzademo.model.Pizza;
import com.pizzademo.model.Combo;
import com.pizzademo.repository.ExtraRepository;
import com.pizzademo.repository.PizzaRepository;
import com.pizzademo.repository.ComboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            PizzaRepository pizzaRepository,
            ExtraRepository extraRepository,
            ComboRepository comboRepository) {
        return args -> {
            // Seed Pizzas
            if (pizzaRepository.count() == 0) {
                pizzaRepository.save(new Pizza(null, 
                    "Pepperoni", 
                    "Massa branca, molho de tomate, queijo mussarela e pepperoni",
                    new BigDecimal("45.00"),
                    "/images/pepperoni.png",
                    true));

                pizzaRepository.save(new Pizza(null,
                    "Queijo",
                    "Massa branca, molho de tomate, queijo mussarela",
                    new BigDecimal("40.00"),
                    "/images/queijo.png",
                    true));

                pizzaRepository.save(new Pizza(null,
                    "Marguerita",
                    "Massa branca, molho de tomate, queijo mussarela, tomates frescos e manjericão",
                    new BigDecimal("43.00"),
                    "/images/marguerita.png",
                    true));
            }

            // Seed Extras (Bordas)
            if (extraRepository.count() == 0) {
                // Bordas
                extraRepository.save(new Extra(null, "Cheddar", new BigDecimal("5.00"), Extra.ExtraType.BORDA));
                extraRepository.save(new Extra(null, "Catupiry", new BigDecimal("5.00"), Extra.ExtraType.BORDA));
                extraRepository.save(new Extra(null, "Requeijão", new BigDecimal("5.00"), Extra.ExtraType.BORDA));
                extraRepository.save(new Extra(null, "Chocolate", new BigDecimal("5.00"), Extra.ExtraType.BORDA));

                // Bebidas
                extraRepository.save(new Extra(null, "Coca-Cola", new BigDecimal("15.00"), Extra.ExtraType.BEBIDA));
                extraRepository.save(new Extra(null, "Guaraná", new BigDecimal("12.00"), Extra.ExtraType.BEBIDA));
                extraRepository.save(new Extra(null, "Fanta", new BigDecimal("10.00"), Extra.ExtraType.BEBIDA));
                extraRepository.save(new Extra(null, "Heineken", new BigDecimal("10.00"), Extra.ExtraType.BEBIDA));
            }

            // Seed Combos
            if (comboRepository.count() == 0) {
                comboRepository.save(new Combo(null,
                    "Combo Família",
                    "2 pizzas grandes + refrigerante 2L",
                    new BigDecimal("89.90"),
                    "/images/combo_familia.png",
                    "{\"pizzas\": 2, \"bebida\": \"Refrigerante 2L\"}"));

                comboRepository.save(new Combo(null,
                    "Combo Casal",
                    "1 pizza grande + refrigerante 1L",
                    new BigDecimal("59.90"),
                    "/images/combo_casal.png",
                    "{\"pizzas\": 1, \"bebida\": \"Refrigerante 1L\"}"));

                comboRepository.save(new Combo(null,
                    "Combo Individual",
                    "1 pizza média + 1 refrigerante 350ml",
                    new BigDecimal("45.90"),
                    "/images/combo_individual.png",
                    "{\"pizzas\": 1, \"bebida\": \"Refrigerante 350ml\"}"));
            }
        };
    }
}
