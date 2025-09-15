// users.js

async function loadUsers() {
    try {
        const res = await axios.get("/users/all"); // TODO: BE cần viết endpoint
        const tbody = document.getElementById("userTableBody");
        tbody.innerHTML = "";

        res.data.forEach(user => {
            tbody.innerHTML += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.role}</td>
                    <td>${user.fingerprintId || ""}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="openEditModal(${user.id})">Sửa</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Xóa</button>
                    </td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Lỗi khi load users:", err);
    }
}

document.addEventListener("DOMContentLoaded", loadUsers);
