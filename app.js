const API_URL = '/notas';

// ─── Cargar notas al iniciar ───────────────────────────
document.addEventListener('DOMContentLoaded', cargarNotas);

// ─── Guardar nota ──────────────────────────────────────
async function guardarNota() {
    const titulo    = document.getElementById('titulo').value.trim();
    const contenido = document.getElementById('contenido').value.trim();

    if (!titulo || !contenido) {
        mostrarMensaje('Por favor completa el título y el contenido.', 'error');
        return;
    }

    const btn = document.getElementById('btnGuardar');
    btn.disabled = true;
    btn.textContent = 'Guardando…';

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ titulo, contenido })
        });

        if (!response.ok) throw new Error('Error al guardar la nota.');

        document.getElementById('titulo').value    = '';
        document.getElementById('contenido').value = '';
        mostrarMensaje('✓ Nota guardada exitosamente.', 'success');
        await cargarNotas();

    } catch (err) {
        mostrarMensaje('✗ ' + err.message, 'error');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<span class="btn-icon">+</span> Guardar nota';
    }
}

// ─── Cargar todas las notas ────────────────────────────
async function cargarNotas() {
    const contenedor = document.getElementById('listaNotas');
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('No se pudieron cargar las notas.');

        const notas = await response.json();
        renderizarNotas(notas);

    } catch (err) {
        contenedor.innerHTML = `<p class="error-msg">Error: ${err.message}</p>`;
    }
}

// ─── Eliminar nota ─────────────────────────────────────
async function eliminarNota(id, btnEl) {
    btnEl.disabled  = true;
    btnEl.textContent = '…';

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('No se pudo eliminar la nota.');

        mostrarMensaje('✓ Nota eliminada.', 'success');
        await cargarNotas();

    } catch (err) {
        mostrarMensaje('✗ ' + err.message, 'error');
        btnEl.disabled  = false;
        btnEl.textContent = 'Eliminar';
    }
}

// ─── Renderizar lista ──────────────────────────────────
function renderizarNotas(notas) {
    const contenedor = document.getElementById('listaNotas');
    const contador   = document.getElementById('contador');

    contador.textContent = notas.length;

    if (notas.length === 0) {
        contenedor.innerHTML = `
            <div class="empty-state">
                <span class="empty-icon">◎</span>
                <p>Aún no hay notas. ¡Crea la primera!</p>
            </div>`;
        return;
    }

    // Ordenar de más reciente a más antigua
    notas.sort((a, b) => new Date(b.fechaCreacion) - new Date(a.fechaCreacion));

    contenedor.innerHTML = notas.map(nota => `
        <article class="nota-card" data-id="${nota.id}">
            <div class="nota-titulo">${escaparHtml(nota.titulo)}</div>
            <div class="nota-contenido">${escaparHtml(nota.contenido)}</div>
            <div class="nota-meta">
                <span class="nota-fecha">${formatearFecha(nota.fechaCreacion)}</span>
                <button class="btn-eliminar" onclick="eliminarNota(${nota.id}, this)">
                    Eliminar
                </button>
            </div>
        </article>
    `).join('');
}

// ─── Helpers ───────────────────────────────────────────
function mostrarMensaje(texto, tipo) {
    const el = document.getElementById('mensaje');
    el.textContent = texto;
    el.className   = `mensaje ${tipo}`;
    clearTimeout(el._timeout);
    el._timeout = setTimeout(() => {
        el.className = 'mensaje hidden';
    }, 3500);
}

function formatearFecha(fechaStr) {
    if (!fechaStr) return '';
    const d = new Date(fechaStr);
    return d.toLocaleString('es-CO', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function escaparHtml(texto) {
    const div = document.createElement('div');
    div.appendChild(document.createTextNode(texto));
    return div.innerHTML;
}

// ─── Enviar con Enter en el título ─────────────────────
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('titulo').addEventListener('keydown', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            document.getElementById('contenido').focus();
        }
    });
});
