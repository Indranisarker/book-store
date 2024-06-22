document.addEventListener('DOMContentLoaded', function() {
    const stars = document.querySelectorAll('.star');
    const ratingInput = document.getElementById('rating');

    //show modal view
    $(document).ready(function() {
        const modal = $('#reviewModal');
        const closeButton = $('.close-button');

        $('#openModalButton').click(function(){
            modal.show();
        });

        closeButton.click(function() {
            closeModal();
        });

        $(window).click(function(event) {
            if ($(event.target).is(modal)) {
                closeModal();
            }
        });

        function closeModal() {
            modal.hide();
            window.history.back(); // Redirect to the previous page
        }
    });
    $(document).ready(function() {
        const modal = $('#addToCartModal');
        const closeButton = $('.close');

        $('#openModalButton').click(function() {
            modal.show();
        });

        closeButton.click(function() {
            closeModal();
        });

        $(window).click(function(event) {
            if ($(event.target).is(modal)) {
                closeModal();
            }
        });

        function closeModal() {
            modal.hide();
            window.history.back(); // Redirect to the previous page
        }
    });
    //start clicking
    stars.forEach(function(star) {
        star.addEventListener('mouseover', function() {
            const value = parseInt(star.getAttribute('data-value'));

            stars.forEach(function(s, index) {
                if (index < value) {
                    s.classList.add('filled');
                } else {
                    s.classList.remove('filled');
                }
            });
        });

        star.addEventListener('click', function() {
            const value = parseInt(star.getAttribute('data-value'));
            ratingInput.value = value;

            stars.forEach(function(s, index) {
                if (index < value) {
                    s.classList.add('selected');
                } else {
                    s.classList.remove('selected');
                }
            });
        });
    });
});
