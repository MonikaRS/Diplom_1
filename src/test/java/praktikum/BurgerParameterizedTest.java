package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerParameterizedTest {

    private final float bunPrice;
    private final float ingredientPrice1;
    private final float ingredientPrice2;
    private final float expectedTotal;

    public BurgerParameterizedTest(float bunPrice, float ingredientPrice1,
                                   float ingredientPrice2, float expectedTotal) {
        this.bunPrice = bunPrice;
        this.ingredientPrice1 = ingredientPrice1;
        this.ingredientPrice2 = ingredientPrice2;
        this.expectedTotal = expectedTotal;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {100.0f, 50.0f, 75.0f, 325.0f},
                {0.0f, 100.0f, 200.0f, 300.0f},
                {150.5f, 0.0f, 0.0f, 301.0f},
                {10.0f, 15.0f, 20.0f, 55.0f},
                {200.0f, 50.0f, 50.0f, 500.0f},
                {0.0f, 0.0f, 0.0f, 0.0f}
        });
    }

    @Test
    public void testGetPriceWithDifferentPrices() {
        Bun mockBun = Mockito.mock(Bun.class);
        Ingredient mockIngredient1 = Mockito.mock(Ingredient.class);
        Ingredient mockIngredient2 = Mockito.mock(Ingredient.class);

        when(mockBun.getPrice()).thenReturn(bunPrice);
        when(mockIngredient1.getPrice()).thenReturn(ingredientPrice1);
        when(mockIngredient2.getPrice()).thenReturn(ingredientPrice2);

        Burger burger = new Burger();
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        float actualPrice = burger.getPrice();
        assertEquals(expectedTotal, actualPrice, 0.001);
    }
}
