function goBack() {
    window.location.href = "/api/user-list";
}
async function getFingerprint() {
        try {
            const res = await fetch("/fingerprint/enroll");
            if (!res.ok) throw new Error("Không lấy được ID từ BE");
            const id = await res.text();
            document.getElementById("fingerprintId").value = id;
        } catch (err) {
            alert("Lỗi: " + err.message);
        }
}
// Xử lý cho trang Create
const createForm = document.getElementById("userForm");
if (createForm) {
    createForm.addEventListener("submit", async function(e) {
        e.preventDefault();

        const user = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            role: document.getElementById("role").value,
            fingerprintId: document.getElementById("fingerprintId").value
        };

        try {
            const res = await fetch("/users/create", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
            });

            if (!res.ok) throw new Error("Lưu user thất bại");

            const saved = await res.json();
            alert("Thêm User thành công!");
            window.location.href = "/api/user-list";
        } catch (err) {
            alert("Lỗi: " + err.message);
        }
    });
}

const pathParts = window.location.pathname.split("/");
const lastPart = pathParts[pathParts.length - 1];
// Nếu lastPart là số (id user) thì mới gọi API để fill form
if (!isNaN(lastPart)) {
    const userId = lastPart;

    fetch(`/api/users/${userId}`)
        .then(res => res.json())
        .then(user => {
            document.getElementById("username").value = user.username;
            document.getElementById("password").value = user.password;
            document.getElementById("role").value = user.role;
            document.getElementById("fingerprintId").value = user.fingerprintId || "";
        })
        .catch(err => console.error("Lỗi khi load user:", err));
}

// Xử lý cho trang Edit
const editForm = document.getElementById("editForm");
if (editForm) {
    editForm.addEventListener("submit", function(e) {
        e.preventDefault();
            const user = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            role: document.getElementById("role").value,
            fingerprintId: document.getElementById("fingerprintId").value
        };
        console.log(lastPart);
        fetch(`/api/users/edit/${lastPart}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        })
        .then(res => {
            if (!res.ok) throw new Error("Cập nhật thất bại");
            return res.json();
        })
        .then(data => {
            alert("Cập nhật User thành công!");
            window.location.href = "/api/user-list";
        })
        .catch(err => alert("Lỗi: " + err.message));
    });
}


function deleteUser(userId) {
    if(confirm("Bạn có chắc chắn muốn xóa người dùng này?")) {
        fetch(`/api/users/${userId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if(response.ok) {
                alert("Xóa thành công!");
                location.reload();
            } else {
                alert("Xóa thất bại!");
            }
        })
        .catch(error => console.error('Error:', error));
    }
}


