# Goal
Complete the checkout process by integrating an actual Online Payment Gateway, and automatically add some sample food items to the menu so the Home Screen and Search functions can be tested immediately.

## User Review Required
Please review the plan below. I plan to use **Razorpay** as the payment gateway since it is the industry standard for Android apps. I will use a **test API key** so you can simulate payments without using real money.

## Open Questions
- Is Razorpay the payment gateway you want to use, or do you prefer another one (like Stripe or PayPal)?
- For the sample food items, I will add items like "Burger", "Pizza", "Pasta", etc. Let me know if you want specific items added.

## Proposed Changes

### 1. Payment Gateway Integration (User App)
#### [MODIFY] `gradle/libs.versions.toml` & `build.gradle.kts`
- Add the Razorpay Android Checkout SDK dependency.

#### [MODIFY] `app/src/main/java/com/example/userapplication/PayOutActivity.kt`
- Implement the `PaymentResultListener` interface.
- Update the "Place My Order" logic:
  - If "Cash on Delivery" is selected, place the order normally.
  - If "Online Payment" is selected, launch the Razorpay Checkout screen.
  - On successful payment, the `onPaymentSuccess` callback will trigger and place the order in Firebase.
  - On failed payment, the `onPaymentError` callback will show an error message.

### 2. Sample Food Items (Admin / User App)
#### [MODIFY] `app/src/main/java/com/example/userapplication/MainActivity.kt`
- Add a temporary `seedData()` function that checks if the Firebase `menu` node is empty.
- If it is empty, it will automatically insert 6 delicious sample food items (with images, prices, and descriptions) so the Home Screen slider, Popular Items, and Search Bar work immediately.

### 3. GitHub
- Once implemented and tested, I will commit and push all code directly to your `main` branch.

## Verification Plan
1. Launch the User App. Verify the sample food items appear on the Home Screen.
2. Add an item to the cart and proceed to Checkout.
3. Select "Online Payment" and click "Place My Order". Verify the Razorpay popup appears.
4. Complete a test payment and verify the order is successfully placed in Firebase.
