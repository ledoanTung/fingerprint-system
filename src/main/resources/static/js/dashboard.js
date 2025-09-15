document.addEventListener("DOMContentLoaded", function () {
    console.log("Dashboard loaded ✅");

    // Ví dụ: highlight menu hiện tại
    const currentPath = window.location.pathname;
    document.querySelectorAll(".sidebar .nav-link").forEach(link => {
        if (link.getAttribute("href") === currentPath) {
            link.classList.add("active");
        }
    });
});
