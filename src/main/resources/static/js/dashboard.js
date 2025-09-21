document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("scanFingerprintBtn");

    if (btn) {
        btn.addEventListener("click", () => {
            fetch("/api/fingerprint/scan", {
                method: "POST"
            })
            .then(res => {
                if (!res.ok) throw new Error("Scan failed");
                return res.json();
            })
            .then(data => {
                alert("Vân tay hợp lệ! Xin chào " + data.username);
                window.location.href = "/dashboard"; // reload để cập nhật user login
            })
            .catch(err => {
                alert("Lỗi: " + err.message);
            });
        });
    }
});
