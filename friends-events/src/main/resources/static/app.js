let personas = [
  { id: 1, nombre: "Juan", fecha: "1995-06-10", hobbies: ["futbol"], eventos: ["partido"], amigos: [2] },
  { id: 2, nombre: "Sofía", fecha: "1998-11-20", hobbies: ["pintura"], eventos: ["exposición"], amigos: [1] },
  { id: 3, nombre: "Daniel", fecha: "1996-08-15", hobbies: ["lectura"], eventos: ["partido"], amigos: [] }
];

let personaSeleccionada = null;
let filtro = "personas"; // "personas" | "eventos" | "hobbies"
let textoBusqueda = "";

// --- RENDER LISTA ---
function renderLista() {
  const lista = document.getElementById("listaPersonas");
  lista.innerHTML = "";

  let filtradas = personas;
  if (textoBusqueda.trim()) {
    filtradas = filtradas.filter(p =>
      p.nombre.toLowerCase().includes(textoBusqueda.toLowerCase())
    );
  }

  filtradas.forEach(p => {
    const li = document.createElement("li");
    li.className = "flex items-center space-x-2 cursor-pointer hover:bg-gray-100 p-2 rounded";
    li.innerHTML = `<div class="bg-gray-300 w-8 h-8 rounded-full flex items-center justify-center">${p.nombre[0]}</div><span>${p.nombre}</span>`;
    li.onclick = () => mostrarDetalle(p.id);
    lista.appendChild(li);
  });
}

// --- DETALLE ---
function mostrarDetalle(id) {
  personaSeleccionada = personas.find(p => p.id === id);
  const cont = document.getElementById("detalleContenido");
  document.getElementById("btnEliminar").classList.remove("hidden");
  cont.innerHTML = `
    <p><strong>Id:</strong> ${personaSeleccionada.id}</p>
    <p><strong>Nombre:</strong> ${personaSeleccionada.nombre}</p>
    <p><strong>Fecha de nacimiento:</strong> ${personaSeleccionada.fecha}</p>
    <p><strong>Hobbies:</strong> ${personaSeleccionada.hobbies.join(", ")}</p>
    <p><strong>Eventos:</strong> ${personaSeleccionada.eventos.join(", ")}</p>
  `;
  renderGrafo();
}

// --- CREAR PERSONA ---
function abrirModal() {
  document.getElementById("modalCrear").classList.remove("hidden");
  const select = document.getElementById("selectRelacion");
  select.innerHTML = "";
  personas.forEach(p => {
    const opt = document.createElement("option");
    opt.value = p.id;
    opt.textContent = p.nombre;
    select.appendChild(opt);
  });
}

function guardarPersona() {
  const nombre = document.getElementById("inputNombre").value.trim();
  if (!nombre) return alert("Nombre es obligatorio");
  const fecha = document.getElementById("inputFecha").value;
  const hobbies = document.getElementById("inputHobbies").value.split(",").map(h => h.trim()).filter(Boolean);
  const eventos = document.getElementById("inputEventos").value.split(",").map(e => e.trim()).filter(Boolean);
  const relaciones = Array.from(document.getElementById("selectRelacion").selectedOptions).map(o => parseInt(o.value));

  const nueva = {
    id: Date.now(),
    nombre,
    fecha,
    hobbies,
    eventos,
    amigos: relaciones
  };

  personas.push(nueva);
  relaciones.forEach(id => {
    const amigo = personas.find(p => p.id === id);
    if (!amigo.amigos.includes(nueva.id)) amigo.amigos.push(nueva.id);
  });

  cerrarModal();
  renderLista();
  renderGrafo();
}

function cerrarModal() {
  document.getElementById("modalCrear").classList.add("hidden");
}

// --- FILTRO DE DATOS PARA EL GRAFO ---
function getPersonasFiltradas() {
  let resultado = personas;

  // Filtrado por texto de búsqueda
  if (textoBusqueda.trim()) {
    resultado = resultado.filter(p =>
      p.nombre.toLowerCase().includes(textoBusqueda.toLowerCase())
    );
  }

  // Filtrado por tipo de grafo
  if (filtro === "eventos") {
    resultado = resultado.filter(p =>
      p.eventos.some(e => resultado.some(o => o.id !== p.id && o.eventos.includes(e)))
    );
  } else if (filtro === "hobbies") {
    resultado = resultado.filter(p =>
      p.hobbies.some(h => resultado.some(o => o.id !== p.id && o.hobbies.includes(h)))
    );
  }

  return resultado;
}

// --- GRAFO ---
function renderGrafo() {
  const svg = d3.select("#grafo");
  svg.selectAll("*").remove();

  const nodosFiltrados = getPersonasFiltradas();
  const nodes = nodosFiltrados.map(p => ({ id: p.id, nombre: p.nombre }));

  // Crear links dinámicamente según el filtro
  let links = [];
  if (filtro === "personas") {
    nodosFiltrados.forEach(p => {
      p.amigos.forEach(a => {
        if (nodosFiltrados.find(o => o.id === a)) {
          links.push({ source: p.id, target: a });
        }
      });
    });
  } else if (filtro === "eventos") {
    nodosFiltrados.forEach((p, i) => {
      nodosFiltrados.slice(i + 1).forEach(o => {
        if (p.eventos.some(e => o.eventos.includes(e))) {
          links.push({ source: p.id, target: o.id });
        }
      });
    });
  } else if (filtro === "hobbies") {
    nodosFiltrados.forEach((p, i) => {
      nodosFiltrados.slice(i + 1).forEach(o => {
        if (p.hobbies.some(h => o.hobbies.includes(h))) {
          links.push({ source: p.id, target: o.id });
        }
      });
    });
  }

  const width = 400, height = 300;
  svg.attr("width", width).attr("height", height);

  const simulation = d3.forceSimulation(nodes)
    .force("link", d3.forceLink(links).id(d => d.id).distance(100))
    .force("charge", d3.forceManyBody().strength(-300))
    .force("center", d3.forceCenter(width / 2, height / 2));

  const link = svg.append("g")
    .attr("stroke", "#ccc")
    .selectAll("line")
    .data(links)
    .enter().append("line")
    .attr("stroke-width", 2);

  const node = svg.append("g")
    .selectAll("circle")
    .data(nodes)
    .enter().append("circle")
    .attr("r", 20)
    .attr("fill", "steelblue")
    .on("click", (e, d) => mostrarDetalle(d.id));

  const label = svg.append("g")
    .selectAll("text")
    .data(nodes)
    .enter().append("text")
    .attr("text-anchor", "middle")
    .attr("dy", 5)
    .text(d => d.nombre)
    .attr("fill", "white");

  simulation.on("tick", () => {
    link
      .attr("x1", d => d.source.x)
      .attr("y1", d => d.source.y)
      .attr("x2", d => d.target.x)
      .attr("y2", d => d.target.y);

    node
      .attr("cx", d => d.x)
      .attr("cy", d => d.y);

    label
      .attr("x", d => d.x)
      .attr("y", d => d.y);
  });
}

// --- EVENTOS ---
document.getElementById("btnAgregar").onclick = abrirModal;
document.getElementById("btnGuardar").onclick = guardarPersona;
document.getElementById("btnCancelar").onclick = cerrarModal;
document.getElementById("btnActualizar").onclick = renderGrafo;
document.getElementById("btnEliminar").onclick = () => {
  if (!personaSeleccionada) return;
  personas = personas.filter(p => p.id !== personaSeleccionada.id);
  personas.forEach(p => p.amigos = p.amigos.filter(id => id !== personaSeleccionada.id));
  personaSeleccionada = null;
  renderLista();
  renderGrafo();
  document.getElementById("detalleContenido").innerHTML = "";
  document.getElementById("btnEliminar").classList.add("hidden");
};

// --- BUSCAR ---
document.getElementById("btnBuscar").onclick = () => {
  textoBusqueda = document.getElementById("inputBuscar").value;
  renderLista();
  renderGrafo();
};

// --- BOTONES DE FILTRO ---
document.getElementById("btnVerPersonas").onclick = () => {
  filtro = "personas";
  renderGrafo();
};
document.getElementById("btnVerEventos").onclick = () => {
  filtro = "eventos";
  renderGrafo();
};
document.getElementById("btnVerHobbies").onclick = () => {
  filtro = "hobbies";
  renderGrafo();
};

// Inicializar
renderLista();
renderGrafo();
