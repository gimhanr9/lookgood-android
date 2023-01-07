package listeners;

import models.CartItem;

public interface CartItemClick {
    void onCartItemClicked(CartItem cartItem);
    void changeQuantity(CartItem cartItem,int quantity);
}
