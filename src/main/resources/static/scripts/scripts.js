function openMenu() {
    var x = document.getElementById("topNavigation");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}

function calculatePrice() {
    var weight = document.getElementById("weight").value;
    if (weight == 0 || weight == "") {
        alert("Не може да се изчисля цената поради невалидно тегло на пратката.");
    } else if (isNaN(weight)) {
        alert("Въведената стойност за тегло не е число.")
    } else {
        alert(
            "Цената за доставка до офиса е: " + (weight) + ".\r\n"
            + "Цената за доставка до адрес е: " + (1.5 * weight) + "."
        );
    }
}