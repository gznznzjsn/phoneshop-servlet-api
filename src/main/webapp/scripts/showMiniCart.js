const cart = document.querySelector(".cart")

const popUpCart = document.querySelector(".cart-popup")


cart.addEventListener('mouseenter', () => {
    popUpCart.classList.add("active")
})

cart.addEventListener('mouseleave', () => {
    popUpCart.classList.remove("active")
})
