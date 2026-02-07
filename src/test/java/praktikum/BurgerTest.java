package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockIngredient1;

    @Mock
    private Ingredient mockIngredient2;

    @Mock
    private Ingredient mockIngredient3;

    private Burger burger;

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(mockBun);
        assertSame(mockBun, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(mockIngredient1);
        assertEquals(1, burger.ingredients.size());
        assertSame(mockIngredient1, burger.ingredients.get(0));
    }

    @Test
    public void testAddMultipleIngredients() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        assertEquals(2, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertSame(mockIngredient2, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWithInvalidIndex() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);
        burger.addIngredient(mockIngredient3);

        burger.moveIngredient(0, 2);

        assertEquals(3, burger.ingredients.size());
        assertSame(mockIngredient2, burger.ingredients.get(0));
        assertSame(mockIngredient3, burger.ingredients.get(1));
        assertSame(mockIngredient1, burger.ingredients.get(2));
    }

    @Test
    public void testMoveIngredientToSamePosition() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        burger.moveIngredient(0, 0);

        assertSame(mockIngredient1, burger.ingredients.get(0));
    }

    @Test
    public void testGetPriceWithBunAndIngredients() {
        when(mockBun.getPrice()).thenReturn(100.0f);
        when(mockIngredient1.getPrice()).thenReturn(50.0f);
        when(mockIngredient2.getPrice()).thenReturn(75.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        float price = burger.getPrice();
        float expectedPrice = (100.0f * 2) + 50.0f + 75.0f;

        assertEquals(expectedPrice, price, 0.001);
        verify(mockBun, times(1)).getPrice();
        verify(mockIngredient1, times(1)).getPrice();
        verify(mockIngredient2, times(1)).getPrice();
    }

    @Test
    public void testGetPriceWithOnlyBun() {
        when(mockBun.getPrice()).thenReturn(150.0f);

        burger.setBuns(mockBun);

        float price = burger.getPrice();

        assertEquals(300.0f, price, 0.001);
        verify(mockBun, times(1)).getPrice();
    }

    @Test
    public void testGetPriceWithFreeBun() {
        when(mockBun.getPrice()).thenReturn(0.0f);
        when(mockIngredient1.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);

        float price = burger.getPrice();

        assertEquals(100.0f, price, 0.001);
        verify(mockBun, times(1)).getPrice();
    }

    @Test
    public void testGetReceiptWithIngredients() {
        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(200.0f);
        when(mockIngredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient1.getName()).thenReturn("hot sauce");
        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("cutlet");

        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        String receipt = burger.getReceipt();

        assertNotNull(receipt);
        assertTrue(receipt.contains("(==== black bun ====)"));
        assertTrue(receipt.contains("= sauce hot sauce ="));
        assertTrue(receipt.contains("= filling cutlet ="));
        assertTrue(receipt.contains("Price:"));

        verify(mockBun, times(2)).getName();
        verify(mockBun, times(1)).getPrice();
    }

    @Test
    public void testGetReceiptWithoutIngredients() {
        when(mockBun.getName()).thenReturn("white bun");
        when(mockBun.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);

        String receipt = burger.getReceipt();

        assertTrue(receipt.startsWith("(==== white bun ====)\n"));
        assertTrue(receipt.contains("(==== white bun ====)\n"));
        assertTrue(receipt.contains("Price:"));

        verify(mockBun, times(2)).getName();
        verify(mockBun, times(1)).getPrice();
    }

    @Test
    public void testGetReceiptWithDifferentIngredientTypes() {
        when(mockBun.getName()).thenReturn("bun");
        when(mockIngredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient1.getName()).thenReturn("chili");
        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("dinosaur");

        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("= sauce chili ="));
        assertTrue(receipt.contains("= filling dinosaur ="));
    }

    @Test
    public void testIngredientsListIsEmptyInitially() {
        assertTrue(burger.ingredients.isEmpty());
    }

    @Test
    public void testBunIsNullInitially() {
        assertNull(burger.bun);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithNoBunShouldThrowException() {
        burger.addIngredient(mockIngredient1);
        burger.getPrice();
    }

    @Test
    public void testGetPriceWithEmptyBurger() {
        when(mockBun.getPrice()).thenReturn(50.0f);
        burger.setBuns(mockBun);

        float price = burger.getPrice();

        assertEquals(100.0f, price, 0.001);
    }
}
// Тесты для класса Burger - покрытие 100%
