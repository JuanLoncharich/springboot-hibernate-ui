

// Show sections
function showRegister() {
    document.getElementById('authSection').style.display = 'none';
    document.getElementById('registerSection').style.display = 'block';
}

function showLogin() {
    document.getElementById('registerSection').style.display = 'none';
    document.getElementById('authSection').style.display = 'block';
}

// Handle login
function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    })
        .then(async res => {
            if (!res.ok) {
                const errorMessage = await res.text();
                throw new Error(errorMessage);
            }
            return res.json();
        })
        .then(user => {
            localStorage.setItem("currentUser", JSON.stringify(user));
            window.location.href = 'dashboard.html';
        })
        .catch(err => {
            alert(err.message || 'Error al iniciar sesión');
        });
}

// Handle register
function register() {
    const name = document.getElementById('regName').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;

    fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre: name, email, password })
    })
        .then(res => res.json())
        .then(user => {
            currentUser = user;
            showDashboard(user);
        })
        .catch(err => alert('Error al registrar usuario'));
}

// Logout
function logout() {
    localStorage.removeItem("currentUser");
    window.location.href = 'index.html';
}

// Create Registro (without food)
function createRegistro() {
    const fecha = document.getElementById('fecha').value;
    const horario = document.getElementById('horario').value;

    if (!fecha || !horario) {
        alert("Por favor completa todos los campos");
        return;
    }

    const currentUser = JSON.parse(localStorage.getItem("currentUser"));

    fetch('/api/registros', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            fecha,
            horario,
            usuario: { id_usuario: currentUser.id }
        })
    })
        .then(res => res.json())
        .then(() => {
            alert("Registro creado");
        });
}

// Dashboard UI
function showDashboard(user) {
    localStorage.setItem("currentUser", JSON.stringify(user));
    window.location.href = 'dashboard.html';
}

function agregarAlimento() {
    const container = document.getElementById("alimentosContainer");

    const div = document.createElement("div");
    div.className = "input-group mb-2";

    div.innerHTML = `
        <input type="number" placeholder="ID del alimento" class="form-control me-2">
        <input type="number" step="0.01" placeholder="Cantidad (g/ml)" class="form-control me-2">
        <button onclick="this.parentElement.remove()" class="btn btn-outline-danger" type="button">Eliminar</button>
    `;

    container.appendChild(div);
}

function crearRegistroConComida() {
    const fecha = document.getElementById("fechaRegistro").value;
    const horario = document.getElementById("horarioRegistro").value;

    const nombre = document.getElementById("manualNombre").value;
    const calorias = parseFloat(document.getElementById("manualCalorias").value);
    const proteinas = parseFloat(document.getElementById("manualProteinas").value);
    const carbohidratos = parseFloat(document.getElementById("manualCarbohidratos").value);
    const grasas = parseFloat(document.getElementById("manualGrasas").value);
    const cantidad = parseDouble(document.getElementById("manualCantidad").value);


    if (!fecha || !horario || !nombre || isNaN(calorias) || isNaN(cantidad)) {
        alert("Completa todos los campos obligatorios.");
        return;
    }

    const request = {
        fecha,
        horario,
        idUsuario: currentUser.id,

        nombre,
        calorias,
        proteinas,
        carbohidratos,
        grasas,
        cantidad
    };

    fetch("/api/registros/registrar-comida-manual", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(request)
    })
        .then(res => {
            if (!res.ok) throw new Error("Error al guardar registro");
            return res.json();
        })
        .then(() => {
            alert("✅ Registro guardado exitosamente");
            cargarRegistros(); // Recargar lista
            limpiarFormulario();
        })
        .catch(err => {
            alert("❌ Hubo un error al guardar");
            console.error("Error:", err);
        });
}

function limpiarFormulario() {
    document.getElementById("manualNombre").value = "";
    document.getElementById("manualCalorias").value = "";
    document.getElementById("manualProteinas").value = "";
    document.getElementById("manualCarbohidratos").value = "";
    document.getElementById("manualGrasas").value = "";
    document.getElementById("manualCantidad").value = "";
}

function buscarAlimentos(nombre) {
    const container = document.getElementById("suggestionsContainer");
    if (!nombre || nombre.length < 2) {
        container.innerHTML = "";
        return;
    }

    fetch(`/api/alimentos/buscar?nombre=${encodeURIComponent(nombre)}`)
        .then(res => res.json())
        .then(alimentos => {
            container.innerHTML = "";

            alimentos.forEach(alimento => {
                const div = document.createElement("div");
                div.className = "suggestion-item";
                div.innerText = `${alimento.nombre} - ${alimento.calorias} kcal`;
                div.onclick = () => seleccionarAlimento(alimento);
                container.appendChild(div);
            });
        })
        .catch(err => console.error("Error buscando alimentos:", err));
}

function seleccionarAlimento(alimento) {
    document.getElementById("manualNombre").value = alimento.nombre;
    document.getElementById("manualCalorias").value = alimento.calorias;
    document.getElementById("manualProteinas").value = alimento.proteinas;
    document.getElementById("manualCarbohidratos").value = alimento.carbohidratos;
    document.getElementById("manualGrasas").value = alimento.grasas;
    document.getElementById("manualCantidad").focus();
}

function cargarRegistros() {
    const currentUser = JSON.parse(localStorage.getItem("currentUser"));
    if (!currentUser) {
        window.location.href = 'index.html';
        return;
    }

    fetch(`/api/registros/alimentos?idUsuario=${currentUser.id}`)
        .then(res => res.json())
        .then(registros => {
            const container = document.getElementById("registroList");
            container.innerHTML = "";

            if (!registros.length) {
                container.innerHTML = "<p>No tienes registros aún.</p>";
                return;
            }

            registros.forEach(r => {
                const div = document.createElement("div");
                div.className = "card p-3 mb-2";
                div.innerHTML = `
                    <strong>${r.fecha}</strong> - ${r.horario}<br>
                    Comida: <b>${r.nombreAlimento}</b><br>
                    Calorías totales: ${(r.caloriasTotales || 0).toFixed(2)} kcal
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Error cargando registros:", err);
            alert("Hubo un problema al cargar tus registros");
        });
}

