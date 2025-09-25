/*
 * ------------------------------------------------------------
 * Script de inicialización: creación de colecciones e índices
 *
 * Base de datos objetivo: friends_agenda
 * Colecciones: persons, events, hobbies, memberships, friendships, raw_ingest
 * Índices únicos:
 *   - persons(canonicalId)
 *   - events(canonicalId)
 *   - hobbies(canonicalId)
 *   - memberships(personId, hobbyId)
 *   - friendships(personA, personB)
 * Índices de consulta:
 *   - persons("name.full" texto)
 *   - persons("emails.value" asc)
 *   - events(startsAt asc)
 *   - events("location.city" asc)
 *
 * Nota: En MongoDB sólo puede existir un índice de texto por colección y
 * los índices de texto no se combinan libremente con otras claves. Por ello,
 * se crean dos índices separados en persons: uno de texto y otro ascendente.
 * ------------------------------------------------------------
 */

(function () {
  const dbName = 'friends_agenda';
  const database = db.getSiblingDB(dbName);

  // Crear colecciones requeridas (si no existen)
  const requiredCollections = [
    'persons',
    'events',
    'hobbies',
    'memberships',
    'friendships',
    'raw_ingest',
  ];

  const existing = new Set(database.getCollectionNames());
  requiredCollections.forEach((c) => {
    if (!existing.has(c)) {
      database.createCollection(c);
    }
  });

  // Índices únicos
  database.persons.createIndex({ canonicalId: 1 }, {
    unique: true,
    name: 'uniq_persons_canonicalId',
  });

  database.events.createIndex({ canonicalId: 1 }, {
    unique: true,
    name: 'uniq_events_canonicalId',
  });

  database.hobbies.createIndex({ canonicalId: 1 }, {
    unique: true,
    name: 'uniq_hobbies_canonicalId',
  });

  database.memberships.createIndex({ personId: 1, hobbyId: 1 }, {
    unique: true,
    name: 'uniq_memberships_person_hobby',
  });

  database.friendships.createIndex({ personA: 1, personB: 1 }, {
    unique: true,
    name: 'uniq_friendships_pair',
  });

  // Índices de consulta
  database.persons.createIndex({ 'name.full': 'text' }, {
    name: 'text_persons_name_full',
  });

  database.persons.createIndex({ 'emails.value': 1 }, {
    name: 'idx_persons_emails_value',
  });

  database.events.createIndex({ startsAt: 1 }, {
    name: 'idx_events_startsAt',
  });

  database.events.createIndex({ 'location.city': 1 }, {
    name: 'idx_events_location_city',
  });
})();

