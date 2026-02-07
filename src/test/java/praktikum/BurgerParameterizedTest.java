package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerParameterizedTest {
    
    private Burger burger;
    
    private final int indexToRemove;
    private final int expectedSizeAfterRemove;
    
    public BurgerParameterizedTest(int indexToRemove, int expectedSizeAfterRemove) {
        this.indexToRemove = indexToRemove;
        this.expectedSizeAfterRemove = expectedSizeAfterRemove;
    }
    
    @Parameterized.Parameters(name = "Удаление ингредиента с индекса {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {0, 2},  // Удаляем первый ингредиент
            {1, 2},  // Удаляем второй ингредиент
            {2, 2}   // Удаляем третий ингредиент
        });
    }
    
    @Before
    public void setUp() {
        // Создаем моки как локальные переменные
        Bun mockBun = mock(Bun.class);
        Ingredient mockIngredient1 = mock(Ingredient.class);
        Ingredient mockIngredient2 = mock(Ingredient.class);
        Ingredient mockIngredient3 = mock(Ingredient.class);
        
        burger = new Burger();
        
        // Настраиваем поведение моков
        when(mockBun.getPrice()).thenReturn(50.0f);
        when(mockBun.getName()).thenReturn("Булочка");
        
        when(mockIngredient1.getName()).thenReturn("Сыр");
        when(mockIngredient1.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient1.getPrice()).thenReturn(40.0f);
        
        when(mockIngredient2.getName()).thenReturn("Соус");
        when(mockIngredient2.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient2.getPrice()).thenReturn(20.0f);
        
        when(mockIngredient3.getName()).thenReturn("Котлета");
        when(mockIngredient3.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient3.getPrice()).thenReturn(60.0f);
        
        // Собираем бургер
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        burger.addIngredient(mockIngredient3);
    }
    
    @Test
    public void testRemoveIngredientParameterized() {
        int initialSize = burger.ingredients.size();
        
        burger.removeIngredient(indexToRemove);
        
        assertEquals("Неверное количество ингредиентов после удаления", 
                     expectedSizeAfterRemove, burger.ingredients.size());
        assertNotEquals("Количество ингредиентов должно измениться", 
                       initialSize, burger.ingredients.size());
    }
    
    @Test
    public void testGetPriceAfterRemovingIngredients() {
        float priceBefore = burger.getPrice();
        
        burger.removeIngredient(indexToRemove);
        float priceAfter = burger.getPrice();
        
        assertTrue("Цена должна уменьшиться после удаления ингредиента", 
                   priceAfter < priceBefore);
        
        // Проверяем что цена стала правильной
        float expectedPrice = 50.0f * 2; // Булочка * 2
        expectedPrice += 40.0f + 20.0f + 60.0f; // Все ингредиенты
        
        // Вычитаем цену удаленного ингредиента
        switch (indexToRemove) {
            case 0: expectedPrice -= 40.0f; break; // Сыр
            case 1: expectedPrice -= 20.0f; break; // Соус
            case 2: expectedPrice -= 60.0f; break; // Котлета
        }
        
        assertEquals("Неверная цена после удаления ингредиента", 
                     expectedPrice, priceAfter, 0.001f);
    }
}
