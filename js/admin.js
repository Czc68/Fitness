// 管理员后台JS模板
// 需完善API地址和具体实现

const token = localStorage.getItem('token');

// 加载用户列表
function loadUsers() {
    fetch('/api/admin/users', {
        headers: { 'Authorization': `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) renderUserTable(data.users);
    });
}

// 渲染用户表格
function renderUserTable(users) {
    const tbody = document.querySelector('#user-table tbody');
    tbody.innerHTML = '';
    users.forEach(user => {
        tbody.innerHTML += `
        <tr>
            <td>${user.userId}</td>
            <td>${user.username}</td>
            <td>${user.fullName || ''}</td>
            <td>${user.email || ''}</td>
            <td>${user.userRole || ''}</td>
            <td>${user.status === 'active' ? '正常' : '禁用'}</td>
            <td>
                <button class="admin-btn edit" onclick="editUser(${user.userId})">编辑</button>
                <button class="admin-btn delete" onclick="deleteUser(${user.userId})">删除</button>
                <button class="admin-btn role" onclick="toggleRole(${user.userId}, '${user.userRole}')">切换权限</button>
            </td>
        </tr>`;
    });
}

// 加载所有打卡数据
function loadData() {
    fetch('/api/admin/checkins', {
        headers: { 'Authorization': `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) renderDataTable(data.checkins);
    });
}

// 渲染数据表格
function renderDataTable(checkins) {
    const tbody = document.querySelector('#data-table tbody');
    tbody.innerHTML = '';
    checkins.forEach(c => {
        tbody.innerHTML += `
        <tr>
            <td>${c.checkinId}</td>
            <td>${c.userId || ''}</td>
            <td>${c.checkinDate || ''}</td>
            <td>${c.weight || ''}</td>
            <td>${c.sleepHours || ''}</td>
            <td>${c.waterCups || ''}</td>
            <td>${c.steps || ''}</td>
            <td>${c.mood || ''}</td>
            <td>${c.generalNotes || ''}</td>
            <td>
                <button class="admin-btn edit" onclick="editCheckin(${c.checkinId})">编辑</button>
                <button class="admin-btn delete" onclick="deleteCheckin(${c.checkinId})">删除</button>
            </td>
        </tr>`;
    });
}

// 编辑/删除/切换权限等操作函数（需完善弹窗和API调用）
function editUser(id) { alert('编辑用户功能待实现: ' + id); }
function deleteUser(id) {
    if (!confirm('确定要删除该用户吗？此操作不可恢复！')) return;
    fetch(`/api/admin/user/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('用户删除成功！');
            loadUsers();
        } else {
            alert('删除失败：' + (data.message || '未知错误'));
        }
    });
}
function toggleRole(id, currentRole) {
    const nextRole = currentRole === '管理员' ? '用户' : '管理员';
    const role = prompt('请输入新角色（用户/管理员）：', nextRole);
    if (role !== '用户' && role !== '管理员') {
        alert('角色只能是"用户"或"管理员"！');
        return;
    }
    // 先获取用户完整信息
    fetch(`/api/admin/user/detail/${id}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    })
    .then(res => res.json())
    .then(data => {
        if (!data.success || !data.user) {
            alert('获取用户信息失败，无法切换权限：' + (data.message || ''));
            return;
        }
        const user = data.user;
        user.userRole = role;
        // 提交完整对象
        fetch(`/api/admin/user/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
        .then(res => res.json())
        .then(data2 => {
            if (data2.success) {
                alert('权限切换成功！');
                loadUsers();
            } else {
                alert('切换失败：' + (data2.message || '未知错误'));
            }
        });
    });
}

// 页面加载时自动拉取数据
window.onload = function() {
    loadUsers();
    loadData();
}; 