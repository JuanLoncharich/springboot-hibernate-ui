document.getElementById('loadBtn').addEventListener('click', () => {
    fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            const container = document.getElementById('userList');
            container.innerHTML = '<ul>' +
                users.map(user => `<li>ID: ${user.id} - Name: ${user.name}</li>`).join('') +
                '</ul>';
        })
        .catch(err => {
            console.error('Error fetching users:', err);
            alert('Failed to load users.');
        });
});