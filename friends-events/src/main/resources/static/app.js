const API = {
  people: (q, source) => `/api/personas?source=${source || 'all'}&q=${encodeURIComponent(q || '')}`,
  graph: (type) => `/api/graph?type=${type||'people'}`,
  person: (id) => `/api/person/${id}`,
  upsert: `/api/person`
};

let cy=null, currentSource='all', selectedNode=null, searchTimer=null, creating=false;

document.addEventListener('DOMContentLoaded', ()=>{
  initGraph();
  loadPeople();
  loadGraph();
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
    layout:{ name:'cose' }
  });
  cy.on('tap','node',evt=>{
    const id=evt.target.data('id');
    selectPerson(id);
  });
}

async function loadGraph(){
  try{
    const res=await fetch(API.graph('people'));
    if(!res.ok) throw new Error(await res.text());
    const data=await res.json(), elements=[];
    (data.nodes||[]).forEach(n=>elements.push({data:{id:n.id,label:n.label,props:n.props}}));
    (data.edges||[]).forEach(e=>elements.push({data:{id:e.id||`${e.source}-${e.target}`,source:e.source,target:e.target,label:e.label}}));
    cy.elements().remove();
    cy.add(elements);
    cy.layout({name:'cose'}).run();
  }catch(err){ console.error(err) }
}

async function loadPeople(){
  const q=document.getElementById('searchInput').value||'';
  const source=document.getElementById('loadButton').value;
  currentSource=source;
  try{
    const res=await fetch(API.people(q,source));
    if(!res.ok) throw new Error(await res.text());
    renderPeopleList(await res.json());
  }catch(err){ console.error(err) }
}

function renderPeopleList(people){
  const c=document.getElementById('peopleList');
  c.innerHTML='';
  people.forEach(p=>{
    const div=document.createElement('div');
    div.className='list-item';
    div.dataset.id=p.id;
    div.innerHTML=`
      <div class="meta">
        <div class="avatar">${(p.name||'?').charAt(0).toUpperCase()}</div>
        <div>
          <div style="font-weight:600">${escapeHtml(p.name||'Sin nombre')}</div>
          <div class="expandable">${escapeHtml(p.title||p.email||'')}</div>
        </div>
      </div>
      <div><button onclick="selectPerson('${p.id}')">Ver</button></div>`;
    c.appendChild(div);
  });
}

async function selectPerson(id){
  try{
    const res=await fetch(API.person(id));
    if(!res.ok) throw new Error(await res.text());
    showDetails(await res.json(), false);
    cy.nodes().unselect();
    const node=cy.getElementById(String(id));
    if(node) node.select();
  }catch(err){ console.error(err) }
}

function showDetails(p,isNew){
  selectedNode=p;
  creating=isNew;
  document.getElementById('detailTitle').textContent=isNew?'Nueva persona':(p.name||'Detalle');
  document.getElementById('detailForm').style.display='block';
  document.getElementById('btnBack').style.display=isNew?'inline-block':'none';
  document.getElementById('fieldId').value=p.id||'';
  document.getElementById('fieldName').value=p.name||'';
  document.getElementById('fieldEmail').value=p.email||'';
  document.getElementById('fieldNotes').value=p.notes||'';
}

async function savePerson(){
  const id=document.getElementById('fieldId').value||null;
  const payload={id:id||undefined,name:fieldName.value,email:fieldEmail.value,notes:fieldNotes.value,source:currentSource};
  try{
    const res=await fetch(API.upsert,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(payload)});
    if(!res.ok) throw new Error(await res.text());
    const saved=await res.json();
    alert('Guardado correctamente');
    loadPeople();
    loadGraph();
    showDetails(saved,false);
  }catch(err){ alert('No se pudo guardar') }
}

async function deletePerson(){
  const id=fieldId.value;
  if(!id) return alert('No hay id');
  if(!confirm('Confirmar eliminar')) return;
  try{
    const res=await fetch(API.person(id),{method:'DELETE'});
    if(!res.ok) throw new Error(await res.text());
    alert('Eliminado');
    detailForm.style.display='none';
    loadPeople();
    loadGraph();
  }catch(err){ alert('No se pudo eliminar') }
}

function openCreateModal(){ showDetails({id:'',name:'',email:'',notes:''}, true); }

function closeCreateModal(){
  document.getElementById('detailForm').style.display='none';
  document.getElementById('detailTitle').textContent='Selecciona un nodo';
  document.getElementById('btnBack').style.display='none';
}

function switchTab(e){
  document.querySelectorAll('.tab').forEach(t=>t.classList.remove('active'));
  e.currentTarget.classList.add('active');
  const tab=e.currentTarget.dataset.tab;
  if(tab==='graph') loadGraph();
  if(tab==='hobbies') loadRaw();
  if(tab==='events') loadEvents();
}

function refreshAll(){ loadPeople(); loadGraph(); }

function debouncedSearch(){ clearTimeout(searchTimer); searchTimer=setTimeout(()=>loadPeople(),250); }

function escapeHtml(s){ return String(s||'').replace(/[&<>"]+/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]||c)); }

async function loadRaw(){ console.log(await (await fetch('/api/hobbies')).text()); }

function loadEvents(){ console.warn('events not implemented'); }
