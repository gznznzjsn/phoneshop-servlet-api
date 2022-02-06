const showButtons = document.querySelectorAll(".history-button")
const popUpBack = document.querySelector(".history-background")
const popUp = document.querySelector(".price-history")


showButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        popUpBack.classList.add("active")

    })
})

popUpBack.addEventListener('click', (event) => {
    if (event.target != popUp) {
        popUpBack.classList.remove("active")
    }
})
