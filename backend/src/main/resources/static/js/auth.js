document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registerForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const fechaNacimiento = document.getElementById('fechaNacimiento').value;
        const genero = document.getElementById('genero').value;
        const altura = parseFloat(document.getElementById('altura').value);
        const pesoActual = parseFloat(document.getElementById('pesoActual').value);

        fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nombre,
                email,
                password,
                fechaNacimiento,
                genero,
                altura,
                pesoActual
            })
        })
            .then(async response => {
                if (!response.ok) {
                    // Try to parse validation errors
                    const errorData = await response.json().catch(() => ({}));

                    // If we got field errors, show them
                    if (Object.keys(errorData).length > 0) {
                        let errorMessage = '';
                        for (let field in errorData) {
                            errorMessage += `${field}: ${errorData[field]}\n`;
                        }
                        alert(`Errores de validación:\n${errorMessage}`);
                    } else {
                        alert('❌ Hubo un error en el registro');
                    }

                    throw new Error("Validation or server error");
                }

                return response.json();
            })
            .then(user => {
                alert('✅ Registro exitoso');
                window.location.href = 'index.html'; // Redirect back to login
            })
            .catch(err => {
                console.error('Error:', err);
                // Optional: display more technical error only in dev
            });
    });
});