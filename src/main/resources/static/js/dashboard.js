let socket = new SockJS('/ws');
let stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // FE subscribe để nhận kết quả scan
    stompClient.subscribe('/topic/fingerprint', function(message) {
        const data = JSON.parse(message.body);
        if (data.status === "success") {
            alert("Login thành công: " + data.username);
            if (data.role === "ADMIN") {
                window.location.href = "/dashboard";
            } else {
                window.location.href = "/logs";
            }
        } else {
            alert(data.message);
        }
    });
});

// Nút Scan
document.getElementById("scanBtn").addEventListener("click", function() {
    stompClient.send("/app/scan", {}, "");
});
