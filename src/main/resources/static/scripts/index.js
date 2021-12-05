function changeTab(tab) {
    const registerTab = document.querySelector('.tab.register').classList;
    const loginTab = document.querySelector('.tab.login').classList;

    const registerForm = document.querySelector('.authenticate-form.register').classList;
    const loginForm = document.querySelector('.authenticate-form.login').classList;


    const registerActiveTab = registerTab.contains('active');
    const loginActiveTab = loginTab.contains('active');


    if (tab == 'register') {
        if (!registerActiveTab) {
            registerTab.add("active");
            loginTab.remove("active");

            registerForm.add("active");
            loginForm.remove("active");
        }
    } else {
         if (!loginActiveTab) {
            loginTab.add("active");
            registerTab.remove("active");

            loginForm.add("active");
            registerForm.remove("active");
         }
    }
}
