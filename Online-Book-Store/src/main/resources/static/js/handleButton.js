function updateQuantity(button, change) {
    console.log('Clicked button:', button);
    console.log('Change:', change);
    const form = button.closest('form');
    console.log(form);
    const quantitySpan = form.querySelector('.quantity');
    let quantity = parseInt(quantitySpan.innerText, 10);
    quantity += change;
    if (quantity < 1) quantity = 1; // Prevent quantity from going below 1
    quantitySpan.innerText = quantity;
    form.querySelector('input[name="quantity"]').value = quantity;

    // Use AJAX to submit the form without reloading the page
    const xhr = new XMLHttpRequest();
    const formData = new FormData(form);

    // Ensure the quantity value is included in the form data
    formData.set('quantity', quantity);

    xhr.open('POST', form.getAttribute('th:action'), true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Handle success (optional)
                console.log('Quantity updated successfully');
            } else {
                // Handle error (optional)
                console.error('Error updating quantity');
            }
        }
    };
    xhr.send(formData);
    form.submit();
    // Update checkout links after quantity change
    updateCheckoutHref();
    updateCheckoutDetailsHref();
}

function calculateTotalPrice() {
    let totalPrice = 0;

    // Iterate over each cart item
    document.querySelectorAll('.cart-item').forEach(function(cartItem) {
        const price = parseFloat(cartItem.querySelector('.item-price').innerText);
        totalPrice += price;
    });
    // Update total price in the HTML
    let userEmail = document.querySelector('[data-user-email]').getAttribute('data-user-email');
    document.getElementById('emailAndTotal').innerText = userEmail + ', your total: ' + totalPrice.toFixed(2);
    return totalPrice;
}
function calculateTotalQuantity() {
    let totalQuantity = 0;
    document.querySelectorAll('.quantity').forEach(function(quantityElement) {
        totalQuantity += parseInt(quantityElement.innerText);
    });
    return totalQuantity;
}

// Function to update the href attribute of the checkout link
function updateCheckoutHref() {
    const totalQuantity = calculateTotalQuantity();
    const totalAmount = calculateTotalPrice();
    const checkoutLink = document.getElementById('checkout-link');
    checkoutLink.href = '/user/checkout?quantity=' + totalQuantity + '&amount=' + totalAmount;
}
function updateCheckoutDetailsHref() {
    console.log('Updating checkout details href');
    const totalQuantity = calculateTotalQuantity();
    const totalAmount = calculateTotalPrice();
    const checkoutDetailsLink = document.getElementById('checkout-details-link');
    checkoutDetailsLink.href = '/checkout-details?quantity=' + totalQuantity + '&amount=' + totalAmount;
}

//load multiple function
window.onload = function() {
    calculateTotalPrice();
    updateCheckoutHref();
    updateCheckoutDetailsHref();
};


