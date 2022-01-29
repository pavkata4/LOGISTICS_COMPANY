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
        alert("Can't calculate the price because weight is empty.");
    } else if (isNaN(weight)) {
        alert("The entered value in weight field is not a number.")
    } else {
        alert(
            "The price of shipping to office will be " + (weight) + ".\r\n"
            + "The price of shipping to address will be " + (1.5 * weight) + "."
        );
    }
}