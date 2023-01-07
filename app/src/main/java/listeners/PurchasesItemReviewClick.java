package listeners;

import models.PurchasesItem;

public interface PurchasesItemReviewClick {
    void onPurchasesReviewClicked(PurchasesItem purchasesItem,int position);
    void onWriteReviewClicked(PurchasesItem purchasesItem,int position);
}
