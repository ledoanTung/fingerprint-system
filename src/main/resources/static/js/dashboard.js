let socket = new SockJS('/ws');
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/fingerprint', function(message) {
            const data = JSON.parse(message.body);
            console.log(">>> Received from WS:", data);

            if (data.status === "scanned") {
                console.log(">>> Calling login API with id:", data.fingerprintId);

                // Gọi API login — quan trọng: include credentials (cookie)
                fetch(`/fingerprint/login?fingerprintId=${data.fingerprintId}`, {
                    method: "POST",
                    credentials: "same-origin" // or "include" nếu cần cross-site
                })
                .then(res => {
                    console.log(">>> raw fetch res:", res.status, res);
                    return res.json();
                })
                .then(loginData => {
                    console.log(">>> Login response JSON:", loginData);
                    if (loginData.status === "success") {
                        alert("Login thành công: " + loginData.username);
                        // reload/redirect để hiển thị navbar theo authentication
                        window.location.href = "/dashboard";
                    } else {
                        alert("Login thất bại: " + loginData.message);
                    }
                })
                .catch(err => {
                    console.error("Login error", err);
                    alert("Lỗi gọi API login");
                });
            } else {
                alert(data.message || "Scan failed");
            }
        });
    });

    document.getElementById("scanBtn").addEventListener("click", function() {
        // Gửi lên BE để BE gọi ESP32 quét
        stompClient.send("/app/scan", {}, "");
    });