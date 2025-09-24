function startScan() {
    fetch("/fingerprint/scan", {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
    .then(res => res.json())
    .then(data => {
        alert("Kết quả: " + data.message);
    })
    .catch(err => {
        console.error(err);
        alert("Lỗi khi gửi yêu cầu quét!");
    });
}
