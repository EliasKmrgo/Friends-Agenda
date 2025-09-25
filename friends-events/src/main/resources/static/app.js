const API = {
  people: (q, source) => `/api/personas?source=${source || 'all'}&q=${encodeURIComponent(q || '')}`,
  graph: (type) => `/api/graph?type=${type||'people'}`,
  person: (id) => `/api/person/${id}`,
  upsert: `/api/person`
};

let cy=null, currentSource='all', selectedNode=null, searchTimer=null, creating=false;

document.addEventListener('DOMContentLoaded', ()=>{
  initGraph(); loadPeople(); loadGraph();
  document.getElementById('btnAdd').addEventListener('click', openCreateModal);
});

function initGraph(){
  cy = cytoscape({
    container: document.getElementById('cy'),
    elements: [],
    style: [
      { selector:'node', style:{ 'label':'data(label)', 'background-color':'#a3bffa', 'text-valign':'center','text-halign':'center','width':'label','height':'label','padding':'10px'}},
      { selector:'edge', style:{ 'width':2, 'line-color':'#c7d2fe', 'target-arrow-shape':'triangle','target-arrow-color':'#c7d2fe'}}
    ],
    layout:{ nombre:'cose' }
  });
  cy.on('tap','node',evt=>{
    const id=evt.target.data('id'); selectPerson(id);
  });
}

async function loadGraph(){
  try{
    const res=await fetch(API.graph('people')); if(!res.ok) throw new Error(await res.text());
    const data=await res.json(), elements=[];
    (data.nodes||[]).forEach(n=>elements.push({data:{id:n.id,label:n.label,props:n.props}}));
    (data.edges||[]).forEach(e=>elements.push({data:{id:e.id||`${e.source}-${e.target}`,source:e.source,target:e.target,label:e.label}}));
    cy.elements().remove(); cy.add(elements); cy.layout({nombre:'cose'}).run();
  }catch(err){ console.error(err) }
}

async function loadPeople(){
  const q=document.getElementById('searchInput').value||'';
  const source=document.getElementById('loadButton').value;
  currentSource=source;
  try{
    const res=await fetch(API.people(q,source)); if(!res.ok) throw new Error(await res.text());
    renderPeopleList(await res.json());
  }catch(err){ console.error(err) }
}


async function selectPerson(id){
  try{
    const res=await fetch(API.person(id)); if(!res.ok) throw new Error(await res.text());
    showDetails(await res.json(), false);
    cy.nodes().unselect(); const node=cy.getElementById(String(id)); if(node) node.select();
  }catch(err){ console.error(err) }
}

function showDetails(p,isNew){
  selectedNode=p; creating=isNew;
  document.getElementById('detailTitle').textContent=isNew?'Nueva persona':(p.nombre||'Detalle');
  document.getElementById('detailForm').style.display='block';
  document.getElementById('btnBack').style.display=isNew?'inline-block':'none';
  document.getElementById('fieldId').value=p.id||'';
  document.getElementById('fieldName').value=p.nombre||'';
  document.getElementById('fieldDob').value=p.fechaNac||'';
}

async function savePerson(){
  const id=document.getElementById('fieldId').value||null;
  const payload={id:id||undefined,nombre:fieldName.value,fechaNac:fieldDob.value,source:currentSource};
  try{
    const res=await fetch(API.upsert,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(payload)});
    if(!res.ok) throw new Error(await res.text());
    const saved=await res.json(); alert('Guardado correctamente');
    loadPeople(); loadGraph(); showDetails(saved,false);
  }catch(err){ alert('No se pudo guardar') }
}

async function deletePerson(){
  const id=fieldId.value; if(!id) return alert('No hay id'); if(!confirm('Confirmar eliminar')) return;
  try{
    const res=await fetch(API.person(id),{method:'DELETE'}); if(!res.ok) throw new Error(await res.text());
    alert('Eliminado'); detailForm.style.display='none'; loadPeople(); loadGraph();
  }catch(err){ alert('No se pudo eliminar') }
}

function openCreateModal(){
  showDetails({id:'',nombre:'',fechaNac:''}, true);
}
function closeCreateModal(){
  document.getElementById('detailForm').style.display='none';
  document.getElementById('detailTitle').textContent='Selecciona un nodo';
  document.getElementById('btnBack').style.display='none';
}

function switchTab(e){
  document.querySelectorAll('.tab').forEach(t=>t.classList.remove('active'));
  e.currentTarget.classList.add('active');
  const tab=e.currentTarget.dataset.tab;
  if(tab==='graph') loadGraph(); if(tab==='hobbies') loadRaw(); if(tab==='events') loadEvents();
}

function refreshAll(){ loadPeople(); loadGraph() }
function debouncedSearch(){ clearTimeout(searchTimer); searchTimer=setTimeout(()=>loadPeople(),250) }
function escapeHtml(s){ return String(s||'').replace(/[&<>"]+/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]||c)) }
async function loadRaw(){ console.log(await (await fetch('/api/hobbies')).text()) }
function loadEvents(){ console.warn('events not implemented') }

//FUNCIONES JAVASCRIPT

function renderPeopleList(people){
  const c=document.getElementById('peopleList'); c.innerHTML='';
  people.forEach(p=>{
    const div=document.createElement('div'); div.className='list-item'; div.dataset.id=p.id;
    div.innerHTML=`
      <div class="meta">
        <div class="avatar">${(p.nombre||'?').charAt(0).toUpperCase()}</div>
        <div>
          <div style="font-weight:600">${escapeHtml(p.nombre||'Sin nombre')}</div>
          <div class="expandable">${escapeHtml(p.dob||'')}</div>
        </div>
      </div>
      `;
    c.appendChild(div);
  });
}

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
