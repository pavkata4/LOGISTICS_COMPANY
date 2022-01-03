function openMenu() {
    var x = document.getElementById("topNavigation");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}