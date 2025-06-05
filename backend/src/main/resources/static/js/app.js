

let registroEditandoId = null;
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
    const datos = obtenerDatosFormulario();
    if (!validarDatos(datos)) return;
    if (registroEditandoId) {
        modificarRegistro(registroEditandoId, datos);
    } else {
        crearNuevoRegistro(datos);
    }
}


function obtenerDatosFormulario() {
    const currentUser = JSON.parse(localStorage.getItem("currentUser"));
    return {
        fecha: document.getElementById("fechaRegistro").value,
        horario: document.getElementById("horarioRegistro").value,
        nombre: document.getElementById("manualNombre").value,  // <-- aquí debe ser nombre
        cantidad: parseFloat(document.getElementById("manualCantidad").value),
        calorias: parseFloat(document.getElementById("manualCalorias").value),
        proteinas: parseFloat(document.getElementById("manualProteinas").value),
        carbohidratos: parseFloat(document.getElementById("manualCarbohidratos").value),
        grasas: parseFloat(document.getElementById("manualGrasas").value),
        idUsuario: currentUser.id
    };
}


function validarDatos(datos) {
    if (!datos.fecha || !datos.horario || !datos.nombre || isNaN(datos.cantidad)) {
        alert("Completa todos los campos obligatorios.");
        return false;
    }
    if (!registroEditandoId && isNaN(datos.calorias)) {
        alert("Faltan datos de calorías");
        return false;
    }
    return true;
}


function crearNuevoRegistro(datos) {
    fetch("/api/registros/registrar-comida-manual", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
    })
        .then(res => {
            if (!res.ok) throw new Error("Error al guardar registro");
            return res.json();
        })
        .then(() => {
            alert("✅ Registro guardado");
            limpiarFormulario();
            cargarRegistros();
        })
        .catch(err => {
            alert("❌ Hubo un error al guardar");
            console.error("Error:", err);
        });
}

function modificarRegistro(id, datos) {
    // Para modificar, no enviamos macros porque solo editamos fecha, horario, nombre y cantidad
    const payload = {
        fecha: datos.fecha,
        horario: datos.horario,
        nombreAlimento: datos.nombreAlimento,
        cantidad: datos.cantidad
    };

    fetch(`/api/registros/modificar-comida-manual/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Error al modificar");
            alert("✅ Registro modificado");
            registroEditandoId = null;
            limpiarFormulario();
            cargarRegistros();
        })
        .catch(err => {
            alert("❌ Error al modificar");
            console.error(err);
        });
}

async function eliminarRegistro(id) {
    const confirmado = confirm("¿Estás seguro de eliminar este registro?");
    if (!confirmado) return;

    const response = await fetch(`http://localhost:8080/api/registros/${id}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        alert("Error al eliminar el registro");
        return;
    }

    alert("Registro eliminado permanentemente");
    cargarRegistros(); // Actualizar la lista
}

function limpiarFormulario() {
    document.getElementById("manualNombre").value = "";
    document.getElementById("manualCalorias").value = "";
    document.getElementById("manualProteinas").value = "";
    document.getElementById("manualCarbohidratos").value = "";
    document.getElementById("manualGrasas").value = "";
    document.getElementById("manualCantidad").value = "";
    registroEditandoId = null;
}

function buscarAlimentos(termino) {
    fetch(`/api/alimentos/buscar-alimentos?termino=${encodeURIComponent(termino)}`)
        .then(res => {
            if (!res.ok) throw new Error("Error al buscar alimentos");
            return res.json();
        })
        .then(data => {
            const container = document.getElementById("suggestionsContainer");
            container.innerHTML = "";

            data.forEach(alimento => {
                const div = document.createElement("div");
                div.textContent = alimento.nombre;
                div.className = "suggestion-item";
                div.onclick = () => seleccionarAlimento(alimento);
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Error al buscar alimentos:", err);
        });
}




function seleccionarAlimento(alimento) {
    document.getElementById("manualNombre").value = alimento.nombre;
    document.getElementById("manualCalorias").value = alimento.calorias;
    document.getElementById("manualProteinas").value = alimento.proteinas;
    document.getElementById("manualCarbohidratos").value = alimento.carbohidratos;
    document.getElementById("manualGrasas").value = alimento.grasas;

    // Opcionalmente limpiar sugerencias
    document.getElementById("suggestionsContainer").innerHTML = "";
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

function prepararEdicion(registro) {
    registroEditandoId = registro.id;

    document.getElementById("fechaRegistro").value = registro.fecha;
    document.getElementById("horarioRegistro").value = registro.horario;
    document.getElementById("manualNombre").value = registro.nombreAlimento;
    document.getElementById("manualCantidad").value = registro.cantidad;

    // No modificamos macros porque no vienen del back en este caso
    alert("Editando registro. Modificá y hacé clic en 'Guardar Registro'.");
}
